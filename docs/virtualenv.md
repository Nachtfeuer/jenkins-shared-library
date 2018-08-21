# Python virtual environments


## How to use

```groovy
virtualenv {
    sh(script:'python -m install spline')
    sh(script:'python -m spline.application --help')
}
```
