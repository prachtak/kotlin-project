package cz.lukkin.kotlinproject.exception.impl

import cz.lukkin.kotlinproject.annotation.ExceptionLabel
import cz.lukkin.kotlinproject.exception.KotlinProjectException

@ExceptionLabel("Sms provider exception.")
class BadGatewayException : KotlinProjectException {
  constructor(message: String?) : super(message) {}
  constructor(message: String?, cause: Throwable?) : super(message, cause) {}

  companion object {
    private const val serialVersionUID = 1L
  }
}