package cz.lukkin.kotlinproject.service.impl

import org.springframework.stereotype.Service

@Service
class GatewayServiceImpl(private val smsManager: SmsManager, private val tmobile: Tmobile, private val email: Email) : GatewayService {

  // TODO AFTER TMBOLIE IMPL
//  override fun getGateway(countryCode: Int, gateway: Gateway?): Gateway {
//    return when (countryCode) {
//      420 -> if (gateway != null || gateway == SMS_MANAGER) T_MOBILE else SMS_MANAGER
//      -1 -> EMAIL
//      else -> if (gateway != null || gateway == SMS_MANAGER) T_MOBILE else SMS_MANAGER
//    }
//  }

  override fun getGateway(countryCode: Int, gateway: Gateway?): Gateway {
    return when (countryCode) {
      420 -> SMS_MANAGER
      -1 -> EMAIL
      else -> SMS_MANAGER
    }
  }

  override fun getGatewayInstance(gateway: Gateway): GatewayAdapter {
    return when (gateway) {
      SMS_MANAGER -> smsManager
      T_MOBILE -> tmobile
      EMAIL -> email
    }
  }
}
