package my.tools

import groovy.text.markup.MarkupTemplateEngine
import groovy.text.markup.TemplateConfiguration

/**
 * Tool for providing text rendering support.
 */
final class Renderer extends Base {
    /** tool class, instantiation not wanted */
    Renderer(final script) {
        super(script)
    }

    /**
    * Render a template as a final string.
    *
    * @param template the template to render.
    * @param model is a map of data to be used in the template.
    * @return rendered template is a normal string.
    */
    String render(final String template, final Map model) {
        try {
            TemplateConfiguration config = new TemplateConfiguration()
            MarkupTemplateEngine engine = new MarkupTemplateEngine(config)
            engine.createTemplate(template).make(model).toString()
        } catch (err) {
            template
        }
    }
}
