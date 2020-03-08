package cz.lukkin.kotlinproject.exception

import cz.lukkin.kotlinproject.annotation.ExceptionLabel
import org.springframework.core.annotation.AnnotationUtils
import java.text.MessageFormat
import java.util.*

abstract class KotlinProjectException : RuntimeException {
  lateinit var label: String

  constructor() {}
  constructor(message: String?) : super(message) {}
  constructor(message: String?, vararg args: Any?) : super(MessageFormat.format(message, *args)) {}
  constructor(message: String?, cause: Throwable?) : super(message, cause) {}
  constructor(message: String?, cause: Throwable?, vararg args: Any?) : super(MessageFormat.format(message, *args), cause) {}
  constructor(cause: Throwable?) : super(cause) {}
  constructor(message: String?, cause: Throwable?, enableSuppression: Boolean, writableStackTrace: Boolean) : super(message, cause, enableSuppression, writableStackTrace) {}

  companion object {
    private const val serialVersionUID = 1L
  }

  init {
    Optional.ofNullable(AnnotationUtils.findAnnotation(this.javaClass, ExceptionLabel::class.java))
        .ifPresent { a: ExceptionLabel -> label = a.value }
  }
}