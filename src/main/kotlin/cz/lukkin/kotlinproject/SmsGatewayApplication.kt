package cz.lukkin.kotlinproject

import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock
import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock.InterceptMode.PROXY_METHOD
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.scheduling.annotation.EnableScheduling

@EnableFeignClients
@SpringBootApplication
@EnableScheduling
@EnableSchedulerLock(interceptMode = PROXY_METHOD, defaultLockAtMostFor = "PT30S")
@EnableConfigurationProperties(
    MetaConfigurationProperties::class,
    SmsManagerConfigurationProperties::class
)
class DrmaxSmsGatewayApplication

fun main(args: Array<String>) {
  runApplication<DrmaxSmsGatewayApplication>(*args)
}
