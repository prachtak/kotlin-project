package cz.lukkin.kotlinproject.exception.impl

import cz.lukkin.kotlinproject.annotation.ExceptionLabel
import cz.lukkin.kotlinproject.exception.DetailAware
import cz.lukkin.kotlinproject.exception.KotlinProjectException
import org.springframework.validation.BindingResult
import org.springframework.validation.FieldError
import org.springframework.validation.ObjectError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.support.WebExchangeBindException
import java.util.*
import java.util.stream.Collectors

@ExceptionLabel("Validation Error")
class ValidationException : KotlinProjectException, DetailAware {

  private val serialVersionUID = 1L

  private var bindingResult: BindingResult? = null

  constructor(message: String) : super(message)
  constructor(message: String, cause: Throwable) : super(message, cause)

  constructor() {
    bindingResult = null
  }

  constructor(bindingResult: BindingResult) {
    this.bindingResult = bindingResult
  }

  constructor(cause: WebExchangeBindException) : super(cause) {
    bindingResult = cause
  }

  constructor(cause: MethodArgumentNotValidException) : super(cause) {
    bindingResult = cause.bindingResult
  }

  override val detail: Optional<Any>
    get() = Optional.ofNullable(bindingResult).map { obj: BindingResult -> getErrors(obj) }

  private fun getErrors(bindingResult: BindingResult): List<String>? {
    return bindingResult.allErrors.stream()
        .map<String> { f: ObjectError ->
          if (f is FieldError) {
            "Field '" + f.field + "' " + f.getDefaultMessage() + "."
          } else {
            f.defaultMessage
          }
        }
        .collect(Collectors.toList())
  }
}