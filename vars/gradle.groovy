import my.tools.Gradle

/**
 * Running gradle with clean and check task.
 */
void build() {
    // creating instance of Gradle Groovy wrapper for Gradle command line tool
    def api = new Gradle(this)
    api.clean().check()
}
