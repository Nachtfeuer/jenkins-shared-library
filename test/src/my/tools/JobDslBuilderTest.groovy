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
        def libraries = [
            [name:'test1', url:'https://localhost/lib1.git', credentialsId:'cred1', defaultVersion:'1.0'],
            [name:'test2', url:'https://localhost/lib2.git', credentialsId:'cred2', defaultVersion:'2.0'],
            [name:'test3', url:'https://localhost/lib3.git']
        ]

        def dslCode = new JobDslBuilder()
            .setType(JobDslType.MULTIBRANCH_PIPELINE)
            .setName('demo')
            .setDescription('it is a demo job')
            .setSource('https://github.com/Nachtfeuer/jenkins-shared-library.git')
            .setCredentialsId('GIT_CREDENTIALS_ID')
            .setScriptPath('Jenkinsfile')
            .setLibraries(libraries)
            .build()

        assertThat(dslCode).contains("multibranchPipelineJob('demo')")
        assertThat(dslCode).contains("description('it is a demo job')")
        assertThat(dslCode).contains("credentialsId('GIT_CREDENTIALS_ID')")
        assertThat(dslCode).contains("remote('https://github.com/Nachtfeuer/jenkins-shared-library.git')")
        assertThat(dslCode).contains("scriptPath('Jenkinsfile')")
        assertThat(dslCode).contains(
            "libraryConfiguration { name('test1'); defaultVersion('1.0'); " +
            "retriever { modernSCM { scm { git { id('cred1'); remote('https://localhost/lib1.git') } } } } }")
        assertThat(dslCode).contains(
            "libraryConfiguration { name('test2'); defaultVersion('2.0'); " +
            "retriever { modernSCM { scm { git { id('cred2'); remote('https://localhost/lib2.git') } } } } }")
        assertThat(dslCode).contains(
            "libraryConfiguration { name('test3'); defaultVersion('master'); " +
            "retriever { modernSCM { scm { git { id(''); remote('https://localhost/lib3.git') } } } } }")
    }

    /**
     * Testing DSL code for a pipeline job.
     */
    @Test
    void testPipeline() {
        def dslCode = new JobDslBuilder()
            .setType(JobDslType.PIPELINE)
            .setName('demo')
            .setDescription('it is a demo job')
            .setSource('https://github.com/Nachtfeuer/jenkins-shared-library.git')
            .setCredentialsId('GIT_CREDENTIALS_ID')
            .setScriptPath('Jenkinsfile')
            .setHistory(100)
            .build()

        assertThat(dslCode).contains("pipelineJob('demo')")
        assertThat(dslCode).contains("description('it is a demo job')")
        assertThat(dslCode).contains("credentials('GIT_CREDENTIALS_ID')")
        assertThat(dslCode).contains("url('https://github.com/Nachtfeuer/jenkins-shared-library.git')")
        assertThat(dslCode).contains("scriptPath('Jenkinsfile')")
        assertThat(dslCode).contains('logRotator { numToKeep(100) }')
    }
}
