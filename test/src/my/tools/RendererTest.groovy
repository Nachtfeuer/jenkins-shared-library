package my.tools

import static org.assertj.core.api.Assertions.assertThat
import org.junit.Test

/**
 * Testing of class {@link Renderer}.
 */
class RendererTest {
    /** Testing simple valid use cases */
    @Test
    void testSimple() {
        def script = new MockScript()
        def model = [message:'hello']
        assertThat(new Renderer(script).render('message(model.message)', model)).isEqualTo(
            '<message>hello</message>')
        assertThat(new Renderer(script).render('message(model.message)', null)).isEqualTo(
            '<message/>')
    }

    /** Testing loops */
    @Test
    void testLoop() {
        def script = new MockScript()
        def model = [values:[2, 4, 6, 8, 10]]
        assertThat(new Renderer(script).render('model.values.each{ v(it) }', model)).isEqualTo(
             '<v>2</v><v>4</v><v>6</v><v>8</v><v>10</v>')
        assertThat(new Renderer(script).render('model.values.each{ yieldUnescaped("$it ") }', model)).isEqualTo(
             '2 4 6 8 10 ')
    }

    /** testing failing renderer; in this case the template is given back. */
    @Test
    void testFail() {
        def script = new MockScript()
        assertThat(new Renderer(script).render('message(model.foo.bar)', null)).isEqualTo('message(model.foo.bar)')
        assertThat(new Renderer(script).render('message{{}}', null)).isEqualTo('message{{}}')
    }
}
