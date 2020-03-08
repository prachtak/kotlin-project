package cz.lukkin.kotlinproject.domain

import com.github.pozo.KotlinBuilder
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@KotlinBuilder
@Document(collection = "sms")
data class Sms(

    @Id
    val id: ObjectId? = null,
    val countryCode: Int,
    val number: String,
    val message: String,
    val tariff: Tariff,
    val ticketId: String,

    @Indexed
    var status: SmsStatus,

    @Indexed
    var gateway: Gateway?,
    var gatewayStatus: GatewayStatus?,
    val created: Instant,
    var sentTime: Instant? = null,

    @Indexed
    var gatewayRequestId: String? = null,

    @Indexed
    val consumerId: ObjectId
)