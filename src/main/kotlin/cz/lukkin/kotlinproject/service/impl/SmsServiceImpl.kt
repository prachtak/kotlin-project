package cz.lukkin.kotlinproject.service.impl

import net.javacrumbs.shedlock.core.SchedulerLock
import org.mapstruct.factory.Mappers
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.*

@Service
class SmsServiceImpl(
    private val phoneNumberService: PhoneNumberService,
    private val smsRepository: SmsRepository,
    private val gatewayService: GatewayService,
    private val cronLockerService: CronLockerService) : SmsService {

  private val PREPARE_TO_SEND_LOCK = "prepare_to_send_lock"

  companion object : Log()

  val mapper: SmsMapper = Mappers.getMapper(SmsMapper::class.java)

  override fun receiveSms(smsRequest: SmsRequest, consumer: Consumer): SmsResponse {
    val smsList: MutableList<Sms> = mutableListOf()
    val created = Instant.now()
    val ticketId = UUID.randomUUID().toString().replace("-", "")
    val tariff = mapper.mapTariff(smsRequest.tariff)

    when (smsRequest.email) {
      null ->
        smsRequest.recipients?.forEach { recipient ->
          val countryCodeAndNumberDto = phoneNumberService.getCountryCodeAndNumber(recipient)
          val gateway = gatewayService.getGateway(countryCodeAndNumberDto.countryCode, null)
          smsList.add(mapper.mapToDomain(smsRequest, countryCodeAndNumberDto, ticketId, tariff, created, SmsStatus.PREPARE_TO_SEND, gateway, consumer.id))
        }
      else -> {
        val countryCodeAndNumberDto = CountryCodeAndNumberDto(-1, smsRequest.email!!)
        smsList.add(mapper.mapToDomain(smsRequest, countryCodeAndNumberDto, ticketId, tariff, created, SmsStatus.PREPARE_TO_SEND, Gateway.EMAIL, consumer.id))
      }
    }
    smsRepository.saveAll(smsList)
    return mapper.mapResponse(smsRequest, ticketId, created, tariff.value)
  }

  override fun getConsumerSmsByTicketId(ticketId: String, consumer: Consumer): ConsumerSmsResponse {
    val smsList = smsRepository.findAllByTicketIdAndConsumerId(ticketId, consumer.id!!)
    return mapper.mapConsumerSmsResponse(ticketId, smsList)
  }

  @Scheduled(cron = "\${scheduler.send.cron}")
  @SchedulerLock(name = "prepare_to_send__lock", lockAtLeastForString = "PT1S", lockAtMostForString = "PT2S")
  fun sendNewSms() {
    log.debug("PREPARE TO SEND cron started.")
    if (!cronLockerService.isLocked(PREPARE_TO_SEND_LOCK)) {
      cronLockerService.lock(PREPARE_TO_SEND_LOCK)
      val newSms = smsRepository.findAllByStatus(SmsStatus.PREPARE_TO_SEND)
      newSms.forEach(this::sendSms)
      cronLockerService.unlock(PREPARE_TO_SEND_LOCK)
    }
  }

  private fun sendSms(sms: Sms) {
    val gateway = gatewayService.getGateway(sms.countryCode, sms.gateway)
    val sentSms = gatewayService.getGatewayInstance(gateway).send(sms)
    smsRepository.save(sentSms)
  }
}
