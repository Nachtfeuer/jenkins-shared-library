package my.dsl

import org.codehaus.groovy.control.CompilerConfiguration
import groovy.lang.Binding
import groovy.lang.GroovyShell

class ScriptExecutor {
    /** data model for binding */
    private final Map model = [:]
    /** script class for DSL code */
    private final scriptBaseClass

    ScriptExecutor(final scriptBaseClass = null) {
        this.scriptBaseClass = scriptBaseClass
    }

    ScriptExecutor updateModel(final Map model) {
        this.model.putAll(model)
        this
    }

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
