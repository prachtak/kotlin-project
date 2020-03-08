package cz.lukkin.kotlinproject.model

import com.github.pozo.KotlinBuilder
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@KotlinBuilder
@Document(collection = "sms")
data class SmsDto(
    val id: String,
    val countryCode: Int,
    val number: String,
    val message: String,
    val tariff: Tariff,
    val ticketId: String,
    val status: SmsStatus,
    val gateway: Gateway?,
    val gatewayStatus: GatewayStatus?,
    val created: Instant,
    val sentTime: Instant? = null,
    val gatewayRequestId: String? = null,
    val consumerId: String
)