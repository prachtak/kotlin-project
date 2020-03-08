package cz.lukkin.kotlinproject.service

import org.springframework.stereotype.Service

@Service
interface ConsumerService {
  fun createConsumer(request: ConsumerRequest): ConsumerResponse
  fun hasAccess(token: String)
  fun getConsumer(token: String): ConsumerResponse
  fun findConsumer(token: String): Consumer
  fun updateConsumer(request: ConsumerRequest, token: String): ConsumerResponse
  fun deleteConsumer(token: String)
}
