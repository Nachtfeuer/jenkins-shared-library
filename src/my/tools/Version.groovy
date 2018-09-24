package my.tools

/**
 * Versioning handling.
 */
class Version extends Base {
    /**
     * Initialize with Jenkinsfile script instance.
     *
     * @param script Jenkinsfile script instance.
     */
    Version(final script) {
        super(script)
    }

    /**
     * Defines versions that will validated or take the default one.
     *
     * @return validated version when specified, null when fail or
     *         defaulted to 1.0 (major.minor) when not specified.
     */
    Map define(final Map config = [:]) {
        def version = [major:1, minor:0]
        if (config.size() > 0) {
            if (config.every { Version.isValidKey(it.key) && Version.isValidValue(it.value) }) {
                version = config
            } else {
                version = null
            }
        }
        version
    }

    /**
     * @return true when the key is lowercase.
     */
    private static boolean isValidKey(final String key) {
        key.chars.every { it.isLowerCase() }
    }

    /**
     * @return true when the value is none negative number.
     */
    private static boolean isValidValue(final Integer value) {
        value >= 0
    }
}
