# Python virtual environments

[TOC]

## Most simple usage

```groovy
virtualenv {
    sh(script:'python -m install spline')
    sh(script:'python -m spline.application --help')
}
```
## Passing requirements

```groovy
virtualenv(['spline', 'requests==2.12.4']) {
    sh(script:'python -m spline.application --help')
}
```

## Defining another virtual environment folder

```groovy
virtualenv(['spline', 'requests==2.12.4'], 'another-venv') {
    sh(script:'python -m spline.application --help')
}
```

## Error handling

When inside a virtual environment an exception is thrown:

 - the result of such a block is **null**.
 - the pipeline state (currentBuild.result) will be set to 'FAILURE'.
 - the virtual environment will be removed independent of that issue.
