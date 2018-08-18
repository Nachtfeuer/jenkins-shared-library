package my.tools

import static org.assertj.core.api.Assertions.assertThat
import org.junit.Test

/**
 * Testing of {@link Gradle} class.
 */
class GradleTest {
    /**
     * Testing of {@link Gradle#clean} method.
     */
    @Test
    void testClean() {
        def script = new MockScript()
        def gradle = new Gradle(script)

        gradle.clean()
        assertThat(script.calls.size()).isEqualTo(1)
        assertThat(script.calls.get(0)).isEqualTo(['sh', [script:'./gradlew clean']])
    }

    /**
     * Testing of {@link Gradle#check} method.
     */
    @Test
    void testCheck() {
        def script = new MockScript()
        def gradle = new Gradle(script)

        gradle.check()
        assertThat(script.calls.size()).isEqualTo(1)
        assertThat(script.calls.get(0)).isEqualTo(['sh', [script:'./gradlew check']])
    }
}
