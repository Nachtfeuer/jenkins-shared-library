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
                version = [:]
            }
        }
        version
    }

    /**
     * Increment a version part.
     *
     * @param config contain the version part as key and current version as value only.
     * @return modified version when valid key and value otherwise null.
     */
    Map increment(final Map config) {
        def modifiedVersion = [:]
        if (config?.size() == 1) {
            def key = config*.key[0]
            def version = config*.value[0]

            if (Version.isValidKey(key) && version.containsKey(key)) {
                def value = version.get(key)
                if (Version.isValidValue(value)) {
                    version.put(key, value + 1)
                    modifiedVersion = version
                }
            }
        }
        modifiedVersion
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
