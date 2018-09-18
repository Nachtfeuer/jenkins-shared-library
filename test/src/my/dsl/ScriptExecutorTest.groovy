package my.dsl

import static org.assertj.core.api.Assertions.assertThat
import org.junit.Test

/**
 * Testing of class {@link ScriptExecutor}.
 */
class ScriptExecutorTest {
    @Test
    void testSimple() {
        def output = new Capture().stdout {
            new ScriptExecutor().execute('''println('hello')''')
        }
        assertThat(output).isEqualTo('hello\n')
    }

    @Test
    void testModel() {
        def output = new Capture().stdout {
            new ScriptExecutor()
                .updateModel(message:'hello')
                .execute('''println(message)''')
        }
        assertThat(output).isEqualTo('hello\n')
    }

    @Test
    void testJenkinsDsl() {
        def output = new Capture().stdout {
            new ScriptExecutor(Jenkins)
                .execute('''echo("hello")''')
        }
        assertThat(output).isEqualTo('hello\n')
    }
}
