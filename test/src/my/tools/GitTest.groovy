package my.tools

import static org.assertj.core.api.Assertions.assertThat
import org.junit.Test

/**
 * Testing of class {@link Git}.
 */
class GitTest {
    /**
     * Testing of method {@link Git#getShortCommit()}.
     */
    @Test
    void testGitShortCommit() {
        def script = new MockScript()
        script.provide('26fb9fd')
        def git = new Git(script)
        assertThat(git.shortCommit).isEqualTo('26fb9fd')
    }

    /**
     * Testing of method {@link Git#getUrl()}.
     */
    @Test
    void testGitUrl() {
        def script = new MockScript()
        script.provide('https://github.com/Nachtfeuer/jenkins-shared-library.git')
        def git = new Git(script)
        assertThat(git.url).isEqualTo(
            'https://github.com/Nachtfeuer/jenkins-shared-library.git')
    }

    /**
     * Testing of method {@link Git#getAuthorName()}.
     */
    @Test
    void testGitAuthorName() {
        def script = new MockScript()
        script.provide('Thomas Lehmann')
        def git = new Git(script)
        assertThat(git.authorName).isEqualTo('Thomas Lehmann')
    }

    /**
     * Testing of method {@link Git#getAuthorMail()}.
     */
    @Test
    void testGitAuthorMail() {
        def script = new MockScript()
        script.provide('thomas.lehmann.private@gmail.com')
        def git = new Git(script)
        assertThat(git.authorMail).isEqualTo('thomas.lehmann.private@gmail.com')
    }

    /**
     * Testing of method {@link Git#getLastTag()}.
     */
    @Test
    void testGitLastTag() {
        def script = new MockScript()
        script.provide('v0.1')
        def git = new Git(script)
        assertThat(git.lastTag).isEqualTo('v0.1')
    }

    /**
     * Testing of method {@link Git#getChancesSinceLastTag()}.
     */
    @Test
    void testGitChangesSinceLastTag() {
        def script = new MockScript()
        def git = new Git(script)

        script.provide('')
        script.provide('v1.0')
        assertThat(git.changesSinceLastTag).isEqualTo(false)

        script.provide('xxx')
        script.provide('v1.0')
        assertThat(git.changesSinceLastTag).isEqualTo(true)
    }
}
