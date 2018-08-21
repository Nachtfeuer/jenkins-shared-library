package my.tools

/**
 * Python virtual environment wrapper
 */
class VirtualEnv extends Base {
    /** folder name of the virtual environment (default: venv). */
    private String theFolderName = 'venv'
    /** list of requirements to install with pip */
    private final List<String> theRequirements = []

    /**
     * Initializing gradle with Jenkinsfile script instance only.
     */
    VirtualEnv(final script) {
        super(script)
    }

    /**
     * @return currently configured folder name for Python virtual environment
     */
    String getFolderName() {
        this.theFolderName
    }

    /**
     * @return list of configured requirements.
     */
    List<String> getRequirements() {
        this.theRequirements.asImmutable()
    }

    /**
     * Configuration of the Python virtual environment.
     * @param config either a Map or named parameter <b>folderName</b> which defines folder for
     *        virtual environment is supported.
     */
    VirtualEnv configure(final Map config) {
        if (config.containsKey('folderName') && config.folderName instanceof String
                                             && !config.folderName.isEmpty()) {
            this.theFolderName = config.folderName
        } else if (config.containsKey('requirements') && config.requirements instanceof List
                                                      && config.requirements.every { it instanceof String }) {
            this.theRequirements.clear()
            this.theRequirements.addAll(config.requirements)
        }
        this
    }

    /**
     * Provides block to define scope for a virtualenv.
     * @param body closure to create a block.
     * @return whatever the inner block does return.
     */
    def process(final Closure body) {
        this.script.withEnv(this.environment()) {
            this.setup()
            def result = body()
            this.teardown()
            result
        }
    }

    /**
     * Create a virtualenv.
     */
    private void setup() {
        this.script.sh(script:'virtualenv ' + this.theFolderName)
        for (def ix = 0; ix < this.theRequirements.size(); ++ix) {
            this.script.sh(script:"python -m pip install ${theRequirements[ix]}")
        }
    }

    /**
     * Remove the virtualenv.
     */
    private void teardown() {
        this.script.sh(script:'rm -rf ' + this.theFolderName)
    }

    /**
     * @return Adjusted environment variables required for virtual environment.
     */
    private List<String> environment() {
        final VIRTUAL_ENV_PATH = this.script.pwd() + '/' + this.theFolderName
        [
            "PATH=${VIRTUAL_ENV_PATH}/bin:${script.env.PATH}",
            "VIRTUAL_ENV=${VIRTUAL_ENV_PATH}",
        ]
    }
}
