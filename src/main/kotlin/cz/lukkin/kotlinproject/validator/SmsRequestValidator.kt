package cz.lukkin.kotlinproject.validator

import org.springframework.stereotype.Component
import org.springframework.validation.Errors
import org.springframework.validation.Validator

@Component
class SmsRequestValidator(private val phoneNumberService: PhoneNumberService) : Validator {

  companion object : Log()

  override fun validate(target: Any, errors: Errors) {
    val smsRequest: SmsRequest = target as SmsRequest

    if (emptyRecipientsValidator(smsRequest)) {
      errors.rejectValue("recipients", "smsRequest.recipients", "can not be empty")
    }
    val invalidPhoneNumber = phoneNumberValidator(smsRequest.recipients)
    if (invalidPhoneNumber.isNotEmpty()) {
      errors.rejectValue("recipients", " smsRequest.recipients", "not valid number/s: $invalidPhoneNumber")
    }
    if (smsRequest.tariff != null && tariffValidator(smsRequest.tariff) == null) {
      errors.rejectValue("tariff", "smsRequest.Tariff", "is not supported: [" + smsRequest.tariff + "]. Supported values are [Low, Medium, High]")
    }
  }

  override fun supports(clazz: Class<*>): Boolean {
    return SmsRequest::class.java.isAssignableFrom(clazz)
  }

  private fun emptyRecipientsValidator(smsRequest: SmsRequest): Boolean {
    if (smsRequest.email == null) {
      if (smsRequest.recipients == null) {
        return true
      }
      if (smsRequest.recipients[0].isBlank()) {
        return true
      }
    }
    return false
  }

  private fun tariffValidator(requestTariff: String?): Tariff? {
    return Tariff.values().find { tariff -> tariff.value == requestTariff }
  }

  private fun phoneNumberValidator(recipients: List<String>?): List<String> {
    val errorNumbers: MutableList<String> = mutableListOf()
    recipients?.forEach { recipient ->
      if (!phoneNumberService.isValidPhoneNumber(recipient)) {
        errorNumbers.add(recipient)
      }
    }
    return errorNumbers
  }
}