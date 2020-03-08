package cz.lukkin.kotlinproject.service

import org.springframework.stereotype.Service

@Service
interface SmsService {

  fun receiveSms(smsRequest: SmsRequest, consumer: Consumer): SmsResponse
  fun getConsumerSmsByTicketId(ticketId: String, consumer: Consumer): ConsumerSmsResponse
}
