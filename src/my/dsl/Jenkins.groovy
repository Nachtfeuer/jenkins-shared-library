package my.dsl

/**
 * DSL Implementation for local use.
 */
@SuppressWarnings(['AbstractClassWithoutAbstractMethod', 'Println', 'JavaIoPackageAccess'])
abstract class Jenkins extends Script {
    /** newline constant */
    private final static String NEWLINE = '\n'

    /**
     * Print a message to the stdout.
     *
     * @param message any string message
     */
    void echo(final String message) {
        println(message)
    }

    /**
     * Executing a shell command.
     *
     * @param config a map with followng fields: <b>script</b> a string representing the command.
     *        <b>returnStdout</b> an optional boolean indicating that the output is returned;
     *        the default is "false". <b>returnStatus</b> an optional boolean field indicating
     *        that the exit code for the shell command is returned.
     * @return depends on parameters: null if neither exit code nor output is wanted otherwise
     *         an int or a string.
     */
    def sh(final Map config) {
        if (config.returnStdout ?: false) {
            def result = new ShellExecutor().execute(config.script)
            result.lines.join(NEWLINE)
        } else {
            def result = new ShellExecutor().execute(config.script)
            println(result.lines.join(NEWLINE))
            null
        }
    }

    /**
     * Writing text to a file.
     *
     * @param config a map with following fields: <b>file</b> path and filename of file,
     *        <b>text</b> the text to write into that file.
     */
    void writeFile(final Map config) {
        new File(config.file).text = config.text
    }

    /**
     * Reading text from a file.
     *
     * @param config a map with following fields: <b>file</b> path and filename of file.
     * @return file content as string.
     */
    String readFile(final Map config) {
        new File(config.file).text
    }
}
