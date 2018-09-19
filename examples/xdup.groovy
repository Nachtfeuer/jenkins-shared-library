import my.dsl.Jenkins
import my.dsl.ScriptExecutor

def executor = new ScriptExecutor(Jenkins)
executor.updateModel(message:'hello')
executor.execute('''
    @Library('jenkins-shared-library@master')
    import my.tools.DuplicateCodeFinder
    import my.tools.Find

    stage('Check For Duplicates') {
        def sourceFiles = new Find(this).files('src', '*.groovy')
        sourceFiles.addAll(new Find(this).files('test', '*.groovy'))

        def dup = new DuplicateCodeFinder(this)
        dup.minimumBlockSize = 8
        dup.sourceFiles = sourceFiles
        dup.check()
    }
''')
println('done')
