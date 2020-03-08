package cz.lukkin.kotlinproject.service

import org.springframework.stereotype.Service

@Service
interface MonitoringService {
  fun getBonjourResponseData(): BonjourResponseData
  fun getAllSms(): MonitoringSmsResponse
  fun getAllConsumers(): MonitoringConsumersResponse
}
