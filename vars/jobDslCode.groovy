import my.tools.JobDslConverter

/**
 * Generating Job DSL code from a JSON file representing one job.
 * @param jsonPathAndFilename path and filename of JSON file.
 * @return Job DSL code
 */
String fromJson(final String jsonPathAndFilename) {
    new JobDslConverter(this).fromJson(jsonPathAndFilename)
}

/**
 * Generating Job DSL code from a YAML file representing one job.
 * @param yamlPathAndFilename path and filename of YAML file.
 * @return Job DSL code
 */
String fromYaml(final String yamlPathAndFilename) {
    new JobDslConverter(this).fromYaml(yamlPathAndFilename)
}
