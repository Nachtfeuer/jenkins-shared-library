/**
 * Provides annotation as used in a Jenkins for specifying the shared library to use.
 */
@interface Library {
    String value()
    String name() default ''
}
