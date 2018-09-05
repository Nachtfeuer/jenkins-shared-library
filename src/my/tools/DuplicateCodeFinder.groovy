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
        sources.each {
            final String CONTENT = this.script.readFile(file:it)
            final List<String> LINES = CONTENT.split('\n')
            this.theSources.put(it, LINES)
        }
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
                def results = sourceCompare.compareSources()
                this.theReportData.add([
                    sources:[pathAndFileName1:source1.key, pathAndFileName2:source2.key].asImmutable(),
                    results:results
                ])
            }
        }
        this.theReportData.isEmpty()
    }
}
