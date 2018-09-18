package my.dsl

/**
 * Can execute a shell command also with pipes.
 */
class ShellExecutor {
    /** additional environment variables */
    private final Map<String, String> theEnv = [:]

    /**
     * Add environment variables that should be available for the shell.
     *
     * @param env environment variables to make available for the shell.
     * @return executor itself to allow further processing.
     */
    ShellExecutor updateEnv(final Map<String, String> env) {
        this.theEnv.putAll(env)
        this
    }
    /**
     * @param command bash command.
     * @return map with two fields: exitCode (int) and lines (list of strings)
     */
    Map execute(final String command) {
        def result = [exitCode:0, lines:[]]
        def builder = new ProcessBuilder(['bash', '-c', command])
            .redirectErrorStream(true)
        builder.environment().putAll(this.theEnv)
        def process = builder.start()

        process.outputStream.close()
        process.inputStream.eachLine { result.lines.add(it) }
        process.waitFor()
        result.exitCode = process.exitValue()
        result
    }
}
