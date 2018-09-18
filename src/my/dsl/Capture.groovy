package my.dsl

/**
 * Capturing stdout.
 */
final class Capture {
    /** keeping original stream */
    private final originalStdoutStream

    /** remember original stdout */
    Capture() {
        this.originalStdoutStream = System.out
    }

    /**
     * Capturing stdout (like println).
     *
     * @param body closure; all outputs done in the block are captured.
     * @return captured stdout as string.
     */
    final String stdout(final Closure body) {
        def buffer = new ByteArrayOutputStream()
        def capturedStream = new PrintStream(buffer)
        System.out = capturedStream
        body()
        System.out = this.originalStdoutStream
        buffer.toString()
    }
}
