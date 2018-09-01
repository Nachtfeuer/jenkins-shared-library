package my.tools

import static org.assertj.core.api.Assertions.assertThat
import org.junit.Test

/**
 * Testing of class {@link SourceCompare}.
 */
class SourceCompareTest {
    /** Testing of method {@link SourceCompare#countDuplicateLines(int, int)}. */
    @Test
    void testDuplicateLinesExactMatch() {
        def sc = new SourceCompare()
            .setSources(testSources.one, testSources.one)
        assertThat(sc.countDuplicateLines(0, 0)).isEqualTo(4)
        assertThat(sc.countDuplicateLines(2, 2)).isEqualTo(2)
        assertThat(sc.countDuplicateLines(0, 2)).isEqualTo(0)
    }

    /** Testing of method {@link SourceCompare#compareSources}. */
    @Test
    void testCompareSourcesWithExactMatch() {
        def results = new SourceCompare()
            .setSources(testSources.one, testSources.one)
            .setMinimumBlockSize(1)
            .compareSources()
        assertThat(results.size()).isEqualTo(1)
        assertThat(results.get(0)).isEqualTo([indices:[0, 0], blockSize:4])
    }

    /** Testing of method {@link SourceCompare#compareSources}. */
    @Test
    void testCompareSourcesWithExactMatchAndMoreThanOneDuplicate() {
        def results = new SourceCompare()
            .setSources(testSources.two, testSources.one)
            .setMinimumBlockSize(2)
            .compareSources()
        assertThat(results.size()).isEqualTo(2)
        assertThat(results.get(0)).isEqualTo([indices:[0, 0], blockSize:2])
        assertThat(results.get(1)).isEqualTo([indices:[3, 2], blockSize:2])
    }

    /** Testing of method {@link SourceCompare#compareSources}. */
    @Test
    void testCompareSourcesWithExactMatchAndMoreThanOneDuplicateInverse() {
        def results = new SourceCompare()
            .setSources(testSources.two, testSources.three)
            .setMinimumBlockSize(2)
            .compareSources()
        assertThat(results.size()).isEqualTo(2)
        assertThat(results.get(0)).isEqualTo([indices:[0, 2], blockSize:3])
        assertThat(results.get(1)).isEqualTo([indices:[3, 0], blockSize:2])
    }

    /**
     * Different tests sources to compare.
     */
    private Map getTestSources() {
        [
            one:'''green\nblue\nlight\ndark''',
            two:'''green\nblue\nred\nlight\ndark''',
            three:'''light\ndark\ngreen\nblue\nred''',
        ]
    }
}
