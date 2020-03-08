package cz.lukkin.kotlinproject.controller

import cz.lukkin.kotlinproject.model.payload.ConsumerSmsResponse
import cz.lukkin.kotlinproject.model.payload.SmsRequest
import cz.lukkin.kotlinproject.model.payload.SmsResponse
import cz.lukkin.kotlinproject.service.ConsumerService
import cz.lukkin.kotlinproject.service.SmsService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import javax.validation.Valid


@RestController
@RequestMapping("/sms")
class SmsController(
    private val smsService: SmsService,
    private val consumerService: ConsumerService) {


  @PostMapping
  @ResponseStatus(HttpStatus.OK)
  fun sendSms(@RequestHeader(Headers.CONSUMER_TOKEN) token: String, @Valid @RequestBody smsRequest: SmsRequest): SmsResponse {
    consumerService.hasAccess(token)
    val consumer = consumerService.findConsumer(token)
    return smsService.receiveSms(smsRequest, consumer)
  }

  @GetMapping("/{ticketId}")
  @ResponseStatus(HttpStatus.OK)
  fun readConsumerSms(@RequestHeader(Headers.CONSUMER_TOKEN) token: String, @PathVariable("ticketId") ticketId: String): ConsumerSmsResponse {
    consumerService.hasAccess(token)
    val consumer = consumerService.findConsumer(token)
    return smsService.getConsumerSmsByTicketId(ticketId, consumer)
  }
}