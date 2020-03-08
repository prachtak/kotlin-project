package cz.lukkin.kotlinproject.gateway.smsManager.client.configuration

import com.fasterxml.jackson.dataformat.xml.XmlMapper
import cz.lukkin.kotlinproject.gateway.smsManager.model.payload.SmsManagerResponse
import cz.lukkin.kotlinproject.logger.Log
import feign.Response
import feign.Util
import feign.codec.Decoder
import java.lang.reflect.Type

class CustomFeignDecoder : Decoder {

  companion object : Log()

  override fun decode(response: Response?, type: Type?): SmsManagerResponse {

    log.debug("Processing custom Decoder for response: {}", response!!.body())
    val responseBody: String?
    var smsManagerResponse = SmsManagerResponse()
    try {
      responseBody = when {
        response.body() != null -> {
          String(Util.toByteArray(response.body().asInputStream()))
        }
        else -> {
          response.reason()
        }
      }
      val xmlMapper = XmlMapper()
      smsManagerResponse = xmlMapper.readValue(responseBody, SmsManagerResponse::class.java)
    } catch (e: Exception) {
      log.error("Error while decoding smsResponse: {}", e)
    }
    return smsManagerResponse
  }
}