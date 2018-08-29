import my.tools.Find

/**
 * Searching files (only).
 * @param path the path where to start searching of files.
 * @param name the search criteria (wildcards are allowed).
 * @return list of found files
 */
List<String> files(final String path, final String name) {
    new Find(this).files(path, name)
}
