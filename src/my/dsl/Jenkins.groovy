package my.dsl

import my.tools.Parser
import my.tools.Find
import my.tools.Git
import my.tools.Gradle
import my.tools.Renderer
import my.tools.Version
import my.tools.VirtualEnv
import my.tools.DuplicateCodeFinder

/**
 * DSL Implementation for local use.
 */
@SuppressWarnings(['AbstractClassWithoutAbstractMethod', 'Println', 'JavaIoPackageAccess'])
abstract class Jenkins extends Script {
    /** newline constant */
    private final static String NEWLINE = '\n'
    /** environment variables */
    private final Map theEnv = [PATH:System.getenv('PATH')]
    /** current working path */
    private final String theCurrentPath = System.getProperty('user.dir')

    /**
     * Provide current environment variables (read only).
     *
     * @return Provide current environment block
     * @note In opposite to Jenkins it is (here) not allowed to modify; please use <b>withEnv</b>.
     */
    Map getEnv() {
        this.theEnv.asImmutable()
    }

    /** Simulates pwd DSL in Jenkins. */
    String pwd() {
        this.theCurrentPath
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

    /**
     * Providing xgradle object.
     *
     * @return xgradle object
     */
    def getXgradle() {
        new Gradle(this)
    }

    /**
    * Render a template as a final string.
    *
    * @param template the template to render.
    * @param model is a map of data to be used in the template.
    * @return rendered template is a normal string.
    */
    String xrender(final String template, final Map model) {
        new Renderer(this).render(template, model)
    }

    /**
     * Providing xversion object.
     *
     * @return xversion object
     */
    def getXversion() {
        new Version(this)
    }

    /**
     * Provides a Python virtual environment inside the block (closure).
     *
     * @param requirements optional list of requirements (Python mododules or tools)
     * @param folderName optional folder name (default: venv)
     * @return whatever the inner block does return.
     */
    def virtualenv(final List<String> requirements=[], final String folderName='venv', final Closure body) {
        new VirtualEnv(this)
            .configure(folderName:folderName, requirements:requirements)
            .process {
            body()
        }
    }

    /**
    * Checking for duplicate code for files in the list.
    * @param files path and filenames of code that should be checked.
    * @param policies Map of policies; possible fields are listed below.
    *
    * <b>minimumBlockSize<b>: minimum number of lines that count as duplicate code (default: 4)<br/>
    * <b>ignoreCase<b>: when true the letter case will be ignored (default: false)<br/>
    * <b>ignoreWhitespaces</b>: when true then spaces and tabs are ignored (default: false)<br/>
    * <b>percentageSimilarity</b>: when 100% then exact match otherwise partial match
    *    depending on percentags (default: 100)
    */
    boolean xdup(final List<String> files, final Map policies = [:]) {
        def api = new DuplicateCodeFinder(this)
        api.with {
            sourceFiles = files
            minimumBlockSize = policies.minimumBlockSize ?: 4
            ignoreCase = policies.ignorecase ?: false
            ignoreWhitespaces = policies.ignoreWhitespaces ?: false
            percentageSimilarity = policies.percentageSimilarity ?: 100.0
            check()
        }
    }

}
