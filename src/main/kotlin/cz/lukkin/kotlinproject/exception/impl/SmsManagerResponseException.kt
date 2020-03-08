package cz.lukkin.kotlinproject.exception.impl

import cz.lukkin.kotlinproject.annotation.ExceptionLabel
import cz.lukkin.kotlinproject.exception.KotlinProjectException

@ExceptionLabel("Validation Error")
class SmsManagerResponseException(message: String, var status: Int) : KotlinProjectException(message) {
  companion object {
    private const val serialVersionUID = 1L
  }
}