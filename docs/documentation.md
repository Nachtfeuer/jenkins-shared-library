# How to organize Documenation

## Markdown

Markdown seems a good choice when writing documentation
with just a small set of formatting capabilities reducing
the documentation to what is really needed:

  - titles
  - sections
  - bold and italic emphasizing
  - code blocks
  - tables
  - links
  - embedded images

## Mkdocs

There are many good tools but I feel very comfortable with [mkdocs](https://www.mkdocs.org)
because of following reasons:

 - Easy to install (`sudo pip install mkdocs`)
 - Easy to configure (Small and simple `mkdocs.yml` in the root of your repository)
 - Easy to verify of the final result (`mkdocs serve`)
   Each change of the configured markdown files updates automatically your browser
 - Easy to publish to github pages (`mkdocs gh-deploy`)
   In my case: simply my user and password for my Github account


**Please note:** There's also a plugin for gradle but it looks to me that
the excepted folder struture is different and I have not yet found a way
to use the exising folder structure.
