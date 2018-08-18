import my.tools.Gradle

/**
 * Running gradle with clean task.
 */
Gradle call() {
    // creating instance of Gradle Groovy wrapper for Gradle command line tool
    return new Gradle(this)
}
