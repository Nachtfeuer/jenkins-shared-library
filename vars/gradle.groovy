import my.tool.Gradle

// creating instance of Gradle Groovy wrapper for Gradle command line tool
def api = new Gradle(this)

/**
 * Running gradle with clean task.
 */
def clean() {
    return api.clean()
}

/**
 * Running gradle with check task.
 */
def check() {
    return api.check()
}
