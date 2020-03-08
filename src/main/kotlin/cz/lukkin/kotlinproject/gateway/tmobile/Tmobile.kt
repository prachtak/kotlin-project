package cz.lukkin.kotlinproject.gateway.tmobile

import cz.lukkin.kotlinproject.domain.Sms
import cz.lukkin.kotlinproject.gateway.GatewayAdapter
import org.springframework.stereotype.Component

@Component
class Tmobile : GatewayAdapter() {

  override fun send(sms: Sms): Sms {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }
}