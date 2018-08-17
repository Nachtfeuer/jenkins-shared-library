package my.tools

import static org.assertj.core.api.Assertions.assertThat
import org.junit.Test

/**
 * Testing of {@link Gradle} class.
 */
class GradleTest {
    /**
     * Testing of {@link Gradle#check} method.
     */
    @Test
    void testCheck() {
        def script = new MockScript()
        def gradle = new Gradle(script)

        gradle.check()
        assertThat(script.calls.size()).isEqualTo(1)
    }
}
