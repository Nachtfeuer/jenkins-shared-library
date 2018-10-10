import my.tools.DuplicateCodeFinder

/**
 * Checking for duplicate code for files in the list.
 * @param files path and filenames of code that should be checked.
 * @param policies Map of policies; possible fields are listed below.
 *
 * <b>minimumBlockSize<b>: minimum number of lines that count as duplicate code (default: 4)<br/>
 * <b>ignoreCase<b>: when true the letter case will be ignored (default: false)<br/>
 * <b>ignoreWhitespaces</b>: when true then spaces and tabs are ignored (default: false)<br/>
 * <b>percentageSimilarity</b>: when 100% then exact match otherwise partial match depending on percentags (default: 100)
 */
boolean call(final List<String> files, final Map policies = [:]) {
    def api = new DuplicateCodeFinder(this)
    api.sourceFiles = files
    api.minimumBlockSize = policies.minimumBlockSize ?: 4
    api.ignoreCase = policies.ignorecase ?: false
    api.ignoreWhitespaces = policies.ignoreWhitespaces ?: false
    api.percentageSimilarity = policies.percentageSimilarity ?: 100.0
    api.check()
}
