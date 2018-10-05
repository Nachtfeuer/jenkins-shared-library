package my.tools

/**
 * Versioning handling.
 */
class Version extends Base {
    /** Indicator or "invaid version" */
    public final static Map INVALID_VERSION = [:].asImmutable()

    /** Version extension indicating a snapshot version */
    private final static String SNAPSHOT = '-SNAPSHOT'

    /**
     * Initialize with Jenkinsfile script instance.
     *
     * @param script Jenkinsfile script instance.
     */
    Version(final script) {
        super(script)
    }

    /**
     * Transpose of string version into a version map.
     *
     * @param strVersion a version like '1.2.3'.
     * @param version a version like [major:1, minor:0, patch:0].
     * @return transposed version when possible or INVALID_VERSION when failed.
     * @note number of entries have to be same for tokenized strVersion and the version
     */
    static Map transpose(final String strVersion, final Map version) {
        def transposedVersion = Version.INVALID_VERSION
        if (version.data && version.data.size() > 0) {
            def isSnapshot = strVersion.contains(Version.SNAPSHOT)
            def tokenizedVersion = strVersion.replace(Version.SNAPSHOT, '').tokenize('.')
            def newVersion = [version.data.keySet().toList(), tokenizedVersion]
                .transpose().collectEntries { it }
            if (newVersion.size() == version.data.size() && tokenizedVersion.size() == version.data.size()) {
                transposedVersion = [data:newVersion, meta:version.meta]
                transposedVersion.meta.snapshot = isSnapshot
            }
        }
        transposedVersion
    }

    /**
     * Defines versions that will validated or take the default one.
     *
     * @return validated version when specified, null when fail or
     *         defaulted to 1.0 (major.minor) when not specified.
     */
    Map define(final Map config = [:]) {
        def version = [data:[major:1, minor:0], meta:[snapshot:false, prefix:'v']]
        if (config.size() > 0) {
            if (config.every { Version.isValidKey(it.key) && Version.isValidValue(it.value) }) {
                version.data = config
            } else {
                version = Version.INVALID_VERSION
            }
        }
        version
    }

    /**
     * Increment a version part.
     *
     * @param config contain the version part as key and current version as value only.
     * @return modified version when valid key and value otherwise INVALID_VERSION.
     */
    Map increment(final Map config) {
        def modifiedVersion = Version.INVALID_VERSION
        if (config?.size() == 1) {
            def key = config*.key[0]
            def version = config*.value[0]
            if (Version.isValidKey(key) && version.data && version.data.containsKey(key)) {
                def value = version.data.get(key)
                if (Version.isValidValue(value)) {
                    version.data.put(key, value + 1)
                    modifiedVersion = version
                }
            }
        }
        modifiedVersion
    }

    /**
     * Get current version depending on tool (maven, gradle, tag).
     *
     * @param config key is the tool and value the defined version (policy).
     * @return current version if found otherwise INVALID_VERSION.
     */
    Map get(final Map config) {
        def version = Version.INVALID_VERSION
        if (config?.size() == 1) {
            def key = config*.key[0]
            version = config*.value[0]

            switch (key) {
                case 'gradle':
                    def content = this.script.readFile(file:'build.gradle')
                    def match = content =~ /(?m)^version[ ]*=[ ]*(.*)/
                    version = Version.transpose(match[0][1], version)
                    break
                case 'maven':
                    def content = this.script.readFile(file:'pom.xml')
                    def model = new Parser().parseXml(content)
                    version = Version.transpose(model.version, version)
                    break
                case 'tag':
                    def content = new Git(this.script).lastTag
                    if (!version.meta.prefix.isEmpty() && content.startsWith(version.meta.prefix)) {
                        content = content[version.meta.prefix.size()..content.size() - 1]
                    }
                    version = Version.transpose(content, version)
                    break
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
