package cz.lukkin.kotlinproject.annotation

@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.CLASS)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class ExceptionLabel(
    /**
     * @return Label value
     */
    val value: String = "")