package cz.lukkin.kotlinproject.gateway.smsManager.client

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE
import org.springframework.http.MediaType.TEXT_XML_VALUE
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import java.net.URI

@Service
@FeignClient(name = "SmsManager", configuration = [FeignConfiguration::class])
interface SmsManagerController {

  @PostMapping(consumes = [APPLICATION_FORM_URLENCODED_VALUE], produces = [TEXT_XML_VALUE])
  fun sendSms(uri: URI, @RequestBody form: Map<String, Any?>): SmsManagerResponse
}