package my.tools

import static org.assertj.core.api.Assertions.assertThat
import org.junit.Test

/**
 * Testing of class {@link Renderer}.
 */
class JobDslTypeTest {
    @Test
    void testValues() {
        assertThat(JobDslType.values().size()).isEqualTo(2)
        assertThat(JobDslType.values()).contains(JobDslType.PIPELINE)
        assertThat(JobDslType.values()).contains(JobDslType.MULTIBRANCH_PIPELINE)
    }

    @Test
    void testValueOf() {
        for (entry in JobDslType.values()) {
            assertThat(JobDslType.valueOf(entry.name())).isEqualTo(entry)
        }
    }
}
