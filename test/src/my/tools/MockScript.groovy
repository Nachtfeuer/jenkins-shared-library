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
    /** Stack with return values */
    private final Stack provides = []
    /** publish DSL instance **/
    private final publish = [:]

    MockScript() {
        this.publish.put('html') { title, path, mainFile='index.html' -> this.publishHtml(title, path, mainFile) }
    }

    /** readonly access to list off calls. **/
    def getCalls() {
        this.calls.asImmutable()
    }

    /** access to current build */
    def getCurrentBuild() {
        this.currentBuild
    }

    /** add an entry to provide as return value for some DSL calls */
    void provide(final obj) {
        this.provides.add(obj)
    }

    /** mock own publish DSL **/
    Map getXpublish() {
        this.publish.asImmutable()
    }

    /** mock of the Jenkins sh DSL function. */
    def sh(final Map config) {
        this.calls.add(['sh', config])

        if (this.provides) {
            this.provides.pop()
        } else {
            null
        }
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
    void junit(final Map config) {
        this.calls.add(['junit', config])
    }

    /** mock of the jacoco DSL function. */
    void jacoco() {
        this.calls.add(['jacoco'])
    }

    /** mock of the readJSON DSL function. */
    Map readJSON(final Map config) {
        this.calls.add(['readJSON', config])

        if (this.provides) {
            this.provides.pop()
        } else {
            null
        }
    }

    /** mock of the readYaml DSL function. */
    Map readYaml(final Map config) {
        this.calls.add(['readYaml', config])

        if (this.provides) {
            this.provides.pop()
        } else {
            null
        }
    }

    /** mock of the readFile DSL function. */
    String readFile(final Map config) {
        this.calls.add(['readFile', config])

        if (this.provides) {
            this.provides.pop()
        } else {
            null
        }
    }

    /**
    * @param title title of the HTML report.
    * @param path releative path where the HTML report is located.
    * @param mainFile main HTML file (default: index.html)
    */
    private void publishHtml(final String title, final String path, final String mainFile='index.html') {
        this.calls.add(['xpublish.html', title, path, mainFile])
    }
}

