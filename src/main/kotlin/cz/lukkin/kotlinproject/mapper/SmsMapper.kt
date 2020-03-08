package cz.lukkin.kotlinproject.mapper

import org.bson.types.ObjectId
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings
import java.time.Instant


@Mapper
abstract class SmsMapper {

  @Mappings(
      Mapping(target = "countryCode", source = "countryCodeAndNumberDto.countryCode"),
      Mapping(target = "number", source = "countryCodeAndNumberDto.number"),
      Mapping(target = "message", source = "smsRequest.text"),
      Mapping(target = "consumerId", source = "consumerId")
  )
  abstract fun mapToDomain(smsRequest: SmsRequest, countryCodeAndNumberDto: CountryCodeAndNumberDto, ticketId: String, tariff: Tariff?, created: Instant, status: SmsStatus, gateway: Gateway, consumerId: ObjectId?): Sms

  fun mapTariff(reqTariff: String?): Tariff {
    var tariff = Tariff.values().find { tariff -> tariff.value == reqTariff }
    if (tariff == null) {
      tariff = Tariff.MEDIUM
    }
    return tariff
  }

  @Mappings(
      Mapping(target = "id", source = "ticketId"),
      Mapping(target = "recipients", source = "smsRequest.recipients"),
      Mapping(target = "text", source = "smsRequest.text"),
      Mapping(target = "latestSendTime", source = "smsRequest.latestSendTime"),
      Mapping(target = "tariff", source = "tariff"),
      Mapping(target = "ticketId", source = "ticketId")
  )
  abstract fun mapResponse(smsRequest: SmsRequest, ticketId: String, created: Instant, tariff: String): SmsResponse


  fun mapId(objectId: ObjectId): String {
    return objectId.toString()
  }

  fun mapConsumerSmsResponse(ticketId: String, smsList: List<Sms>): ConsumerSmsResponse {
    return ConsumerSmsResponse(ticketId = ticketId, smsList = mapConsumerSmsList(smsList))
  }

  abstract fun mapConsumerSmsList(smsList: List<Sms>): List<ConsumerSmsDto>

  abstract fun mapConsumerSmsList(smsList: Sms): ConsumerSmsDto

  abstract fun mapListDomainToDto(smsList: List<Sms>): List<SmsDto>
  abstract fun mapDomainToDto(smsList: Sms): SmsDto

}