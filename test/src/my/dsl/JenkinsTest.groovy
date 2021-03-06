package my.dsl

import static org.assertj.core.api.Assertions.assertThat
import org.junit.Test

import my.tools.Git
import my.tools.Gradle
import my.tools.Version

/**
 * Testing of class {@link Jenkins}.
 */
class JenkinsTest {
    /** Testing of {@link Jenkins#echo(final String)}. */
    @Test
    void testEcho() {
        def jenkins = [:] as Jenkins
        def output = new Capture().stdout {
            jenkins.echo('hello')
        }
        assertThat(output).isEqualTo('hello\n')
    }

    /** Testing of {@link Jenkins#sh(final Map)}. */
    @Test
    void testSh() {
        def jenkins = [:] as Jenkins
        assertThat(jenkins.sh(script:'echo hello', returnStdout:true)).isEqualTo('hello')
        def output = new Capture().stdout {
            assertThat(jenkins.sh(script:'echo hello')).isEqualTo(null)
        }
        assertThat(output).isEqualTo('hello\n')
    }

    /** Testing of {@link Jenkins#sh(final Map)}. */
    @Test
    void testShWithEnv() {
        def jenkins = [:] as Jenkins
        jenkins.withEnv(['MESSAGE=hello']) {
            assertThat(jenkins.sh(script:'echo \$MESSAGE', returnStdout:true)).isEqualTo('hello')
        }
    }

    /** Testing of {@link Jenkins#writeFile(final Map)}. */
    @Test
    void testWriteFile() {
        def jenkins = [:] as Jenkins
        jenkins.writeFile(file:'test_write.txt', text:'test write')
        assertThat(new File('test_write.txt').text).isEqualTo('test write')
        new File('test_write.txt').delete()
        assertThat(new File('test_write.txt').exists()).isEqualTo(false)
    }

    /** Testing of {@link Jenkins#readFile(final Map)}. */
    @Test
    void testReadFile() {
        def jenkins = [:] as Jenkins
        new File('test_read.txt').text = 'test read'
        assertThat(jenkins.readFile(file:'test_read.txt')).isEqualTo('test read')
        new File('test_read.txt').delete()
        assertThat(new File('test_read.txt').exists()).isEqualTo(false)
    }

    /**
     * Testing <b>withEnv</b> DSL behaving correctly.
     */
    @Test
    void testWithEnd() {
        def jenkins = [:] as Jenkins
        assertThat(jenkins.env).doesNotContainKey('DEMO0')
        jenkins.withEnv(['DEMO0=main']) {
            assertThat(jenkins.env).containsEntry('DEMO0', 'main')
            assertThat(jenkins.env).doesNotContainKey('DEMO1')
            assertThat(jenkins.env).doesNotContainKey('DEMO2')
            jenkins.withEnv(['DEMO1= hello ', 'DEMO2= world ']) {
                assertThat(jenkins.env).containsEntry('DEMO1', 'hello')
                assertThat(jenkins.env).containsEntry('DEMO2', 'world')
            }
            assertThat(jenkins.env).containsEntry('DEMO0', 'main')
            assertThat(jenkins.env).doesNotContainKey('DEMO1')
            assertThat(jenkins.env).doesNotContainKey('DEMO2')
        }
    }

    /**
     * Testing <b>stage</b> DSL behaving correctly.
     */
    @Test
    void testStage() {
        def jenkins = [:] as Jenkins
        def output = new Capture().stdout {
            jenkins.stage('Demo') {
                jenkins.echo('hello')
            }
        }

        assertThat(output).isEqualTo('stage(Demo) {\nhello\n}\n')
    }

    /** Testing xparser.xml DSL */
    @Test
    void testParseXml() {
        def jenkins = [:] as Jenkins
        def pomFile = System.getProperty('user.dir') + '/test/resources/pom.default.xml'
        def text = new File(pomFile).text
        assertThat(jenkins.xparser.xml(text)).isEqualTo(
            [modelVersion:'4.0.0', groupId:'my-group-id', artifactId:'my-artifact-id', version:'1.0'])
    }

    /** Testing xfind.files DSL */
    @Test
    void testFindFiles() {
        def jenkins = [:] as Jenkins
        def path = System.getProperty('user.dir') + '/test/resources'
        def result = jenkins.xfind.files(path, '*.xml')
        assertThat(result.size()).isEqualTo(1)
        assertThat(result[0]).contains('pom.default.xml')
    }

    /** Testing xpublish.html DSL */
    @Test
    void testPublishHtml() {
        def jenkins = [:] as Jenkins
        assertThat(jenkins.xpublish).isInstanceOf(Map)
        // just to verify the calls (no real publishing when running locally)
        jenkins.xpublish.html('HTML Code Coverage', 'build/reports/coverage')
        jenkins.xpublish.html('HTML Code Coverage', 'build/reports/coverage', 'index.html')
    }

    /** Testing xgit DSL */
    @Test
    void testGit() {
        def jenkins = [:] as Jenkins

        def gitUrls = [
            'https://github.com/Nachtfeuer/jenkins-shared-library.git',
            'git@github.com:Nachtfeuer/jenkins-shared-library.git'
        ]

        assertThat(jenkins.xgit).isInstanceOf(Git)
        assertThat(gitUrls).contains(jenkins.xgit.url)
        assertThat(jenkins.xgit.authorName).isEqualTo('Thomas Lehmann')
        assertThat(jenkins.xgit.authorMail).isEqualTo('thomas.lehmann.private@gmail.com')
    }

    /** Testing xgradle DSL */
    @Test
    void testGradle() {
        def jenkins = [:] as Jenkins
        assertThat(jenkins.xgradle).isInstanceOf(Gradle)
    }

    /** Testing xrender DSL */
    @Test
    void testRender() {
        def jenkins = [:] as Jenkins
        def model = [values:[2, 4, 6, 8, 10]]
        assertThat(jenkins.xrender('model.values.each{ v(it) }', model)).isEqualTo(
             '<v>2</v><v>4</v><v>6</v><v>8</v><v>10</v>')
        assertThat(jenkins.xrender('model.values.each{ yieldUnescaped("$it ") }', model)).isEqualTo(
             '2 4 6 8 10 ')
    }

    /** Testing xversion DSL */
    @Test
    void testVersion() {
        def jenkins = [:] as Jenkins
        assertThat(jenkins.xversion).isInstanceOf(Version)
    }

    /** Testing of virtualenv DSL */
    @Test
    void testVirtualEnv() {
        def jenkins = [:] as Jenkins
        jenkins.virtualenv {
            // virtual environment should be created
            assertThat(new File('venv').exists()).isEqualTo(true)
        }
        // virtual environment should be removed
        assertThat(new File('venv').exists()).isEqualTo(false)
    }

    /** Testing of xdup DSL */
    @Test
    void testDuplicateCodeChecker() {
        def jenkins = [:] as Jenkins
        assertThat(new File('duplicates').exists()).isEqualTo(false)

        def listOfSource = jenkins.xfind.files(System.getProperty('user.dir') + '/test', '*.groovy')
        jenkins.xdup(listOfSource)
        assertThat(new File('duplicates/index.html').exists()).isEqualTo(true)

        // cleanup
        'rm -rf duplicates'.execute().text.eachLine { jenkins.echo(it) }
        assertThat(new File('duplicates').exists()).isEqualTo(false)
    }
}
