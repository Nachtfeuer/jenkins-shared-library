package my.dsl

import org.codehaus.groovy.control.CompilerConfiguration

/**
 * Can execute a Groovy script with DSL.
 */
class ScriptExecutor {
    /** data model for binding */
    private final Map model = [:]
    /** script class for DSL code */
    private final scriptBaseClass

    /**
     * Initialize script executor.
     *
     * @param scriptBaseClass optional base class for injecting DSL.
     */
    ScriptExecutor(final scriptBaseClass = null) {
        this.scriptBaseClass = scriptBaseClass
    }

    /**
     * Updating the model (binding).
     *
     * @param model a map of freely definable parameters; the fields will be variables in the script.
     * @return executor itself to allow further operations.
     */
    ScriptExecutor updateModel(final Map model) {
        this.model.putAll(model)
        this
    }

    /**
     * Running defined Groovy code.
     *
     * @param groovyCode groovy source code.
     * @return executor itself to allow further operations.
     */
    ScriptExecutor execute(final String groovyCode) {
        def config = new CompilerConfiguration()
        if (this.scriptBaseClass)  {
            config.scriptBaseClass = this.scriptBaseClass.name
        }
        def binding = new Binding(this.model)
        def shell = new GroovyShell(this.class.classLoader, binding, config)
        def script = shell.parse(groovyCode)
        script.run()
        this
    }
}
