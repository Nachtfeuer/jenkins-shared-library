# Job DSL code support

[TOC]

## Purpose

Job DSL is great Jenkins pluging offering you an API for creating
and/or updating of Jenkins jobs and views. This library adds now
functionality to read a job description via Map (in Memory),
a JSON file or a YAML file generating Job DSL code.

## Working Example with JSON

```groovy
@Library('jenkins-shared-library@master')
import groovy.json.JsonOutput

pipeline {
    agent any
    stages {
        stage('Prepare') {
            steps {
                script {
                    final DATA = readJSON(text:groovy.json.JsonOutput.toJson([
                        type:'MULTIBRANCH_PIPELINE',
                        name:'jenkins-shared-library-demo',
                        description:'a Jenkins shared library',
                        source:'https://github.com/Nachtfeuer/jenkins-shared-library.git',
                        script:'Jenkinsfile',
                    ]))
                    writeJSON(file:'demo.json', json:DATA)
                }
            }
        }

        stage('Job DSL') {
            steps {
                script {
                    jobDsl(scriptText:jobDslCode.fromJson('demo.json))
                }
            }
        }
    }
}
```
