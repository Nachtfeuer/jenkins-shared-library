package my.tools

/**
 * Converter for different formats into Job DSL code.
 */
class JobDslConverter extends Base {
    /**
     * Initializing gradle with Jenkinsfile script instance only.
     */
    JobDslConverter(final script) {
        super(script)
    }

    /**
     * Generating Job DSL code from a JSON file representing on job.
     * @param jsonPathAndFilename path and filename of JSON file.
     * @return Job DSL code
     */
    String fromJson(final String jsonPathAndFilename) {
        JobDslConverter.fromMap(this.script.readJSON(file:jsonPathAndFilename))
    }

    /**
     * Generating Job DSL code from a YAML file representing on job.
     * @param yamlPathAndFilename path and filename of YAML file.
     * @return Job DSL code
     */
    String fromYaml(final String yamlPathAndFilename) {
        JobDslConverter.fromMap(this.script.readYaml(file:yamlPathAndFilename))
    }

    /**
     * generting Job DSL code from a Map
     * @param jobDefinition Map with fields as a job definition.
     * @return Job DSL code
     */
    private static String fromMap(final Map jobDefinition) {
        def builder = new JobDslBuilder()
        jobDefinition.each {
            key, value ->
                switch (key) {
                    case 'type'          : builder.type = value as JobDslType; break
                    case 'name'          : builder.name = value; break
                    case 'description'   : builder.description = value; break
                    case 'source'        : builder.source = value; break
                    case 'script'        : builder.scriptPath = value; break
                    case 'credentialsId' : builder.credentialsId = value; break
                }
        }
        builder.build()
    }
}
