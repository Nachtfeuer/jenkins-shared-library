package my.tools

import groovy.util.slurpersupport.GPathResult

/**
 * Parser for XML, Yaml and JSON
 */
class Parser {
    /**
     * Converts XML string into Groovy object model.
     *
     * @param text XML string to parse.
     * @return parsed XML as Groovy object model.
     */
    Map parseXml(final String text) {
        this.convertXml(new XmlSlurper().parseText(text))
    }

    /**
     * Converts parsed XML string into Groovy object model.
     * @param nodes Xml nodes from XmlSlurper.
     * @return map with Xml string as Groovy object model.
     */
    private Map convertXml(final GPathResult nodes) {
        nodes.children().collectEntries {
            [it.name(), it.childNodes() ? convertXml(it) : it.text()]
        }
    }
}
