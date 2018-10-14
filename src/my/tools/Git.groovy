package my.tools

/**
 * Simple wrapper for command line tool git.
 */
class Git extends Base {
    /** git short commit for current repository. */
    public final static String SHORT_COMMIT_CMD = 'git rev-parse --short HEAD'
    /** git url */
    public final static String URL_CMD = 'git config --get remote.origin.url'
    /** git author name of last commit */
    public final static String AUTHOR_NAME_CMD = 'git show -s --format="%an"'
    /** git author mail of last commit */
    public final static String AUTHOR_MAIL_CMD = 'git show -s --format="%ae"'
    /** git get last tag */
    public final static String LAST_TAG_CMD = 'git describe --tags --abbrev=0'

    /**
     * Initializing with Jenkinsfile script instance only.
     */
    Git(final script) {
        super(script)
    }

    /**
     * Get git short commit for current repository.
     * @return git short commit.
     */
    String getShortCommit() {
        this.script.sh(script:SHORT_COMMIT_CMD, returnStdout:true)
    }

    /**
     * Get git url
     * @return git url.
     */
    String getUrl() {
        this.script.sh(script:URL_CMD, returnStdout:true)
    }

    /**
     * Get last commit author name.
     * @return author name.
     */
    String getAuthorName() {
        this.script.sh(script:AUTHOR_NAME_CMD, returnStdout:true)
    }

    /**
     * Get last commit author mail.
     * @return author mail.
     */
    String getAuthorMail() {
        this.script.sh(script:AUTHOR_MAIL_CMD, returnStdout:true)
    }

    /**
     * Get last tag (if available).
     * @return last tag otherwise an empty string.
     */
    String getLastTag() {
        this.script.sh(script:LAST_TAG_CMD, returnStdout:true)
    }

    /**
     * Checking for changes since last tag
     * @return true when a change has been detected otherwise false
     * @note it works well only if you have tags.
     */
    boolean getChangesSinceLastTag() {
        try {
            def tag = this.lastTag
            !this.script.sh(script:"git diff $tag", returnStdout:true).trim().isEmpty()
        } catch (err) {
            this.script.echo('Git.getChangesSinceLastTag() :: Failed to detect changes (err.message)')
            false
        }
    }
}
