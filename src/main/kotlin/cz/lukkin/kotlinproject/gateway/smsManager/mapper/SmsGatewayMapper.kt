package cz.lukkin.kotlinproject.gateway.smsManager.mapper

import com.fasterxml.jackson.dataformat.xml.XmlMapper
import cz.lukkin.kotlinproject.gateway.smsManager.model.payload.SmsManagerRequest
import org.mapstruct.Mapper

@Mapper
abstract class SmsGatewayMapper {

  fun mapSmsRequestDocument(sms: Sms, apiKey: String): String {
    val requestHeader = SmsManagerRequest.RequestHeader(apiKey)
    val message = SmsManagerRequest.Message(sms.message)
    val number = SmsManagerRequest.Number(sms.number)
    val request = SmsManagerRequest.Request(mapTariff(sms.tariff), message, listOf(number))
    val requestList = listOf(request)
    val smsToSend = SmsManagerRequest(requestHeader = requestHeader, requestList = requestList)
    val xmlMapper = XmlMapper()
    return xmlMapper.writeValueAsString(smsToSend)
  }

  private fun mapTariff(tariff: Tariff): SmsManagerTariff {
    return when (tariff) {
      Tariff.LOW -> SmsManagerTariff.lowcost
      Tariff.MEDIUM -> SmsManagerTariff.economy
      Tariff.HIGH -> SmsManagerTariff.high
    }
  }

  fun mapSmsSentStatus(statusCode: Int, withoutDelivery: Boolean): SmsStatus {
    return when (statusCode) {
      0, 1, 3, 5, 9 -> SmsStatus.SENT_TO_GATEWAY
      2 -> if (withoutDelivery) SmsStatus.SENT_WITHOUT_DELIVERY else SmsStatus.SENT_WITH_DELIVERY
      4 -> SmsStatus.NOT_DELIVERED
      else -> SmsStatus.SENT_TO_GATEWAY
    }
  }

  fun mapSmsDeliveryStatus(statusCode: Int, smsStatus: SmsStatus): SmsStatus {
    return when (statusCode) {
      2 -> SmsStatus.DELIVERED
      3, 6, 7, 8 -> SmsStatus.NOT_DELIVERED
      else -> smsStatus
    }
  }
}