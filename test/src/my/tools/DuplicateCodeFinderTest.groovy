package my.tools

import static org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith

import junitparams.JUnitParamsRunner
import junitparams.Parameters

/**
 * Testing of class {@link DuplicateCodeFinder}.
 */
@RunWith(JUnitParamsRunner)
class DuplicateCodeFinderTest {
    @Test
    @Parameters
    void testDcf(final Map config) {
        def script = new MockScript()
        config.sources.each { script.provide(it.code) }

        def dcf = new DuplicateCodeFinder(script)
        dcf.sourceFiles = config.sources*.name
        dcf.minimumBlockSize = config.policies.minimumBlockSize
        dcf.ignoreCase = config.policies.ignoreCase
        dcf.ignoreWhitespaces = config.policies.ignoreWhitespaces
        dcf.percentageSimilarity = config.policies.percentageSimilarity
        assertThat(dcf.check()).isEqualTo(false)
        def reportData = dcf.reportData
        assertThat(reportData.size()).isEqualTo(config.expectedResults.size())
        config.expectedResults.eachWithIndex {
            expected, resultIdx ->
                assertThat(reportData.get(resultIdx).sources).isEqualTo(expected.sources)
                assertThat(reportData.get(resultIdx).results).isEqualTo(
                    expected.results*.asImmutable())
        }
    }

    @SuppressWarnings('UnusedPrivateMethod')
    private List parametersForTestDcf() {
        [[
            sources:[
                [name:'demo1.txt', code:'aa\nbb\ncc\ndd\nss\ntt\nuu\nvv'],
                [name:'demo2.txt', code:'ss\ntt\nuu\nvv\naa\nbb\ncc\ndd']
            ],
            policies:[
                minimumBlockSize:4, ignoreCase:false, ignoreWhitespaces:false, percentageSimilarity:100.0
            ],
            expectedResults:[[
                sources:[pathAndFileName1:'demo1.txt', pathAndFileName2:'demo2.txt' ],
                results:[[indices:[0, 4], blockSize:4], [indices:[4, 0], blockSize:4]]
            ]]
        ]]
    }
}
