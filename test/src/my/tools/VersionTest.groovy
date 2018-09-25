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
        assertThat(new Version().define(MAJOR:0, minor:0, patch:0)).isEqualTo([:])
        assertThat(new Version().define(major:-1, minor:0, patch:0)).isEqualTo([:])
    }

    /** Testing incrementing of valid version. */
    @Test
    void testIncrementValidVersion() {
        assertThat(new Version().increment(major:[major:0, minor:0])).isEqualTo([major:1, minor:0])
        assertThat(new Version().increment(minor:[major:0, minor:0])).isEqualTo([major:0, minor:1])
    }

    /** Testing incrementing of invalid version. */
    @Test
    void testIncrementInvalidVersion() {
        // more than two entries not allowed
        assertThat(new Version().increment(major:[major:0], minor:[minor:0])).isEqualTo([:])
        // key not contained in the version
        assertThat(new Version().increment(patch:[major:0])).isEqualTo([:])
        // key not valid
        assertThat(new Version().increment(MAJOR:[major:0])).isEqualTo([:])
        // version number not valid
        assertThat(new Version().increment(major:[major:-1])).isEqualTo([:])
    }
}
