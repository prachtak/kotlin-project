package cz.lukkin.kotlinproject.domain.support

data class GatewayStatusType(
    var gateway: Gateway?,
    var status: Int?,
    var errorMessage: String?
)
