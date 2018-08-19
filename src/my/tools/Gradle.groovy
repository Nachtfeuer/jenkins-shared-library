package my.tools

/**
 * Handling of standardized gradle build with focus und Java, Groovy and relating.
 */
class Gradle extends Base {
    /**
     * Initializing gradle with Jenkinsfile script instance only.
     */
    Gradle(final script) {
        super(script)
    }

    /**
     * Running clean task using gradlew.
     * @return itself to allow further operations.
     */
    void clean() {
        this.script.sh(script:'./gradlew clean')
        this
    }

    /**
     * Running check task using gradlew.
     * @return itself to allow further operations.
     */
    void check() {
        this.script.sh(script:'./gradlew check')
        this
    }
}