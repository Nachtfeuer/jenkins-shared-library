package my.tools

/**
 * Tool to compare sources finding duplicates with a minimum block size.
 */
class SourceCompare {
    /** whitespace characters to remove when policy ignoreWhitespaces is set to true. */
    private final static WHITESPACES = '[\t ]*'
    /** list of sources to compare (each source a list of lines) */
    private final List<List<String>> sources = []
    /** policies influencing comparison */
    private final Map policies = [
        // minimum block size to recognize as duplicate (default: 4)
        minimumBlockSize:4,
        // when true the comparison of two lines is independent of the letter case (default: false)
        ignoreCase:false,
        // when true, then ignoring spaces and tabs on camparisons (default: false)
        ignoreWhitespaces:false
    ]
    /** concrete "isEqual" compare function depending on policies */
    private final isEqual = { left, right -> this.isEqualExactMatch(left, right) }

    /**
     * Provide two sources to be compared.
     *
     * @param left first source to compare with second one.
     * @param right second source to compare with first one.
     * @return builder itself to allow chaining of further operations.
     */
    SourceCompare setSources(final String left, final String right) {
        this.sources.clear()
        [left, right].each { this.sources.add(it.split('\n')) }
        this
    }

    /**
     * Change minimum block size policy.
     *
     * @param minimumBlockSize new size to recognize as duplicate.
     * @return builder itself to allow chaining of further operations.
     */
    SourceCompare setMinimumBlockSize(final int minimumBlockSize) {
        this.policies.minimumBlockSize = minimumBlockSize
        this
    }

    /**
     * Change ignore case policy.
     *
     * @param enabled when true then ignore case is enabled
     * @return builder itself to allow chaining of further operations.
     */
    SourceCompare setIgnoreCase(final boolean enabled) {
        this.policies.ignoreCase = enabled
        this
    }

    /**
     * Change whitespaces policy.
     *
     * @param enabled when true then ignore whitespaces
     * @return builder itself to allow chaining of further operations.
     */
    SourceCompare setIgnoreWhitespaces(final boolean enabled) {
        this.policies.ignoreWhitespaces = enabled
        this
    }

    /**
     * Count duplicate consecutive lines starting by given indices.
     *
     * @param leftPosition zero based index of first source
     * @param rightPosition zero based index of second source
     * @return number of duplicate lines found starting by given indices.
     */
    int countDuplicateLines(int leftPosition, int rightPosition) {
        int identicalLines = 0
        while (leftPosition < this.sources[0].size() && rightPosition < this.sources[1].size()) {
            // lines are identical?
            if (!this.isEqual(this.sources[0][leftPosition], this.sources[1][rightPosition])) {
                break
            }
            // one identical line found
            identicalLines += 1
            // next two lines to compare
            ++leftPosition
            ++rightPosition
        }

        identicalLines
    }

    /**
     * Find all duplicate lines (recognizing minimum block size).
     *
     * @return list of information about duplicates (if given)
     */
    List compareSources() {
        def results = []

        this.transformSources()

        int leftPosition = 0
        while (leftPosition < this.sources[0].size()) {
            int offset = 0
            int rightPosition = 0
            while (rightPosition < this.sources[1].size()) {
                int duplicateLines = this.countDuplicateLines(leftPosition, rightPosition)
                if (duplicateLines >= this.policies.minimumBlockSize) {
                    results.add([indices:[leftPosition, rightPosition], blockSize:duplicateLines])
                    rightPosition += duplicateLines
                    if (offset == 0) {
                        offset += duplicateLines
                    }
                } else {
                    ++rightPosition
                }
            }
            leftPosition += (offset > 0) ? offset: 1
        }
        results
    }

    /**
     * Depending on policy transform sources.
     */
    private void transformSources() {
        if (this.policies.ignoreCase) {
            this.sources[0] = this.sources[0]*.toUpperCase()
            this.sources[1] = this.sources[1]*.toUpperCase()
        }

        if (this.policies.ignoreWhitespaces) {
            this.sources[0] = this.sources[0]*.replaceAll(WHITESPACES, '')
            this.sources[1] = this.sources[1]*.replaceAll(WHITESPACES, '')
        }
    }

    /**
     * Compare two string to be identical (exact match).
     *
     * @param left first string to compare with second one.
     * @param right second string to compare with first one.
     * @return true when both strings are identical.
     */
    private boolean isEqualExactMatch(final String left, final String right) {
        left == right
    }
}
