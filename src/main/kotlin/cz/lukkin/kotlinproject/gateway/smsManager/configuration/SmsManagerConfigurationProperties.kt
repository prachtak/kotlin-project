package cz.lukkin.kotlinproject.gateway.smsManager.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("gateway.sms-manager")
data class SmsManagerConfigurationProperties(
    val url: String,
    val apiKey: String
)