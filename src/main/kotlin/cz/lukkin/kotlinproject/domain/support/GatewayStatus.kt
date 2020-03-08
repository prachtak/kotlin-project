package cz.lukkin.kotlinproject.domain.support

data class GatewayStatus(
    var primary: GatewayStatusType? = null,
    var secondary: GatewayStatusType? = null
)
