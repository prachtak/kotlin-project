package cz.lukkin.kotlinproject.model

import com.github.pozo.KotlinBuilder

import java.time.Instant

@KotlinBuilder
data class ConsumerSmsDto(
    val number: String,
    val message: String,
    val tariff: Tariff,
    val status: SmsStatus,
    val gateway: Gateway,
    val created: Instant,
    var sentTime: Instant? = null
)