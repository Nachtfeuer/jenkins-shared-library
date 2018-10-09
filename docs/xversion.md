# Versioning and Tagging

[TOC]

## Versioning

### Defining your version

First you define your version giving the individual parts
a name; the names are your personal choice. The numbers
are the initial version when there is no version yet.

```groovy
def version = xversion.define(major:1, minor:0, patch:0)
println(version) // prints [data:[major:1, minor:0, patch:0], meta:[snapshot:false, prefix:'v']]
```

or using *major* and *minor* as default policy:

```groovy
def version = xversion.define()
println(version) // prints [data:[major:1, minor:0], meta:[snapshot:false, prefix:'v']]
```

### Increment the version

The incrementor - as the name says - increments a version part.

```groovy
def version = xversion.define(major:1, minor:0, patch:0)
version = xversion.increment(major:version)
println(version) // prints [data:[major:2, minor:0, patch:0], meta:[snapshot:false, prefix,'v']]
```

or

```groovy
def version = xversion.define(major:2, minor:0, patch:0)
version = xversion.increment(minor:version)
println(version) // prints [data:[major:2, minor:1, patch:0], meta:[snapshot:false, prefix,'v']]
```

### Retrieving current version

You can retrieve the version either by the underyling build tools
or using last tag. When there is not tag the defined version is
used. It's expected that existing version match the version policy
otherwise an exception is thrown.

```groovy
def version = xversion.get(maven:version)   // reading from pom.xml
def version = xversion.get(gradle:version)  // reading from build.gradle
def version = xversion.get(tag:version)     // reading from tag
```


### Applying version to tool

It does change the version either in the `pom.xml` or in the `build.gradle`:

```groovy
xversion.apply(maven:version)   // adjusts the pom.xml
xversion.apply(gradle:version)  // adjusts the version field in the build.gradle
xversion.apply(tag:version)     // creates and pushes version as tag
```
