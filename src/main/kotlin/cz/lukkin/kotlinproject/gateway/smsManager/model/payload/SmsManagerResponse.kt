package cz.lukkin.kotlinproject.gateway.smsManager.model.payload

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText

@JsonIgnoreProperties(ignoreUnknown = true)
@JacksonXmlRootElement(localName = "Result")
class SmsManagerResponse {

  @JacksonXmlProperty(localName = "ResultHeader")
  lateinit var resultHeader: ResultHeader

  @JacksonXmlProperty(localName = "Response")
  lateinit var response: Response

  @JacksonXmlElementWrapper(localName = "ResponseRequestList")
  @JacksonXmlProperty(localName = "ResponseRequest")
  lateinit var responseRequests: Collection<ResponseRequest>

  @JsonIgnoreProperties(ignoreUnknown = true)
  class ResultHeader {

    @JacksonXmlProperty(localName = "Interface")
    lateinit var interfaceElem: String

    @JacksonXmlProperty(localName = "Version", isAttribute = true)
    lateinit var version: String

  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  class Response {

    @JacksonXmlProperty(localName = "Type", isAttribute = true)
    lateinit var status: ResponseType

    @JacksonXmlProperty(localName = "ID", isAttribute = true)
    var statusCode: Int = 0

    @JacksonXmlText
    var errorMessage: String = ""

    enum class ResponseType {
      OK, ERROR
    }
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  class ResponseRequest {

    @JacksonXmlProperty(localName = "RequestID")
    lateinit var requestId: String

    @JacksonXmlElementWrapper(localName = "ResponseNumbersList")
    @JacksonXmlProperty(localName = "Number")
    lateinit var numbers: Collection<String>
  }
}