package cz.lukkin.kotlinproject.service

import org.springframework.stereotype.Service

@Service
interface GatewayService {
  fun getGateway(countryCode: Int, gateway: Gateway?): Gateway
  fun getGatewayInstance(gateway: Gateway): GatewayAdapter
}
