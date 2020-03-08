package cz.lukkin.kotlinproject.model.payload

import java.time.Instant
import javax.validation.constraints.NotEmpty

data class SmsRequest(

    val recipients: List<String>? = null,

    @field:NotEmpty
    val text: String? = null,

    var latestSendTime: Instant? = null,
    var tariff: String? = null,
    var email: String? = null
)