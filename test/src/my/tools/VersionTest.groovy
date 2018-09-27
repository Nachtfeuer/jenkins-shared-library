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

    /** Testing of {@link Version#transpose(String, Map)}. */
    @Test
    void testValidTranspose() {
        assertThat(Version.transpose('2', [major:1]).toString()).isEqualTo(
            [major:2].toString())
        assertThat(Version.transpose('2.1', [major:1, minor:0]).toString()).isEqualTo(
            [major:2, minor:1].toString())
        assertThat(Version.transpose('3.2.1', [major:1, minor:0, patch:0]).toString()).isEqualTo(
            [major:3, minor:2, patch:1].toString())
    }

    /** Testing of {@link Version#transpose(String, Map)}. */
    @Test
    void testInvalidTranspose() {
        assertThat(Version.transpose('2.1', [:])).isEqualTo(
            Version.INVALID_VERSION)
        assertThat(Version.transpose('', [major:1])).isEqualTo(
            Version.INVALID_VERSION)
        assertThat(Version.transpose('2.1', [major:1])).isEqualTo(
            Version.INVALID_VERSION)
        assertThat(Version.transpose('2', [major:1, minor:0])).isEqualTo(
            Version.INVALID_VERSION)
    }

    @Test
    void testGetForGradle() {
        def script = new MockScript()
        def version = new Version(script)

        script.provide('version = 2')
        assertThat(version.get(gradle:[major:1]).toString()).isEqualTo(
            [major:2].toString())
        script.provide('version = 2.1')
        assertThat(version.get(gradle:[major:1, minor:0]).toString()).isEqualTo(
            [major:2, minor:1].toString())
    }

    @Test
    void testGetForMaven() {
        def script = new MockScript()
        def version = new Version(script)

        def pomFile = System.getProperty('user.dir') + '/test/resources/pom.default.xml'
        def text = new File(pomFile).text

        script.provide(text.replace('<version>1.0</version>', '<version>2</version>'))
        assertThat(version.get(maven:[major:1]).toString()).isEqualTo(
            [major:2].toString())
        script.provide(text.replace('<version>1.0</version>', '<version>2.3</version>'))
        assertThat(version.get(maven:[major:1, minor:0]).toString()).isEqualTo(
            [major:2, minor:3].toString())
    }
}
