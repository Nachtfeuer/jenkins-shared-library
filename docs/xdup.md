# Duplicate Code Finder


## Introduction

In fact the tool does not know about code but text.
Following options are available:

 - **(m)inimum(B)lockSize**: When duplicate code/test has been found it has to be greater
   or equal to those configured size otherwise it will be ignored. (default: 4)
 - **(i)gnore(C)ase**: when true then the letter case is ignored (default: false)
 - **(i)gnore(W)hitespaces**: when true then spaces and tabs are ignored (default: false)
 - **(p)ercentage(S)imilarity**: control how many characters have to match (in percentage)

The next table should give a few simple examples on line comparisons:

| First String    | Second String     | Adjusted Policies | Result    |
| --------------- | ----------------- | ----------------- | --------- |
| wonderful day   | wonderful day     |                   | equal     |
| wonderful DAY   | wonderful day     |                   | not equal |
| wonderful DAY   | wonderful day     | iC=true           | equal     |
| wonderful   DAY | wo nd erful  d ay | iC=true           | not equal |
| wonderful   DAY | wo nd erful  d ay | iC=true, iW=true  | equal     |
| Wonderful Day   | wonderful day     | pS=75             | equal     |
| -onderful -ay   | wonderful day     | pS=75             | equal     |


## Usage

```groovy
xdup.check(path:'src', extensions: ['.groovy'],
           minimumBlockSize:6, ignoreCase:true, ignoreWhitespaces:true, percentageSimilarity:90)
```

**Please note**: In a declarative pipeline you have to place it in a `script { ... }` block.
