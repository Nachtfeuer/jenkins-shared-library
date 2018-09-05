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
        final List<String> SOURCE = '''green\nblue\nlight\ndark'''.split('\n')
        def sc = new SourceCompare()
            .setSources(SOURCE, SOURCE)
        assertThat(sc.countDuplicateLines(0, 0)).isEqualTo(4)
        assertThat(sc.countDuplicateLines(2, 2)).isEqualTo(2)
        assertThat(sc.countDuplicateLines(0, 2)).isEqualTo(0)
    }

    /** Testing of method {@link SourceCompare#setPercentageSimlarity(double)}. */
    @Test
    void testPercentageSimilarityPolicies() {
        def sc = new SourceCompare()

        sc.percentageSimilarity = -1.0
        assertThat(sc.policies.percentageSimilarity).isEqualTo((double)100.0)
        sc.percentageSimilarity = 101.0
        assertThat(sc.policies.percentageSimilarity).isEqualTo((double)100.0)
        sc.percentageSimilarity = 50.0
        assertThat(sc.policies.percentageSimilarity).isEqualTo((double)50.0)
        sc.percentageSimilarity = 0.0
        assertThat(sc.policies.percentageSimilarity).isEqualTo((double)0.0)
    }
}
