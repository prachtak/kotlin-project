package cz.lukkin.kotlinproject.model.payload

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include
import com.github.pozo.KotlinBuilder

@KotlinBuilder
@JsonInclude(Include.NON_NULL)
data class ConsumerResponse(
    val name: String,
    val deliveryUpdateEndpoint: String? = null,
    val token: String,
    val id: String,
    val created: String
)
