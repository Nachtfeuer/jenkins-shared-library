package my.tools

import static org.assertj.core.api.Assertions.assertThat
import org.junit.Test

/**
 * Testing of class {@link Version}.
 */
class VersionTest {
    /** Testing defining of valid version. */
    @Test
    void testDefineValidVersion() {
        assertThat(new Version().define()).isEqualTo([major:1, minor:0])
        assertThat(new Version().define(major:0, minor:0, patch:1)).isEqualTo(
            [major:0, minor:0, patch:1])
    }

    /** Testing defining of invalid version. */
    @Test
    void testDefineInvalidVersion() {
        assertThat(new Version().define(MAJOR:0, minor:0, patch:0)).isEqualTo(null)
        assertThat(new Version().define(major:-1, minor:0, patch:0)).isEqualTo(null)
    }
}
