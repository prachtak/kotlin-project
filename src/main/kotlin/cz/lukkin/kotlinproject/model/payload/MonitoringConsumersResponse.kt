package cz.lukkin.kotlinproject.model.payload

import com.github.pozo.KotlinBuilder
import cz.lukkin.kotlinproject.model.ConsumerDto

@KotlinBuilder
data class MonitoringConsumersResponse(
    val consumers: Collection<ConsumerDto>
)