package my.dsl

import static org.assertj.core.api.Assertions.assertThat
import org.junit.Test

/**
 * Testing of class {@link Jenkins}.
 */
class JenkinsTest {
    /** Testing of {@link Jenkins#echo(final String)}. */
    @Test
    void testEcho() {
        def jenkins = [:] as Jenkins
        def output = new Capture().stdout {
            jenkins.echo('hello')
        }
        assertThat(output).isEqualTo('hello\n')
    }

    /** Testing of {@link Jenkins#sh(final Map)}. */
    @Test
    void testSh() {
        def jenkins = [:] as Jenkins
        assertThat(jenkins.sh(script:'echo hello', returnStdout:true)).isEqualTo('hello')
        def output = new Capture().stdout {
            assertThat(jenkins.sh(script:'echo hello')).isEqualTo(null)
        }
        assertThat(output).isEqualTo('hello\n')
    }

    /** Testing of {@link Jenkins#sh(final Map)}. */
    @Test
    void testShWithEnv() {
        def jenkins = [:] as Jenkins
        jenkins.withEnv(['MESSAGE=hello']) {
            assertThat(jenkins.sh(script:'echo \$MESSAGE', returnStdout:true)).isEqualTo('hello')
        }
    }

    /** Testing of {@link Jenkins#writeFile(final Map)}. */
    @Test
    void testWriteFile() {
        def jenkins = [:] as Jenkins
        jenkins.writeFile(file:'test_write.txt', text:'test write')
        assertThat(new File('test_write.txt').text).isEqualTo('test write')
        new File('test_write.txt').delete()
        assertThat(new File('test_write.txt').exists()).isEqualTo(false)
    }

    /** Testing of {@link Jenkins#readFile(final Map)}. */
    @Test
    void testReadFile() {
        def jenkins = [:] as Jenkins
        new File('test_read.txt').text = 'test read'
        assertThat(jenkins.readFile(file:'test_read.txt')).isEqualTo('test read')
        new File('test_read.txt').delete()
        assertThat(new File('test_read.txt').exists()).isEqualTo(false)
    }

    /**
     * Testing <b>withEnv</b> DSL behaving correctly.
     */
    @Test
    void testWithEnd() {
        def jenkins = [:] as Jenkins
        assertThat(jenkins.env).doesNotContainKey('DEMO0')
        jenkins.withEnv(['DEMO0=main']) {
            assertThat(jenkins.env).containsEntry('DEMO0', 'main')
            assertThat(jenkins.env).doesNotContainKey('DEMO1')
            assertThat(jenkins.env).doesNotContainKey('DEMO2')
            jenkins.withEnv(['DEMO1= hello ', 'DEMO2= world ']) {
                assertThat(jenkins.env).containsEntry('DEMO1', 'hello')
                assertThat(jenkins.env).containsEntry('DEMO2', 'world')
            }
            assertThat(jenkins.env).containsEntry('DEMO0', 'main')
            assertThat(jenkins.env).doesNotContainKey('DEMO1')
            assertThat(jenkins.env).doesNotContainKey('DEMO2')
        }
    }
}
