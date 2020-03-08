package cz.lukkin.kotlinproject.controller.advice

import cz.lukkin.kotlinproject.validator.SmsRequestValidator
import org.springframework.web.bind.WebDataBinder
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.InitBinder

@ControllerAdvice
class InitBinderControllerAdvice(private val smsRequestValidator: SmsRequestValidator) {

  @InitBinder
  private fun bindValidator(webDataBinder: WebDataBinder) {
    webDataBinder.addValidators(smsRequestValidator)
  }
}