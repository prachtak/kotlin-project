package cz.lukkin.kotlinproject.model.payload

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include
import com.github.pozo.KotlinBuilder
import cz.lukkin.kotlinproject.model.ConsumerSmsDto

@KotlinBuilder
@JsonInclude(Include.NON_NULL)
data class ConsumerSmsResponse(
    val ticketId: String,
    val smsList: List<ConsumerSmsDto>
)
