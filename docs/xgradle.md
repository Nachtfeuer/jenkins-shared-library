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

**Please note**: In a declarative pipeline you have to place it in a `script { ... }`.

# Publish

Publishing of build results (junit, jacoco, ...):

```groovy
xgradle.publish()
```

**Please note**: In a declarative pipeline you have to place it in a `script { ... }`.
