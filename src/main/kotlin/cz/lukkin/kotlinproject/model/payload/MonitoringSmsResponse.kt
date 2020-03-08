package cz.lukkin.kotlinproject.model.payload

import com.github.pozo.KotlinBuilder
import cz.lukkin.kotlinproject.model.SmsDto

@KotlinBuilder
data class MonitoringSmsResponse(
    val smsList: Collection<SmsDto>
)