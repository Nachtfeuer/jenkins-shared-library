package my.tools

import static org.assertj.core.api.Assertions.assertThat
import org.junit.Test

/**
 * Testing of {@link JobDslBuilder} class.
 */
class JobDslBuilderTest {
    /**
     * Testing DSL code for a multibranch pipeline job.
     */
    @Test
    void testMultiBranchPipeline() {
        def dslCode = new JobDslBuilder()
            .setType(JobDslType.MULTIBRANCH_PIPELINE)
            .setName('demo')
            .setDescription('it is a demo job')
            .setSource('https://github.com/Nachtfeuer/jenkins-shared-library.git')
            .setCredentialsId('GIT_CREDENTIALS_ID')
            .setScriptPath('Jenkinsfile')
            .build()

        assertThat(dslCode).contains("multibranchPipelineJob('demo')")
        assertThat(dslCode).contains("description('it is a demo job')")
        assertThat(dslCode).contains("credentialsId('GIT_CREDENTIALS_ID')")
        assertThat(dslCode).contains("remote('https://github.com/Nachtfeuer/jenkins-shared-library.git')")
        assertThat(dslCode).contains("scriptPath('Jenkinsfile')")
    }
}
