import my.tools.Renderer

/**
 * Render a template as a final string.
 *
 * @param template the template to render.
 * @param model is a map of data to be used in the template.
 * @return rendered template is a normal string.
 */
@NonCPS
String call(final String template, final Map model) {
    new Renderer(this).render(template, model)
}
