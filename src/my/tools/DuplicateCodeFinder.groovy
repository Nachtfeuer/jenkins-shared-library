package my.tools

/**
 * Detecting duplicate code.
 */
class DuplicateCodeFinder extends Base {
    private final SourceCompare sourceCompare
    private final Map<String, List<String>> theSources = [:]
    private final List theReportData = []

    /**
     * Initializing gradle with Jenkinsfile script instance and
     * instantiating the {@link SourceCompare} doing the real work.
     */
    DuplicateCodeFinder(final script) {
        super(script)
        this.sourceCompare = new SourceCompare()
    }

    /**
     * @return found duplicate code.
     */
    List getReportData() {
        this.theReportData*.asImmutable()
    }

    /**
     * @param sources list of path and filenames
     * @return builder itself to allow chaining of further operations.
     */
    DuplicateCodeFinder setSourceFiles(final List<String> sources) {
        this.theSources.clear()
        sources.each {
            final String CONTENT = this.script.readFile(file:it)
            final List<String> LINES = CONTENT.split('\n')
            this.theSources.put(it, LINES)
        }
        this
    }

    /**
     * Change minimum block size policy.
     *
     * @param minimumBlockSize new size to recognize as duplicate.
     * @return builder itself to allow chaining of further operations.
     */
    DuplicateCodeFinder setMinimumBlockSize(final int minimumBlockSize) {
        this.sourceCompare.minimumBlockSize = minimumBlockSize
        this
    }

    /**
     * Change ignore case policy.
     *
     * @param enabled when true then ignore case is enabled
     * @return builder itself to allow chaining of further operations.
     */
    DuplicateCodeFinder setIgnoreCase(final boolean enabled) {
        this.sourceCompare.ignoreCase = enabled
        this
    }

    /**
     * Change whitespaces policy.
     *
     * @param enabled when true then ignore whitespaces
     * @return builder itself to allow chaining of further operations.
     */
    DuplicateCodeFinder setIgnoreWhitespaces(final boolean enabled) {
        this.sourceCompare.ignoreWhitespaces = enabled
        this
    }

    /**
     * Change percentage similarity policy.
     *
     * @param percentage value (0 .. 100.0)
     * @return builder itself to allow chaining of further operations.
     */
    DuplicateCodeFinder setPercentageSimilarity(final double percentage) {
        this.sourceCompare.percentageSimilarity = percentage
        this
    }

    /**
     * Checking for duplicate code.
     * @return true when no duplicate code has been found.
     */
    boolean check() {
        // ensure path and filenames are sorted in ascending order
        this.theSources.sort()*.key
        // find all combinations
        def combinations = GroovyCollections.combinations(this.theSources, this.theSources)
        // compare all comninations once only
        combinations.each {
            def source1 = it[0].collect { [key:it.key, value:it.value] } [0]
            def source2 = it[1].collect { [key:it.key, value:it.value] } [0]
            if (source1.key <= source2.key) {
                sourceCompare.setSources(source1.value, source2.value)
                sourceCompare.filesIdentical = source1.key == source2.key

                def results = sourceCompare.compareSources()
                // we only remember duplicates when there are at least 1:
                if (results.size() > 0) {
                    // special case: when comparing one file with itself and the only duplicte
                    // is the whole file then we can ignore it:
                    boolean ignore = results.size() == 1 \
                        && source1.key == source2.key \
                        && source1.value.size() == results[0].blockSize

                    if (!ignore) {
                        this.theReportData.add([
                            sources:[pathAndFileName1:source1.key, pathAndFileName2:source2.key].asImmutable(),
                            results:results
                        ])
                    }
                }
            }
        }
        this.theReportData.isEmpty()
    }
}
