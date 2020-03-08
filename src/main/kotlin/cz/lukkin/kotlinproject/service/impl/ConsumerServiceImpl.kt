package cz.lukkin.kotlinproject.service.impl

import org.mapstruct.factory.Mappers
import org.springframework.stereotype.Service

@Service
class ConsumerServiceImpl(private val consumerRepository: ConsumerRepository) : ConsumerService {

  val mapper: ConsumerMapper = Mappers.getMapper(ConsumerMapper::class.java)

  override fun createConsumer(request: ConsumerRequest): ConsumerResponse {
    val consumer = consumerRepository.save(mapper.mapToDomain(request))
    return mapper.mapConsumerResponse(consumer)
  }

  override fun getConsumer(token: String): ConsumerResponse {
    hasAccess(token)
    return mapper.mapConsumerResponse(this.findConsumer(token))
  }

  override fun updateConsumer(request: ConsumerRequest, token: String): ConsumerResponse {
    hasAccess(token)
    val consumer = consumerRepository.findByToken(token)
    consumer.name = request.name
    consumer.deliveryUpdateEndpoint = request.deliveryUpdateEndpoint
    consumerRepository.save(consumer)
    return mapper.mapConsumerResponse(consumer)
  }

  override fun deleteConsumer(token: String) {
    hasAccess(token)
    val consumer = consumerRepository.findByToken(token)
    consumer.enabled = false
    consumerRepository.save(consumer)
  }

  override fun findConsumer(token: String): Consumer {
    return consumerRepository.findByToken(token)
  }

  override fun hasAccess(token: String) {
    if (!consumerRepository.existsByTokenAndEnabled(token, true)) {
      throw AccessDeniedException("Don't have access.")
    }
  }
}
