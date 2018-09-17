package my.dsl

/**
 * Library main class capable of running a Groovy script with this library.
 */
@SuppressWarnings(['Println', 'JavaIoPackageAccess', 'SystemExit'])
class Runner {
    /**
     * Provides command lines options for help and for specifying a Groovy script file
     * that should be executed with this library.
     */
    static void main(arguments) {
        def cli = new CliBuilder(usage:'java -jar jenkins-shared-pipeline.jar [options]',
                                 header:'Options:')
        cli.with {
            h(longOpt:'help', 'Print this help text and exit.')
            s(longOpt:'script', args:1, argName:'file', 'Groovy script file')
        }

        def options = cli.parse(arguments)
        if (options.h) {
            cli.usage()
        } else {
            if (options.s) {
                def executor = new ScriptExecutor(Jenkins)
                executor.execute(new File(options.s).text)
            }
        }
    }
}
