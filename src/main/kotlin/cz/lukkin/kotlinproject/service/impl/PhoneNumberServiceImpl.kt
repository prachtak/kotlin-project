package cz.lukkin.kotlinproject.service.impl

import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber
import org.springframework.stereotype.Service

@Service
class PhoneNumberServiceImpl : PhoneNumberService {

  override fun isValidPhoneNumber(phoneNumber: String): Boolean {
    var valid: Boolean
    var phoneNumberParse: PhoneNumber? = null
    val phoneUtil = PhoneNumberUtil.getInstance()
    try {
      phoneNumberParse = phoneUtil.parse(phoneNumber, null)
      valid = phoneUtil.isValidNumber(phoneNumberParse)
    } catch (e: Exception) {
      valid = false
    }
    return valid
  }

  override fun getCountryCodeAndNumber(recipient: String): CountryCodeAndNumberDto {
    val phoneUtil = PhoneNumberUtil.getInstance()
    val swissNumberProto = phoneUtil.parse(recipient, null)
    return CountryCodeAndNumberDto(swissNumberProto.countryCode, phoneUtil.format(swissNumberProto, PhoneNumberUtil.PhoneNumberFormat.E164))
  }
}