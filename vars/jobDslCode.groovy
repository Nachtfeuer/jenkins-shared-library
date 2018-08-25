import my.tools.JobDslConverter

/**
 * Generating Job DSL code from a JSON file representing on job.
 * @param jsonPathAndFilename path and filename of JSON file.
 * @return Job DSL code
 */
String fromJson(final String jsonPathAndFilename) {
    new JobDslConverter(this).fromJson(jsonPathAndFilename)
}
