package my.tools

import static org.assertj.core.api.Assertions.assertThat
import org.junit.Test

/**
 * Testing of {@link JobDslConverter} class.
 */
class JobDslConverterTest {
    /**
     * Testing to read a JSON from file to convert it
     * into a Job DSL code for a multibranch pipeline.
     */
    @Test
    void testConvertFromJsonMultiBranch() {
        def script = new MockScript()
        script.provide(JobDslConverterTest.defaultJobDefinition())

        def dslCode = new JobDslConverter(script).fromJson('data/multibranch.json')

        assertThat(dslCode).contains("multibranchPipelineJob('demo')")
        assertThat(dslCode).contains("description('it is a demo job')")
        assertThat(dslCode).contains("credentialsId('GIT_CREDENTIALS_ID')")
        assertThat(dslCode).contains("remote('https://github.com/Nachtfeuer/jenkins-shared-library.git')")
        assertThat(dslCode).contains("scriptPath('Jenkinsfile')")
    }

    /**
     * Testing to read a JSON from file to convert it
     * into a Job DSL code for a multibranch pipeline.
     */
    @Test
    void testConvertFromYamlMultiBranch() {
        def script = new MockScript()
        script.provide(JobDslConverterTest.defaultJobDefinition())

        def dslCode = new JobDslConverter(script).fromYaml('data/multibranch.yaml')

        assertThat(dslCode).contains("multibranchPipelineJob('demo')")
        assertThat(dslCode).contains("description('it is a demo job')")
        assertThat(dslCode).contains("credentialsId('GIT_CREDENTIALS_ID')")
        assertThat(dslCode).contains("remote('https://github.com/Nachtfeuer/jenkins-shared-library.git')")
        assertThat(dslCode).contains("scriptPath('Jenkinsfile')")
    }

    /**
     * Map representing a default job definition for the tests.
     * @return Map of job related fields.
     */
    private static Map defaultJobDefinition() {
        [
            type:'MULTIBRANCH_PIPELINE',
            name:'demo',
            description:'it is a demo job',
            credentialsId:'GIT_CREDENTIALS_ID',
            source:'https://github.com/Nachtfeuer/jenkins-shared-library.git',
            script:'Jenkinsfile',
        ]
    }
}
