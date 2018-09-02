package my.tools

import static org.assertj.core.api.Assertions.assertThat

import org.junit.Test
import org.junit.runner.RunWith

import junitparams.JUnitParamsRunner
import junitparams.Parameters

/**
 * Testing of method {@link SourceCompare#compareSources}.
 */
@RunWith(JUnitParamsRunner)
class CompareSourcesTest {
    /**
     * Testing of method {@link SourceCompare#compareSources} for different setups.
     */
    @Test
    @Parameters
    void testCompareSources(final Map config) {
        def results = new SourceCompare()
            .setSources(config.sources[0], config.sources[1])
            .setMinimumBlockSize(config.minimumBlockSize ?: 4)
            .setIgnoreCase(config.ignoreCase ?: false)
            .setIgnoreWhitespaces(config.ignoreWhitespaces ?: false)
            .compareSources()
        assertThat(results.toString()).isEqualTo(config.expectedResults.toString())
    }

    /**
     * @return list of different test setups.
     */
    @SuppressWarnings('UnusedPrivateMethod')
    private parametersForTestCompareSources() {
        [[
            sources:['green\nblue\nlight\ndark', 'light\ndark\ngreen\nblue'],
            minimumBlockSize:4,
            expectedResults:[]
        ], [
            sources:['green\nblue\nlight\ndark', 'light\ndark\ngreen\nblue'],
            minimumBlockSize:2,
            expectedResults:[
                [indices:[0, 2], blockSize:2], [indices:[2, 0], blockSize:2]]
        ], [
            sources:['green\nblue\nlight\ndark', 'light\ndark\ngreen\nblue\ngreen\nblue'],
            minimumBlockSize:2,
            expectedResults:[
                [indices:[0, 2], blockSize:2], [indices:[0, 4], blockSize:2], [indices:[2, 0], blockSize:2]]
        ], [
            sources:['green\nblue\nlight\ndark', 'Light\nDark\nGreen\nBlue'],
            minimumBlockSize:2,
            ignoreCase:true,
            expectedResults:[
                [indices:[0, 2], blockSize:2], [indices:[2, 0], blockSize:2]]
        ], [
            sources:['green\nblue\nlight\ndark', ' Light \n  \t Dark \n   Green \t \n  Blue  '],
            minimumBlockSize:2,
            ignoreCase:true,
            ignoreWhitespaces:true,
            expectedResults:[
                [indices:[0, 2], blockSize:2], [indices:[2, 0], blockSize:2]]
        ]]
    }
}
