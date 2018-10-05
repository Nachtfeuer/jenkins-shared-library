import my.tools.Version

/**
 * Defines versions that will validated or take the default one.
 *
 * @return validated version when specified, null when fail or
 *         defaulted to 1.0 (major.minor) when not specified.
 */
Map define(final Map config = [:]) {
    new Version(this).define(config)
}

/**
 * Increment a version part.
 *
 * @param config contain the version part as key and current version as value only.
 * @return modified version when valid key and value otherwise INVALID_VERSION.
 */
Map increment(final Map config) {
    new Version(this).increment(config)
}

/**
 * Get current version depending on tool (maven, gradle, tag).
 *
 * @param config key is the tool and value the defined version (policy).
 * @return current version if found otherwise INVALID_VERSION.
 */
Map get(final Map config) {
    new Version(this).get(config)
}
