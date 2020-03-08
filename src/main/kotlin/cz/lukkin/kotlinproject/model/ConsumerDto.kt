package cz.lukkin.kotlinproject.model

import com.github.pozo.KotlinBuilder
import java.time.Instant
import java.util.*

@KotlinBuilder
data class ConsumerDto(
    val id: String,
    val name: String,
    val deliveryUpdateEndpoint: String? = null,
    val token: String = UUID.randomUUID().toString().replace("-", ""),
    val created: Instant = Instant.now(),
    val enabled: Boolean = true
)