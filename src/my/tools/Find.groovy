package my.tools

/**
 * Comfortable wrapper for command line tool "find".
 */
class Find extends Base {
    /**
     * Initializing gradle with Jenkinsfile script instance only.
     */
    Find(final script) {
        super(script)
    }

    /**
     * Searching files (only).
     * @param path the path where to start searching of files.
     * @param name the search criteria (wildcards are allowed).
     * @return list of found files
     */
    List<String> files(final String path, final String name) {
        def result = this.script.sh(script:"find $path -type f -name \"$name\"", returnStdout:true).trim()
        result.split('\n')
    }
}
