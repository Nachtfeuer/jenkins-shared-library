package my.tools

/**
 * Supported Job types.
 */
enum JobDslType {
    // pipeline using a Jenkinsfile that can be parametrized
    PIPELINE,
    // pipeline using a Jenkinsfile per branch (cannot be parametrized)
    MULTIBRANCH_PIPELINE
}
