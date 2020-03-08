package cz.lukkin.kotlinproject.gateway.smsManager

import org.mapstruct.factory.Mappers
import org.springframework.stereotype.Component
import java.net.URI
import java.time.Instant

@Component
class SmsManager(private val smsManagerController: SmsManagerController, private val smsManagerConfigurationProperties: SmsManagerConfigurationProperties) : GatewayAdapter() {

  companion object : Log()

  val mapper: SmsGatewayMapper = Mappers.getMapper(SmsGatewayMapper::class.java)

  override fun send(sms: Sms): Sms {
    val response: SmsManagerResponse
    val smsData = mapper.mapSmsRequestDocument(sms, smsManagerConfigurationProperties.apiKey)
    try {
      response = smsManagerController.sendSms(URI.create(smsManagerConfigurationProperties.url), mapOf("XMLDATA" to smsData))
      sms.status = mapper.mapSmsSentStatus(response.response.statusCode, sms.tariff == Tariff.LOW)
      response.responseRequests.forEach { sr -> sms.gatewayRequestId = sr.requestId }
    } catch (ex: SmsManagerResponseException) {
      log.warn("Error while sending sms: {}", ex.message.toString())
      val primary = GatewayStatusType(gateway = sms.gateway, status = ex.status, errorMessage = ex.message)
      sms.gatewayStatus = GatewayStatus(primary, null)

      if (ex.status in 400..499) {
        sms.status = SmsStatus.NOT_DELIVERED
      }
    }
    sms.sentTime = Instant.now()
    return sms
  }
}