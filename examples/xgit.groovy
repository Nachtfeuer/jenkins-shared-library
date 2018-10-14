/**
 * Execute <pre>groovy -cp src examples/xgit.groovy</pre>
 * from the root of this repository
 */
import my.dsl.Jenkins
import my.tools.Git

def script = [:] as  Jenkins
def git = new Git(script)

println("git commit: " + git.shortCommit)
println("git url: " + git.url)
println("git author name: " + git.authorName)
println("git author email: " + git.authorMail)
println("git last tag: " + git.lastTag)
println("git changes since last tag: " + git.changesSinceLastTag)
