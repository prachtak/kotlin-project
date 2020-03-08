package cz.lukkin.kotlinproject.gateway.smsManager.client.configuration

import feign.Client
import feign.Logger
import feign.codec.Decoder
import feign.codec.ErrorDecoder
import feign.form.spring.SpringFormEncoder
import org.springframework.beans.factory.ObjectFactory
import org.springframework.boot.autoconfigure.http.HttpMessageConverters
import org.springframework.cloud.openfeign.support.SpringEncoder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class FeignConfiguration {

  @Bean
  fun feignLoggerLevel(): Logger.Level {
    return Logger.Level.FULL
  }

  @Bean
  fun feignDecoder(): Decoder {
    return CustomFeignDecoder()
  }

  @Bean
  fun feignErrorDecoder(): ErrorDecoder {
    return CustomFeignErrorDecoder()
  }

  @Bean
  fun feignEncoder(converters: ObjectFactory<HttpMessageConverters>): SpringFormEncoder {
    return SpringFormEncoder(SpringEncoder(converters))
  }

  @Bean
  fun feignClient(): Client? {
    return Client.Default(null, null)
  }

}