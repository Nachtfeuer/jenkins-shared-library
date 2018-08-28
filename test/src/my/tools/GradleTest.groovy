package my.tools

import static org.assertj.core.api.Assertions.assertThat
import org.junit.Test

/**
 * Testing of {@link Gradle} class.
 */
class GradleTest {
    /**
     * Testing of {@link Gradle#clean()} method.
     */
    @Test
    void testClean() {
        def script = new MockScript()
        def gradle = new Gradle(script)

        assertThat(gradle.clean()).isSameAs(gradle)
        assertThat(script.calls.size()).isEqualTo(1)
        assertThat(script.calls.get(0)).isEqualTo(['sh', [script:'./gradlew clean']])
    }

    /**
     * Testing of {@link Gradle#check()} method.
     */
    @Test
    void testCheck() {
        def script = new MockScript()
        def gradle = new Gradle(script)

        assertThat(gradle.check()).isSameAs(gradle)
        assertThat(script.calls.size()).isEqualTo(1)
        assertThat(script.calls.get(0)).isEqualTo(['sh', [script:'./gradlew check']])
    }

    /**
     * Testing of {@link Gradle#publish()} method.
     */
    @Test
    void testPublish() {
        def script = new MockScript()
        def gradle = new Gradle(script)

        assertThat(gradle.publish()).isSameAs(gradle)
        assertThat(script.calls.size()).isEqualTo(4)
        assertThat(script.calls.get(0)).isEqualTo(['junit', [testResults:'**/TEST*.xml']])
        assertThat(script.calls.get(1)).isEqualTo(['jacoco'])
        assertThat(script.calls.get(2)).isEqualTo(
            ['xpublish.html', 'HTML Code Coverage', 'build/reports/coverage', 'index.html'])
        assertThat(script.calls.get(3)).isEqualTo(
            ['xpublish.html', 'HTML Pit Test Coverage', 'build/reports/pitest', 'index.html'])
    }
}
