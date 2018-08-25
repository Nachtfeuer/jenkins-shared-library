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
        def jobDefinition = this.script.readJSON(file:jsonPathAndFilename)
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
