package cz.lukkin.kotlinproject.gateway.smsManager.client.configuration

import feign.Response
import feign.Util
import feign.codec.ErrorDecoder

class CustomFeignErrorDecoder : ErrorDecoder {

  companion object : Log()

  override fun decode(methodKey: String?, response: Response?): Exception {

    log.debug("Processing custom ErrorDecoder for response: {}", response!!.body())
    var responseBody = ""
    try {
      responseBody = when {
        response.body() != null -> {
          String(Util.toByteArray(response.body().asInputStream()))
        }
        else -> {
          response.reason()
        }
      }
    } catch (e: Exception) {
      log.debug("Error while decoding smsResponse: {} ", e)
    }
    return SmsManagerResponseException(status = response.status(), message = responseBody)
  }
}
