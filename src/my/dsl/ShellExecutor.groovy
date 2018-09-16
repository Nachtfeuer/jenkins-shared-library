package my.dsl

/**
 * Can execute a shell command also with pipes.
 */
class ShellExecutor {
    /**
     * @param command bash command.
     * @return map with two fields: exitCode (int) and lines (list of strings)
     */
    Map execute(final String command) {
        def result = [exitCode:0, lines:[]]
        def process = new ProcessBuilder(['bash', '-c', command])
            .redirectErrorStream(true)
            .start()

        process.outputStream.close()
        process.inputStream.eachLine { result.lines.add(it) }
        process.waitFor()
        result.exitCode = process.exitValue()
        result
    }
}
