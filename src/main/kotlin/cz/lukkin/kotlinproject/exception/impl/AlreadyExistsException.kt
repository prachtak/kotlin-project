package cz.lukkin.kotlinproject.exception.impl

import cz.lukkin.kotlinproject.annotation.ExceptionLabel
import cz.lukkin.kotlinproject.exception.KotlinProjectException

@ExceptionLabel("Resource already exists")
class AlreadyExistsException(msg: String?) : KotlinProjectException(msg) {
  companion object {
    private const val serialVersionUID = 1L
  }
}