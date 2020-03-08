package cz.lukkin.kotlinproject.gateway.smsManager.model.payload

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText

@JacksonXmlRootElement(localName = "RequestDocument")
class SmsManagerRequest(requestHeader: RequestHeader, requestList: List<Request>) {

  @JacksonXmlProperty(localName = "RequestHeader")
  val requestHeader: RequestHeader = requestHeader

  @JacksonXmlElementWrapper(localName = "RequestList")
  @JacksonXmlProperty(localName = "Request")
  val requestList: Collection<Request> = requestList

  class Request(
      @field:JacksonXmlProperty(localName = "Type", isAttribute = true) val tariff: SmsManagerTariff,
      @field:JacksonXmlProperty(localName = "Message") val message: Message, number: List<Number>) {
    @JacksonXmlElementWrapper(localName = "NumbersList")
    @JacksonXmlProperty(localName = "Number")
    val numbersList: Collection<Number> = number
  }

  class RequestHeader(@field:JacksonXmlProperty(localName = "Apikey") val apiKey: String)

  class Message(@JacksonXmlText val message: String) {
    @JacksonXmlProperty(localName = "Type", isAttribute = true)
    var type: Type = Type.Text

    @JacksonXmlRootElement(namespace = "message")
    enum class Type {
      Text
    }
  }

  class Number(@JacksonXmlText val number: String)

}