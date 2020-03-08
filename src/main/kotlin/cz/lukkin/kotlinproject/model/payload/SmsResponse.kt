package cz.lukkin.kotlinproject.model.payload

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include
import com.github.pozo.KotlinBuilder
import java.time.Instant

@KotlinBuilder
@JsonInclude(Include.NON_NULL)
data class SmsResponse(
    val id: String,
    val recipients: List<String>,
    val email: String? = null,
    val text: String,
    val latestSendTime: Instant? = null,
    val tariff: String? = null,
    val created: Instant,
    val ticketId: String
)
