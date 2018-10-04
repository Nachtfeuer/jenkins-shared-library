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
        assertThat(new Version().define())
            .isEqualTo([data:[major:1, minor:0], meta:[snapshot:false]])
        assertThat(new Version().define(major:0, minor:0, patch:1))
            .isEqualTo([data:[major:0, minor:0, patch:1], meta:[snapshot:false]])
    }

    /** Testing defining of invalid version. */
    @Test
    void testDefineInvalidVersion() {
        assertThat(new Version().define(MAJOR:0, minor:0, patch:0))
            .isEqualTo(Version.INVALID_VERSION)
        assertThat(new Version().define(major:-1, minor:0, patch:0))
            .isEqualTo(Version.INVALID_VERSION)
    }

    /** Testing incrementing of valid version. */
    @Test
    void testIncrementValidVersion() {
        def version = new Version()
        assertThat(version.increment(major:version.define(major:0, minor:0)))
            .isEqualTo([data:[major:1, minor:0], meta:[snapshot:false]])
        assertThat(version.increment(minor:version.define(major:0, minor:0)))
            .isEqualTo([data:[major:0, minor:1], meta:[snapshot:false]])
    }

    /** Testing incrementing of invalid version. */
    @Test
    void testIncrementInvalidVersion() {
        def version = new Version()
        // more than two entries not allowed
        assertThat(version.increment(major:version.define(major:0), minor:version.define(minor:0)))
            .isEqualTo(Version.INVALID_VERSION)
        // key not contained in the version
        assertThat(version.increment(patch:version.define(major:0)))
            .isEqualTo(Version.INVALID_VERSION)
        // key not valid
        assertThat(version.increment(MAJOR:version.define(major:0)))
            .isEqualTo(Version.INVALID_VERSION)
        // version number not valid
        assertThat(version.increment(major:[data:[major:-1], meta:[snapshot:false]]))
            .isEqualTo(Version.INVALID_VERSION)
    }

    /** Testing of {@link Version#transpose(String, Map)}. */
    @Test
    void testValidTranspose() {
        def version = new Version()
        assertThat(Version.transpose('2', version.define(major:1)).toString())
            .isEqualTo([data:[major:2], meta:[snapshot:false]].toString())
        assertThat(Version.transpose('2.1', version.define(major:1, minor:0)).toString())
            .isEqualTo([data:[major:2, minor:1], meta:[snapshot:false]].toString())
        assertThat(Version.transpose('3.2.1', version.define(major:1, minor:0, patch:0)).toString())
            .isEqualTo([data:[major:3, minor:2, patch:1], meta:[snapshot:false]].toString())
        assertThat(Version.transpose('3.2-SNAPSHOT', version.define(major:1, minor:0)).toString())
            .isEqualTo([data:[major:3, minor:2], meta:[snapshot:true]].toString())
    }

    /** Testing of {@link Version#transpose(String, Map)}. */
    @Test
    void testInvalidTranspose() {
        def version = new Version()
        assertThat(Version.transpose('2.1', [:]))
            .isEqualTo(Version.INVALID_VERSION)
        assertThat(Version.transpose('', version.define(major:1)))
            .isEqualTo(Version.INVALID_VERSION)
        assertThat(Version.transpose('2.1', version.define(major:1)))
            .isEqualTo(Version.INVALID_VERSION)
        assertThat(Version.transpose('2', version.define(major:1, minor:0)))
            .isEqualTo(Version.INVALID_VERSION)
    }

    /** Testing of {@link Version#get(Map)} for gradle */
    @Test
    void testGetForGradle() {
        def script = new MockScript()
        def version = new Version(script)

        script.provide('version = 2')
        assertThat(version.get(gradle:version.define(major:1)).toString())
            .isEqualTo([data:[major:2], meta:[snapshot:false]].toString())
        script.provide('version = 2.1')
        assertThat(version.get(gradle:version.define(major:1, minor:0)).toString())
            .isEqualTo([data:[major:2, minor:1], meta:[snapshot:false]].toString())
    }

    /** Testing of {@link Version#get(Map)} for maven */
    @Test
    void testGetForMaven() {
        def script = new MockScript()
        def version = new Version(script)

        def pomFile = System.getProperty('user.dir') + '/test/resources/pom.default.xml'
        def text = new File(pomFile).text

        script.provide(text.replace('<version>1.0</version>', '<version>2</version>'))
        assertThat(version.get(maven:version.define(major:1)).toString())
            .isEqualTo([data:[major:2], meta:[snapshot:false]].toString())

        script.provide(text.replace('<version>1.0</version>', '<version>2.3</version>'))
        assertThat(version.get(maven:version.define(major:1, minor:0)).toString())
            .isEqualTo([data:[major:2, minor:3], meta:[snapshot:false]].toString())
    }
}
