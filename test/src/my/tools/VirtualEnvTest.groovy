package my.tools

import static org.assertj.core.api.Assertions.assertThat
import org.junit.Test

/**
 * Testing of class {@link VirtualEnv}.
 */
class VirtualEnvTest {
    /**
     * Testing of {@link VirtualEnv#configure(Map)}.
     */
    @Test
    void testValidConfigure() {
        def script = new MockScript()
        def api = new VirtualEnv(script)

        assertThat(api.configure(folderName:'myenv')).isSameAs(api)
        assertThat(api.configure(requirements:['wheel==0.29.0'])).isSameAs(api)

        assertThat(api.folderName).isEqualTo('myenv')
        assertThat(api.requirements).isEqualTo(['wheel==0.29.0'])
    }

    /**
     * Testing of {@link VirtualEnv#configure(Map)}.
     */
    @Test
    void testInValidConfigure() {
        def script = new MockScript()
        def api = new VirtualEnv(script)

        api.configure(unknownParameter:'myenv') // containsKey fails
        api.configure(folderName:1234)          // is String fails
        api.configure(folderName:'')            // isEmpty fails
        assertThat(api.folderName).isEqualTo('venv')
    }

    /**
     * Testing {@link VirtualEnv#process()} method.
     */
    @Test
    void testProcess() {
        def script = new MockScript()
        def api = new VirtualEnv(script)
        def result = api.process {
            assertThat(script.env).containsKey('VIRTUAL_ENV')
            assertThat(script.env.PATH).contains(script.env.VIRTUAL_ENV + '/bin')
            42
        }

        assertThat(result).isEqualTo(42)
        assertThat(script.calls.size()).isEqualTo(4)
        assertThat(script.calls.get(0).get(0)).isEqualTo('pwd')
        assertThat(script.env).doesNotContainKey('VIRTUAL_ENV')
        assertThat(script.env.PATH).doesNotContain(script.env.VIRTUAL_ENV + '/bin')
        assertThat(script.calls.get(1).get(0)).isEqualTo('withEnv')
        assertThat(script.calls.get(2)).isEqualTo(['sh', [script:'virtualenv venv']])
        assertThat(script.calls.get(3)).isEqualTo(['sh', [script:'rm -rf venv']])
    }

    /**
     * Testing {@link VirtualEnv#process()} method.
     */
    @Test
    void testProcessWithRequirements() {
        def requirements = ['tox==2.9.1', 'wheel==0.29.0']
        def script = new MockScript()
        def api = new VirtualEnv(script)
        api.configure(requirements:requirements).process { 42 }

        assertThat(script.calls.get(3).toString()).isEqualTo(
            ['sh', [script:'python -m pip install tox==2.9.1']].toString())
        assertThat(script.calls.get(4).toString()).isEqualTo(
            ['sh', [script:'python -m pip install wheel==0.29.0']].toString())
    }

    /**
     * Testing with error that the pipeline state is set to failure and the
     * Python virtual environment is finally removed.
     */
    @Test
    @SuppressWarnings('ThrowRuntimeException')
    void testProcessWithError() {
        def script = new MockScript()
        def api = new VirtualEnv(script)
        def result = api.process { throw new RuntimeException('some error') }

        assertThat(result).isEqualTo(null)
        assertThat(script.currentBuild.result).isEqualTo('FAILURE')
        assertThat(script.calls.get(3)).isEqualTo(['sh', [script:'rm -rf venv']])
    }
}
