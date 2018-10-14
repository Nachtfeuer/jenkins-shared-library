# Git DSL

[TOC]

## Usage

```groovy
echo('Git short commit: ' + xgit.shortCommit)
echo('Git url: ' + xgit.url)
echo('Git author name: ' + xgit.authorName)
echo('Git author mail: ' + xgit.authorMail)
echo('Git last tag: ' + xgit.lastTag)
echo('Git changes since last tag: ' + xgit.changesSinceLastTag)
```

**Please note**:
 - In a declarative pipeline you have to place it in a `script { ... }` block.


## Example pipeline

The pipeline show here does not build anything; it's just the focus on Git and versioning.
The choosen model is **semantic versioning** means  we do not write a version into a pom.xml
or a build.gradle (or any other build file). The latest version is fetch from last tag;
if you don't have a tag yet the version specified by `define` is taken.

In given example the branch *master* is defined as only release branch; for all other branch
we specify the version as snapshot. For the release branch we do tagging only.

Tagging will be disabled if there is no change detected since last tag.

```groovy
@Library('jenkins-shared-library@master') _

def currentVersion = [:]
def taggingEnabled = false

pipeline {
    agent any

    options {
        buildDiscarder(logRotator(numToKeepStr: '10'))
        disableConcurrentBuilds()
        timestamps()
        ansiColor('xterm')
        timeout(time: 10, unit: 'MINUTES')
    }

    stages {
        stage('Prepare') {
            steps {
                script {
                    // defines your version policy (default here: major.minor)
                    currentVersion = xversion.define()
                    // trying to get current version from (last) tag
                    currentVersion = xversion.get(tag:currentVersion)
                    // master branch is release here; all other branches are snapshots
                    currentVersion.meta.snapshot = !env.BRANCH_NAME.equals('master')

                    def hasChanges = xgit.changesSinceLastTag
                    def description = "<b>commit</b>: ${xgit.shortCommit}, <b>lastAuthor</b>: ${xgit.authorName}"

                    if (!hasChanges) {
                        description = description + "<br/>No changes!"
                    }

                    currentBuild.description = description
                    // release branch (here: master) increments only
                    if (env.BRANCH_NAME.equals('master') && hasChanges) {
                        currentVersion = xversion.increment(minor:currentVersion)
                        taggingEnabled = true
                    }
                    currentBuild.displayName = '#' + env.BUILD_NUMBER + ' - ' + xversion.stringifyForTag(currentVersion)
                }
            }
        }
    }

    post {
        success {
            script {
                if ('master'.equals(env.BRANCH_NAME) && taggingEnabled) {
                    sshagent(['SSH_GIT_CREDENTIALS']) {
                        xversion.apply(tag:currentVersion)
                    }
                }
            }
        }
    }
}
```
