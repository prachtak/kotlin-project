package cz.lukkin.kotlinproject.service
import org.springframework.stereotype.Service

@Service
interface PhoneNumberService {
  fun isValidPhoneNumber(phoneNumber: String): Boolean
  fun getCountryCodeAndNumber(recipient: String): CountryCodeAndNumberDto
}