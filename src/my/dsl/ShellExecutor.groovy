package my.dsl

class ShellExecutor {
    def execute(final String command) {
        def result = [exitCode: 0, lines:[]]
        def process = new ProcessBuilder(['bash', '-c', command])
            .redirectErrorStream(true) 
            .start()

        process.outputStream.close()
        process.inputStream.eachLine { result.lines.add(it) }
        process.waitFor()
        result.exitCode = process.exitValue()
        return result
    }
}