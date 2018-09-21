import my.tools.Parser

/**
 * Converts XML string into Groovy object model.
 *
 * @param text XML string to parse.
 * @return parsed XML as Groovy object model.
 */
Map xml(final String text) {
    new Parser.fromXml(text)
}
