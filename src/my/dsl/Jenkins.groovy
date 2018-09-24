package my.dsl

import my.tools.Parser
import my.tools.Find
import my.tools.Git

/**
 * DSL Implementation for local use.
 */
@SuppressWarnings(['AbstractClassWithoutAbstractMethod', 'Println', 'JavaIoPackageAccess'])
abstract class Jenkins extends Script {
    /** newline constant */
    private final static String NEWLINE = '\n'
    /** environment variables */
    private final Map theEnv = [:]

    /**
     * Provide current environment variables (read only).
     *
     * @return Provide current environment block
     * @note In opposite to Jenkins it is (here) not allowed to modify; please use <b>withEnv</b>.
     */
    Map getEnv() {
        this.theEnv.asImmutable()
    }

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
            def result = new ShellExecutor().updateEnv(this.theEnv).execute(config.script)
            result.lines.join(NEWLINE)
        } else {
            def result = new ShellExecutor().updateEnv(this.theEnv).execute(config.script)
            println(result.lines.join(NEWLINE))
            null
        }
    }

    /**
     * Define a scope (block) where the defined environment variables are available.
     *
     * @param envStrList list of strings in the form 'a=b'.
     * @param body (closure) block capability.
     * @return whatever the (closure) block does provide.
     */
    def withEnv(final List<String> envStrList, final Closure body) {
        // backup old environment variables
        def backupEnv = [:]
        backupEnv.putAll(this.theEnv)
        // adjust environment variables (overwrite and/or add)
        for (entry in envStrList) {
            def tokens = entry.tokenize('=')*.trim()
            this.theEnv.put(tokens[0], tokens[1])
        }
        // execute the (closure) block
        def result = body()
        // restore old environment variables
        this.theEnv.clear()
        this.theEnv.putAll(backupEnv)
        result
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

    /**
     * Run pipeline code in a named stage block.
     *
     * @param title title of the stage
     * @param body (closure) block capability.
     * @return whatever the (closure) block does provide.
     */
    def stage(final String title, final Closure body) {
        println("stage(${title}) {")
        def result = body()
        println('}')
        result
    }

    /**
     * Providing xparser object.
     *
     * @return xparser object providing functions like <b>xml</b>.
     */
    Map getXparser() {
        def parseXml = { final String text -> new Parser().parseXml(text) }
        [xml:parseXml]
    }

    /**
     * Providing xfind object.
     *
     * @return xfind object providing functions like <b>files</b>.
     */
    Map getXfind() {
        def findFiles = { final String path, final String name -> new Find(this).files(path, name) }
        [files:findFiles]
    }

    /**
     * Providing xpublish object. Does not publish because it's running locally.
     *
     * @return xpublish object
     */
    Map getXpublish() {
        def publishHtml = { final String title, final String path, final String mainFile='index.html' -> }
        [html:publishHtml]
    }

    /**
     * Providing xgit object.
     *
     * @return xgit object
     */
    def getXgit() {
        new Git(this)
    }
}
