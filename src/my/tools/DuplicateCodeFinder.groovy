package my.tools

/**
 * Detecting duplicate code.
 */
class DuplicateCodeFinder extends Base {
    /** class that does the source comparison */
    private final SourceCompare sourceCompare
    /** path and filenames and the loaded sources lines */
    private final Map<String, List<String>> theSources = [:]
    /** final report data about the found duplicates */
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
        this.writeHtmlReport()
        this.theReportData.isEmpty()
    }

    /**
     * Provide code lines in defined range for a concrete path and filename.
     * @param pathAndFileName path and filename of source code
     * @param line line index
     * @param blockSize number of lines of the code block
     * @return list of lines or an empty list if not found.
     */
    private List<String> getCode(final String pathAndFileName, final int line, final int blockSize) {
        def lines = []
        def result = this.theSources.find { it.key ==  pathAndFileName }
        if (result) {
            lines.addAll(result.value[line..line + blockSize - 1])
        }
        lines
    }

    /**
     * Render the HTML report and write it to duplicates/index.html (relative to current folder).
     */
    private void writeHtmlReport() {
        this.script.sh(script:'mkdir -p duplicates')
        this.script.writeFile(file:'duplicates/index.html', text:renderHtmlReport())
    }

    /**
     * @return HTML report as string.
     */
    @SuppressWarnings('GStringExpressionWithinString')
    private String renderHtmlReport() {
        def template = '''
        yieldUnescaped '<!DOCTYPE html>'
        html {
            head {
                meta(charset:'utf-8')
                meta(name:'viewport', content:'width=device-width, initial-scale=1, shrink-to-fit=no')
                title('Duplicate Code Report')
                link(rel:'stylesheet', href:'https://maxcdn.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css',
                     integrity:'sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO',
                     crossorigin:'anonymous')
            }

            body {
                div(class:'container-fluid') {
                    table(class:'table table-border') {
                        thead(class:'thead-dark') {
                            tr{ th('File Information') th('Source Code') }
                        }

                        tbody {
                            model.report.each {
                                def item = it
                                tr {
                                    td {
                                        b('First file:')
                                        yieldUnescaped('<br/>')
                                        yieldUnescaped("$item.sources.pathAndFileName1") yieldUnescaped('<br/>')

                                        b('Second file:')
                                        yieldUnescaped('<br/>')
                                        yieldUnescaped("$item.sources.pathAndFileName2")
                                    }

                                    td {
                                        item.results.each {
                                            def duplicate = it
                                            b('Positions:')
                                            span("line ${duplicate.indices[0]} - line ${duplicate.indices[1]}, ")
                                            b('Duplicate lines:') span("${duplicate.blockSize}")
                                            pre(class:'p-2 bg-warning border border-danger') {
                                                model.getCode(item.sources.pathAndFileName1, duplicate.indices[0],
                                                              duplicate.blockSize).each {
                                                    yieldUnescaped("$it") yieldUnescaped('<br/>')
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        '''
        def getCodeWrapper = { p, l, b -> this.getCode(p, l, b) }
        new Renderer(this.script).render(template, [
            report:this.theReportData, getCode:getCodeWrapper])
    }
}
