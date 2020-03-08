package cz.lukkin.kotlinproject.service.impl

import org.mapstruct.factory.Mappers
import org.springframework.stereotype.Service

@Service
class MonitoringServiceImpl(private val smsRepository: SmsRepository, private val consumerRepository: ConsumerRepository) : MonitoringService {

  val smsMapper: SmsMapper = Mappers.getMapper(SmsMapper::class.java)
  val consumerMapper: ConsumerMapper = Mappers.getMapper(ConsumerMapper::class.java)

  override fun getAllSms(): MonitoringSmsResponse {
    return MonitoringSmsResponse(smsMapper.mapListDomainToDto(smsRepository.findAll()))
  }

  override fun getAllConsumers(): MonitoringConsumersResponse {
    return MonitoringConsumersResponse(consumerMapper.mapListDomainToDto(consumerRepository.findAll()))
  }

  override fun getBonjourResponseData(): BonjourResponseData {
    return ApplicationMonitoringHolder.bonjourResponseData
  }
}
