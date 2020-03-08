package cz.lukkin.kotlinproject.domain

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant
import java.util.*

@Document(collection = "consumer")
data class Consumer(
    @Id
    var id: ObjectId? = null,
    var name: String,
    var deliveryUpdateEndpoint: String? = null,
    val token: String = UUID.randomUUID().toString().replace("-", ""),
    val created: Instant = Instant.now(),
    var enabled: Boolean = true)