# Own Gradle DSL

# Purpose

 - builder concept (chaining of tasks)
 - standardization (junit, jacoco, ...)

# Build

The **xgradle** DSL does use internally a **Gradle** class
chaining the tasks **clean** and **check**:

```groovy
xgradle.build()
```

**Please note**: In a declarative pipeline you have to place it in a `script { ... }` block.

# Publish

Publishing of build results (junit, jacoco, HTML coverage and Pit test coverage):

```groovy
xgradle.publish()
```

**Please note**:
 - In a declarative pipeline you have to place it in a `script { ... }` block.
 - The reports are published only when the path do exist; for coverage
   `build/reports/coverage` and for pit test `build/reports/pitest`
   are the expected paths. Using **Jacoco** you can configure it with
   `html.destination file("${buildDir}/reports/coverage")`.
   You can check the file `build.gradle` of this project.
