package my.tools

import static org.assertj.core.api.Assertions.assertThat

import org.junit.Test
import org.junit.runner.RunWith

import junitparams.JUnitParamsRunner
import junitparams.Parameters

import org.yaml.snakeyaml.Yaml

/**
 * Testing of method {@link SourceCompare#compareSources}.
 */
@RunWith(JUnitParamsRunner)
class DuplicateCodeTest {
    /** newline character requires for splitting sources */
    private final static String NEWLINE = '\n'

    /**
     * Testing of method {@link SourceCompare#compareSources} for different setups.
     */
    @Test
    @Parameters
    void testCompareSources(final Map config) {
        List<List<String>> sources =  [
            (List)config.sources[0].split(NEWLINE),
            (List)config.sources[1].split(NEWLINE)
        ]

        def expectedResults = config.expectedResults*.asImmutable()
        expectedResults = expectedResults.asImmutable()

        def results = new SourceCompare()
            .setSources(sources[0], sources[1])
            .setMinimumBlockSize(config.minimumBlockSize ?: 4)
            .setIgnoreCase(config.ignoreCase ?: false)
            .setIgnoreWhitespaces(config.ignoreWhitespaces ?: false)
            .setPercentageSimilarity(config.percentageSimilarity ?: 100.0)
            .compareSources()
        assertThat(results.toString()).isEqualTo(expectedResults.toString())
    }

    /**
     * @return list of different test setups.
     */
    @SuppressWarnings('UnusedPrivateMethod')
    private List parametersForTestCompareSources() {
        def testDataFile = System.getProperty('user.dir') + '/test/resources/duplicate-code-test.yml'
        new Yaml().load(new File(testDataFile).text).parameters
    }
}
