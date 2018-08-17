package my.tools

/**
 * Mock for Jenkins DSL.
 */
class MockScript {
    /** list of calls */
    private final List calls = []

    /** access to list off call. **/
    def getCalls() {
        this.calls
    }

    /** mock of the Jenkins sh DSL function. */
    def sh(final Map config) {
        this.calls.add(['sh', config])
    }
}
