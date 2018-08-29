# Find DSL

## How to use?

```groovy
def files = xfind.files('.', '*.json')
```

The command is equivalent to `find . -type f -name "*.json"`.

**Please note**:
 - In a declarative pipeline you have to place it in a `script { ... }` block.
