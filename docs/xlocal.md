# Use of the library without Jenkins

[TOC]

## Introduction

Writing Jenkins DSL code usually also means to write unittests and
to mock things because you don't have the Jenkins and its
plugins as part of your build process (and you also should not have it).

However there are usecases where it would be awesome when you were
able to use your own library code locally (still: without Jenkins):

 - testing your library code that it does work as expected (without mocks).
 - using your library as a tool to run a Jenkinsfile without Jenkins.

The first use case is more simple since you - usually - don't require too
many DSL functions that have to be implemented before you can test one or
two of your classes. For the second use case you require quite some DSL
because you have to implement all DSL you see in a Jenkinsfile and the
few DSL functions used in the classes. Giving you an idea see next section ...

## Simple scenario

Consider the duplicate code DSL and the related classes in this library.
That library code does required **sh**, **readFile** and **writeFile** as DSL functions.
The class `my.dsl.Jenkinks` does implement all this DSL functions for local use
while the class `my.dsl.ScriptExecutor` is capable of running any Groovy code:

```groovy
import my.dsl.Jenkins
import my.dsl.ScriptExecutor

def executor = new ScriptExecutor(Jenkins)
executor.execute('''
    import my.tools.DuplicateCodeFinder
    import my.tools.Find

    def sourceFiles = new Find(this).files('src', '*.groovy')
    sourceFiles.addAll(new Find(this).files('test', '*.groovy'))

    def dup = new DuplicateCodeFinder(this)
    dup.minimumBlockSize = 8
    dup.sourceFiles = sourceFiles
    dup.check()
''')
println('done')
```

Being in the root of this project call it like following:

```bash
groovy -cp src examples/xdup.groovy
```

It works really good. Even the `my.dsl.*` code can be easily unittested.
At the end you just need one *all in one* jar being executable to
pass a script file as parameter which works similar to the example above.

## Using the fat jar as tool

```bash
$ java -jar build/libs/jenkins-shared-pipeline-all-1.0.jar --help
usage: java -jar jenkins-shared-pipeline.jar [options]
Options:
 -h,--help            Print this help text and exit.
 -s,--script <file>   Groovy script file
```

If you put the code passed to the `ScriptExecutor` in last example into a file
you can do following

```bash
$ java -jar build/libs/jenkins-shared-pipeline-all-1.0.jar --script /tmp/xdup.groovy
```

## Supported DSL functions

It will constantly be extended but for now
following DSL functions are implemented:

| Commands:                                    |
| -------- | ---------- | --------- | -------- |
| echo     | sh         | writeFile | readFile |
| withEnv  | stage      | xparser   | xfind    |
| xpublish | xgit       | xgradle   | xrender  |
| xversion | virtualenv | xdup      |          |

**Please note**: The DSL **xpublish** does not do anything locally.


## Is vars dead code?

At least I have not found a way (yet) how to use it without Jenkins.
The **ScriptExecutor** expects an abstract class that implements those DSL
code for injecting it into a `GroovyShell` instance. The mechanism basically
comes from Groovy itself. Compiling the sources in *vars* (which is possible)
I need to find a way how those scripts know the DSL they use. Because of this
and the problem that the code cannot easily be tested (yet) you should keep the
code of your own DSL functions very vey small delegating to a class based
implementation using dependency injection under `src/**`.




