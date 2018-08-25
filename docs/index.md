# Welcome to the Jenkins shared library project

[TOC]

## Purpose

 - providing a reference example project for your own Jenkins shared library project
 - demonstrating usage of the basic toolset: Groovy, Gradle, Jacoco, Codenarc, mkdocs, ...
 - providing Jenkinsfile (declarative pipeline) as well as .travis.yml
 - automatically running this project on Travis CI and publishing coverage on coveralls.io
 - providing detailed documentation

## Quickstart

Using the Gradle wrapper the configured Gradle version will be automatically
downloaded and used. The basic requirements can be easily seen in the file
`Dockerfile` serving as description for a Docker build image.

```bash
./gradlew
```

## About quality

 - You should have a verified code style. The tool **Codenarc** provides you for Groovy
   what Checkstyle does for Java. The build does fail when any source code does
   not match the defined rules. It runs automatically with `./gradlew`.
 - Code coverage check is configured and **100% code coverage (line) is expected**!
   If you add new functionality without a test the build will fail.
   It also runs automatically with `./gradlew`.
 - **Provide reasonable documentation**. Any documentation like this one is for you as
   well as for others. Please consider source code documentation as well
   as markdown based documentation to help to understand things.

## Useful links

 - <https://jenkins.io/doc/book/pipeline/syntax/>
 - <https://jenkins.io/doc/pipeline/steps/pipeline-utility-steps/>
 - <https://jenkins.io/doc/book/pipeline/syntax/#declarative-pipeline>
 - <https://jenkins.io/doc/book/pipeline/docker/>
 - <https://jenkins.io/doc/pipeline/steps/>
 - <https://gradle.org/>
 - <https://docs.gradle.org/current/userguide/jacoco_plugin.html>
 - <https://gradle-pitest-plugin.solidsoft.info/>
 - <http://pitest.org/>
 - <http://codenarc.sourceforge.net/>
 - <https://www.mkdocs.org/>
