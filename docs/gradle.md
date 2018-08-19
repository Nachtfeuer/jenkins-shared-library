# How to organize Gradle

[TOC]

## Why Gradle?

Gradle is **very comfortable** task manager to build your project.
You can define the required tasks with a **short amount of code**.

## The Gradle Wrapper

The [gradle wrapper](https://docs.gradle.org/current/userguide/gradle_wrapper.html)
helps you to install the Gradle version you need.

### Current Gradle version

```bash
./gradlew --version
```

### Adding a wrapper

```bash
gradle wrapper
```

### Upgrading the Gradle version

Define the version of Gradle you would like to have:

```bash
./gradlew wrapper --gradle-version 4.9
```

You can verify it like following:

```bash
$ cat gradle/wrapper/gradle-wrapper.properties
distributionBase=GRADLE_USER_HOME
distributionPath=wrapper/dists
distributionUrl=https\://services.gradle.org/distributions/gradle-4.9-bin.zip
zipStoreBase=GRADLE_USER_HOME
zipStorePath=wrapper/dists
```

## Basic Setup

 - Of course it requires `apply plugin: 'groovy'` because the shared library (Jenkins) depends on it.
 - The source sets have to be changed because the Jenkins guys do not use the standard folder structure
   as proposed by Maven/Gradle. The name of the languages (here: groovy) has been removed from the paths.
 - You are not advised to use **vars** in the source sets because you cannot test the code and
   you code coverage would essentially decrease.
 - **Jacoco** is used as official documented. Here 1.0 is adjusted as code coverage limit means 100%
   code coverage is expected. Adding new functionality without adding sufficient tests will fail
   the build.
 - **Codenarc** is the static code analyser for Groovy. Most rules are used and located under
   `config/codenarc/codenarc.rules`. Every rule that is not correct applied will fail the build.
 - **Coveralls** (a great service for visualizing code coverage results) is useful if you run your
   Github project with integrations like [Travis CI](https://travis-ci.org/) - In both cases you have to
   enable the repository there before you can use it; in my case login with my Github Account
   to Travis CI as well as to [COVERALLS](https://coveralls.io/); it's mainly a button to toggle the
   wanted repository on/off.

## Best Practises

 - When one of your tests is failing Gradle usually prints the path to the XML file instead
   of showing the problem. Therefor you can do following: `gradle test -i`; the `-i` forces
   to print all to stdout and that way you can easily scroll back the terminal to the
   printed callstack.
 - Continous testing can be done with `gradle test -it`. Gradle detects changes in files
   when you save them and reruns the tasks (here: all tests).
 - You also can run individual tests with a filter: `gradle test -it --tests GradleTest`
