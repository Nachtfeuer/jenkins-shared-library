package my.tools

/**
 * Mock for Jenkins DSL.
 */
class MockScript {
    /** list of calls */
    private final List calls = []
    /** currently defined environment variables */
    private final env = [PATH:'']
    /** current working path. */
    private final String currentPath = System.getProperty('user.dir')
    /** status of current build and the description */
    private final Map currentBuild = [result:null, description:'']

    /** readonly access to list off calls. **/
    def getCalls() {
        this.calls.asImmutable()
    }

    /** access to current build */
    def getCurrentBuild() {
        this.currentBuild
    }

    /** mock of the Jenkins sh DSL function. */
    def sh(final Map config) {
        this.calls.add(['sh', config])
    }

    /** mock of the Jenkins pwd DSL function. */
    def pwd() {
        this.calls.add(['pwd', this.currentPath])
        this.currentPath
    }

    /** mock of the withEnv DSL function. */
    def withEnv(final List<String> environment, final Closure body) {
        this.calls.add(['withEnv', environment])
        def oldEnv = [:]
        oldEnv.putAll(this.env)
        environment.each {
            def tokens = it.split('=')
            this.env.put(tokens[0], tokens[1])
        }
        def result = body()
        this.env.clear()
        this.env.putAll(oldEnv)
        result
    }

    /** mock of the junit DSL function. */
    def junit(final Map config) {
        this.calls.add(['junit', config])
    }

    /** mock of the jacoco DSL function. */
    def jacoco() {
        this.calls.add(['jacoco'])
    }
}
