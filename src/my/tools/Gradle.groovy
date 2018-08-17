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
     * Running clean and check goal using gradlew.
     */
    void check() {
        this.script.sh('./gradlew clean check')
    }
}
