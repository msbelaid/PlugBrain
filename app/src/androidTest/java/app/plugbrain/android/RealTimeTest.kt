package app.plugbrain.android

/**
 * A test that waits for real minutes to pass. CI runs these only for release tags; pull requests
 * skip them with the notAnnotation runner argument.
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
annotation class RealTimeTest
