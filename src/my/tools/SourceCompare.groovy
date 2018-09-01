package my.tools

/**
 * Tool to compare sources finding duplicates with a minimum block size.
 */
class SourceCompare {
    /** list of sources to compare (each source a list of lines) */
    private final List<List<String>> sources = []
    /** minimum block size to recognize as duplicate (default: 4) */
    private int minimumBlockSize = 4

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
     * Change minimum block size.
     *
     * @param minimumBlockSize new size to recognize as duplicate.
     * @return builder itself to allow chaining of further operations.
     */
    SourceCompare setMinimumBlockSize(final int minimumBlockSize) {
        this.minimumBlockSize = minimumBlockSize
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
            if (this.sources[0][leftPosition] != this.sources[1][rightPosition]) {
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

        int leftPosition = 0
        while (leftPosition < this.sources[0].size()) {
            int offset = 0
            int rightPosition = 0
            while (rightPosition < this.sources[1].size()) {
                int duplicateLines = this.countDuplicateLines(leftPosition, rightPosition)
                if (duplicateLines >= this.minimumBlockSize) {
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
}
