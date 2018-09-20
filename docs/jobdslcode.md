# Job DSL code support

[TOC]

## Purpose

Job DSL is great Jenkins pluging offering you an API for creating
and/or updating of Jenkins jobs and views. This library adds now
functionality to read a job description via Map (in Memory),
a JSON file or a YAML file generating Job DSL code.

## Job definition

Independent whether you use a Map in memory, a JSON file or a yaml file the
job definition for creating a new job is following:

| field         | meaning           | comment                                                      |
|-------------- | ----------------- | ------------------------------------------------------------ |
| name          | name of the job   | an existing job with will be update if it is of same type    |
| description   | job description   | also may contain HTML; rendered when configured in Jenkins   |
| type          | type of job       | supported: MULTIBRANCH_PIPELINE (default) or PIPELINE        |
| source        | source url        | git is supported only (ssh or https variant of url to clone) |
| credentialsId | Id of credentials | *optional*; Jenkins credentials store for SSH credentials    |
| script        | Jenkinsfile path  | path and filename of Jenkinsfile (default: Jenkinsfile)      |
| history       | number of builds  | *pipeline only*; how many old builds to keep (default: 30)   |
| libraries     | list of libraries | defines shared libraries for multibranch jobs                |

A library entry is again a map:

| field          | meaning             | comment                                         |
|--------------- | ------------------- | ----------------------------------------------- |
| name           | name of the library | The name to specify with @Library               |
| defaultVersion | branch or tag       | if not specified: master                        |
| credentialsId  | id for access       | refers to Jenkins credential store; optional    |
| url            | git url             | url of source code repository (https, ssh, ...) |



## Example with JSON

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
                        libraries: [[
                            name:'jenkins-shared-library',
                            url:'https://github.com/Nachtfeuer/jenkins-shared-library.git'
                        ]]
                    ]))
                    writeJSON(file:'demo.json', json:DATA)
                }
            }
        }

        stage('Job DSL') {
            steps {
                script {
                    jobDsl(scriptText:jobDslCode.fromJson('demo.json'))
                }
            }
        }
    }
}
```

## Example with YAML

```groovy
@Library('jenkins-shared-library@master')

pipeline {
    agent any
    stages {
        stage('Prepare') {
            steps {
                script {
                    final DATA = [
                        type:'MULTIBRANCH_PIPELINE',
                        name:'jenkins-shared-library-demo',
                        description:'a Jenkins shared library',
                        source:'https://github.com/Nachtfeuer/jenkins-shared-library.git',
                        script:'Jenkinsfile',
                        libraries: [[
                            name:'jenkins-shared-library',
                            url:'https://github.com/Nachtfeuer/jenkins-shared-library.git'
                        ]]
                    ]
                    writeYaml(file:'demo.yaml', data:DATA)
                }
            }
        }

        stage('Job DSL') {
            steps {
                script {
                    jobDsl(scriptText:jobDslCode.fromYaml('demo.yaml'))
                }
            }
        }
    }
}
```
