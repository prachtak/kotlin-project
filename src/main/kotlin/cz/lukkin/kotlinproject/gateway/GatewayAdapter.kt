package cz.lukkin.kotlinproject.gateway

import cz.lukkin.kotlinproject.domain.Sms

abstract class GatewayAdapter {

  abstract fun send(sms: Sms): Sms
}