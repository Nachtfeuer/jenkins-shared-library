# Git DSL

## Usage

```groovy
echo('Git short commit: ' + xgit.shortCommit)
echo('Git url: ' + xgit.url)
echo('Git author name: ' + xgit.authorName)
echo('Git author mail: ' + xgit.authorMail)
```

**Please note**:
 - In a declarative pipeline you have to place it in a `script { ... }` block.
