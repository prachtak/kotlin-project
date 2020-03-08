package cz.lukkin.kotlinproject.mapper

import cz.lukkin.kotlinproject.domain.Consumer
import cz.lukkin.kotlinproject.model.ConsumerDto
import cz.lukkin.kotlinproject.model.payload.ConsumerRequest
import cz.lukkin.kotlinproject.model.payload.ConsumerResponse
import org.bson.types.ObjectId
import org.mapstruct.Mapper


@Mapper
abstract class ConsumerMapper {

  fun mapToDomain(consumerRequest: ConsumerRequest): Consumer {
    return Consumer(
        name = consumerRequest.name,
        deliveryUpdateEndpoint = consumerRequest.deliveryUpdateEndpoint)
  }

  abstract fun mapConsumerResponse(consumer: Consumer): ConsumerResponse

  abstract fun mapListDomainToDto(consumers: List<Consumer>): List<ConsumerDto>
  abstract fun mapDomainToDto(consumer: Consumer): ConsumerDto

  fun mapId(objectId: ObjectId): String {
    return objectId.toString()
  }
}