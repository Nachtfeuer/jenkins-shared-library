package my.tools

/**
 * Base of all tool classes.
 */
class Base implements Serializable {
    /** Jenkinsfile script instance */
    protected final script

    /**
     * Initialize base with Jenkinsfile script instance
     */
    Base(final script) {
        this.script = script
    }
}
