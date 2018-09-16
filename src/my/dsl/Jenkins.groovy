package my.dsl

/**
 * DSL Implementation for local use.
 */
abstract class Jenkins extends Script {
    /**
     * Print a message to the stdout.
     *
     * @param message any string message
     */
    void echo(final String message) {
        println(message)
    }

    def sh(final Map config) {
        if (config.returnStdout ?: false) {
            def result = new ShellExecutor().execute(config.script)
            result.lines.join('\n')
        } else {
            def result = new ShellExecutor().execute(config.script)
            println(result.lines.join('\n'))
        }
    }

    void writeFile(final Map config) {
        new File(config.file).text = config.text
    }

    String readFile(final Map config) {
        new File(config.file).text
    }
}
