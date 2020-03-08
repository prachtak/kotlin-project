package cz.lukkin.kotlinproject.gateway.email

import cz.lukkin.kotlinproject.domain.Sms
import cz.lukkin.kotlinproject.gateway.GatewayAdapter
import cz.lukkin.kotlinproject.logger.Log
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Component
import java.time.Instant

@Component
class Email(private val javaMailSender: JavaMailSender) : GatewayAdapter() {

  companion object : Log()

  override fun send(sms: Sms): Sms {
    try {
      javaMailSender.send(createMailMessage(sms.number, sms.message))
      sms.status = SmsStatus.DELIVERED
    } catch (ex: Exception) {
      log.warn("Error while sending test email:{}", ex.message)
      val primary = GatewayStatusType(gateway = sms.gateway, status = -1, errorMessage = ex.message)
      sms.gatewayStatus = GatewayStatus(primary, null)
      sms.status = SmsStatus.NOT_DELIVERED
    }
    sms.sentTime = Instant.now()

    return sms
  }

  private fun createMailMessage(toEmail: String, message: String): SimpleMailMessage? {
    val mailMessage = SimpleMailMessage()
    mailMessage.setTo(toEmail)
    mailMessage.setSubject("SMS-GATEWAY: Test email")
    mailMessage.setText(message)
    return mailMessage
  }
}