# The publisher


## Publishing HTML Reports

```groovy
xpublish.html('HTML Code Coverage', 'build/reports/coverage')
```

**Please note**: Using a declarative pipeline you have to call it inside
                 a `script { ... }` block.
