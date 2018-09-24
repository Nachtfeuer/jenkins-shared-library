import my.tools.Git

/**
 * Get git short commit for current repository.
 * @return git short commit.
 */
String getShortCommit() {
    new Git(this).shortCommit
}

/**
 * Get git url
 * @return git url.
 */
String getUrl() {
    new Git(this).url
}

/**
 * Get last commit author name.
 * @return author name.
 */
String getAuthorName() {
    new Git(this).authorName
}

/**
 * Get last commit author mail.
 * @return author mail.
 */
String getAuthorMail() {
    new Git(this).authorMail
}

/**
 * Get last tag.
 * @return last tag.
 */
String getLastTag() {
    new Git(this).lastTag
}
