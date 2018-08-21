import my.tools.VirtualEnv

/**
 * Provides a Python virtual environment inside the block (closure).
 *
 * @param requirements optional list of requirements (Python mododules or tools)
 * @param folderName optional folder name (default: venv)
 * @return whatever the inner block does return.
 */
def call(final List<String> requirements=[], final String folderName='venv', final Closure body) {
    new VirtualEnv(this)
        .configure(folderName:folderName, requirements:requirements)
        .process() {
        body()
    }
}
