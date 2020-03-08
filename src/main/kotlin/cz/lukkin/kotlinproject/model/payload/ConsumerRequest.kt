package cz.lukkin.kotlinproject.model.payload

import com.github.pozo.KotlinBuilder

@KotlinBuilder
data class ConsumerRequest(
    val name: String,
    val deliveryUpdateEndpoint: String? = null
)