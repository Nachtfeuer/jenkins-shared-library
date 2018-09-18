package my.dsl

import static org.assertj.core.api.Assertions.assertThat
import org.junit.Test

/**
 * Testing of class {@link Runner}.
 */
class RunnerTest {
    /**
     * Testing running a Groovy script with the runner.
     */
    @Test
    void testScriptParameter() {
        final FILENAME = 'test_script_parameter.groovy'
        new File(FILENAME).text = '''echo("hello")'''
        def output = new Capture().stdout {
            def arguments = new String[2]
            arguments[0] = '-s'
            arguments[1] = FILENAME
            Runner.main(arguments)
        }
        new File(FILENAME).delete()
        assertThat(new File(FILENAME).exists()).isEqualTo(false)
        assertThat(output).isEqualTo('hello\n')
    }

    /**
     * Testing help mechanism.
     */
    @Test
    void testHelpParameter() {
        def output = new Capture().stdout {
            def arguments = new String[1]
            arguments[0] = '--help'
            Runner.main(arguments)
        }
        assertThat(output).contains('usage:')
    }
}
