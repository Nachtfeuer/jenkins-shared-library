package my.tools

import static org.assertj.core.api.Assertions.assertThat
import org.junit.Test

/**
 * Testing of class {@link DuplicateCodeFinder}.
 */
class DuplicateCodeFinderTest {
    @Test
    void testCheckWithDefaults() {
        def script = new MockScript()
        script.provide('aa\nbb\ncc\ndd\nss\ntt\nuu\nvv')
        script.provide('ss\ntt\nuu\nvv\ncc\ncc\ncc\ndd')

        def dcf = new DuplicateCodeFinder(script)
        dcf.sourceFiles = ['demo1.txt', 'demo2.txt']
        assertThat(dcf.check()).isEqualTo(false)
        assertThat(dcf.reportData.size()).isEqualTo(3)
    }
}
