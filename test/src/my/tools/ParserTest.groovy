package my.tools

import static org.assertj.core.api.Assertions.assertThat
import org.junit.Test

/**
 * Testing of class {@link Parser}.
 */
class ParserTest {
    /** Testing of {@link Parser.parseXml}. */
    @Test
    void testParseXml() {
        def text = '<project></project>'
        assertThat(new Parser().parseXml(text)).isEqualTo([:])
        text = '<project><version>1.0</version></project>'
        assertThat(new Parser().parseXml(text)).isEqualTo([version:'1.0'])

        def pomFile  = System.getProperty('user.dir') + '/test/resources/pom.default.xml'
        text = new File(pomFile).text
        assertThat(new Parser().parseXml(text)).isEqualTo(
            [modelVersion:'4.0.0', groupId:'my-group-id', artifactId:'my-artifact-id', version:'1.0'])
    }
}
