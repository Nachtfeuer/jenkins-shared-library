package my.tools

/**
 * Generates Groovy code using Job DSL API for request job type.
 *
 * @see
 * <a href="https://jenkinsci.github.io/job-dsl-plugin/#method/javaposse.jobdsl.dsl.DslFactory.multibranchPipelineJob">
 *    Multibranch Job DSL Documentation<a/>
 */
final class JobDslBuilder {
    /** final Job type (default: multibranch pipeline) */
    private JobDslType theType = JobDslType.MULTIBRANCH_PIPELINE
    /** final Job name */
    private String theName
    /** final Job description */
    private String theDescription
    /** final SCM source like git url */
    private String theSource
    /** final SCM source credentials ID */
    private String theCredentialsId = ''
    /** final relative path and filename of Jenkinsfile */
    private String theScriptPath
    /** final number of builds to keep in history list */
    private Integer theHistory = 30

    /**
     * Changing the job type.
     *
     * @param type adjusting job type (multibranch pipeline or pipeline).
     * @return builder itself for further operations
     */
    JobDslBuilder setType(final JobDslType type) {
        this.theType = type
        this
    }

    /**
     * Changing the job name.
     *
     * @param name new name for the job
     * @return builder itself for further operations
     */
    JobDslBuilder setName(final String name) {
        this.theName = name
        this
    }

    /**
     * Changing the job description.
     *
     * @param description new description for the job.
     * @return builder itself for further operations
     */
    JobDslBuilder setDescription(final String description) {
        this.theDescription = description
        this
    }

    /**
     * Changing the SCM source (like a git url).
     *
     * @param source  new SCM source.
     * @return builder itself for further operations
     */
    JobDslBuilder setSource(final String source) {
        this.theSource = source
        this
    }

    /**
     * Changing the SCM credentials ID.
     *
     * @param source  new SCM credentials ID.
     * @return builder itself for further operations
     */
    JobDslBuilder setCredentialsId(final String credentialsId) {
        this.theCredentialsId = credentialsId
        this
    }

    /**
     * Changing the path and filename of the Jenkinsfile.
     *
     * @param scriptPath  new path and filename of the Jenkinsfile.
     * @return builder itself for further operations
     */
    JobDslBuilder setScriptPath(final String scriptPath) {
        this.theScriptPath = scriptPath
        this
    }

    /**
     * Changing the number of entries in the build history.
     *
     * @param history number of entries in the build history.
     * @return builder itself for further operations
     */
    JobDslBuilder setHistory(final Integer history) {
        this.theHistory = history
        this
    }

    /**
     * Create Job DSL code depending on type.
     * @return Job DSL code
     */
    String build() {
        final MAP = [:]
        MAP.put(JobDslType.MULTIBRANCH_PIPELINE) { this.buildMultiBranchPipeline() }
        MAP.put(JobDslType.PIPELINE) { this.buildPipeline() }
        MAP.get(this.theType)()
    }

    /**
     * Creating Job DSL code for a for a multibranch pipeline.
     * @return Job DSL code
     */
    private String buildMultiBranchPipeline() {
        """
        multibranchPipelineJob('${this.theName}') {
            description('${this.theDescription}')
            branchSources { git {
                remote('${this.theSource}')
                credentialsId('${this.theCredentialsId}')
            } }
            factory { workflowBranchProjectFactory { scriptPath('${this.theScriptPath}') } }
            orphanedItemStrategy { discardOldItems { numToKeep(0) }}
        }
        """
    }

    /**
     * Creating Job DSL code for a for a pipeline.
     * @return Job DSL code
     */
    private String buildPipeline() {
        """
        pipelineJob('${this.theName}') {
            description('${this.theDescription}')
            concurrentBuild(false)

            definition { cpsScm { scm {
                git {
                    branch('')
                    remote {
                        url('${this.theSource}')
                        credentials('${this.theCredentialsId}')
                    }
                }}
                scriptPath('${this.theScriptPath}')
            }}

            logRotator { numToKeep(${this.theHistory}) }
        }
        """
    }
}
