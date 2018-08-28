package my.tools

import static org.assertj.core.api.Assertions.assertThat
import org.junit.Test

/**
 * Testing of {@link Find} class.
 */
class FindTest {
    /**
     * Testing of method {@link Find#files(String, String)}.
     */
    @Test
    void testFindFiles() {
        def script = new MockScript()
        script.provide('demo1.groovy\ndemo2.groovy')

        def files = new Find(script).files('.', '*.groovy')
        assertThat(script.calls.size()).isEqualTo(1)
        assertThat(script.calls.get(0).toString()).isEqualTo(
            ['sh', [script:'find . -type f -name "*.groovy"', returnStdout:true]].toString())
        assertThat(files).isEqualTo(['demo1.groovy', 'demo2.groovy'])
    }
}
