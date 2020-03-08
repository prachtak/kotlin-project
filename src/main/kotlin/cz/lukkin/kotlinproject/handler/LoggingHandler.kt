package cz.lukkin.kotlinproject.handler

import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.util.ContentCachingResponseWrapper
import java.io.*
import java.nio.charset.Charset
import java.time.Duration
import java.time.LocalDateTime
import java.time.temporal.Temporal
import java.util.*
import java.util.stream.Collectors
import javax.servlet.FilterChain
import javax.servlet.ReadListener
import javax.servlet.ServletException
import javax.servlet.ServletInputStream
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletRequestWrapper
import javax.servlet.http.HttpServletResponse

/**
 * Logging handler Filter
 */
class LoggingHandler : OncePerRequestFilter() {
  /**
   * Log each request and response with full Request URI, content payload and duration of the request in ms.
   * @param request the request
   * @param response the response
   * @param filterChain chain of filters
   * @throws ServletException
   * @throws IOException
   */
  @Throws(ServletException::class, IOException::class)
  override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
    val path = request.requestURI.substring(request.contextPath.length).replace("[/]+$".toRegex(), "")
    if (EXCLUDED_PATHS.contains(path)) {
      filterChain.doFilter(request, response)
      return
    }
    val reqInfo = StringBuffer()
        .append("[").append(request.method).append("]")
        .append(" url = ")
        .append(request.requestURL)
    val queryString = request.queryString
    if (queryString != null) {
      reqInfo.append("?").append(queryString)
    }
    reqInfo.append(LINE_SEPARATOR)
    logger.info("Calling rest interface $reqInfo")
    val requestHeaders = Collections.list(request.headerNames)
        .stream()
        .collect(Collectors.toMap({ h: String? -> h }) { name: String? -> request.getHeader(name) })
    logger.info("Request headers: $requestHeaders")
    val wrappedRequest = MultiReadHttpServletRequest(request)
    val wrappedResponse = ContentCachingResponseWrapper(response)
    val requestBody = wrappedRequest.contentAsString
    if (requestBody.length > 0) {
      logger.info(String.format("Request body: %s", requestBody))
    }
    val startTime: Temporal = LocalDateTime.now()
    // perform action
    filterChain.doFilter(wrappedRequest, wrappedResponse)
    val endTime: Temporal = LocalDateTime.now()
    val executionTime = Duration.between(startTime, endTime)
    logger.info("Returning from the rest interface " + reqInfo + " with status = " + response.status + " in " + executionTime.toMillis() + "ms")
    val contentDisposition = response.getHeader("Content-Disposition")
    if (contentDisposition != null && contentDisposition.contains("attachment")) {
      val fileName = contentDisposition.substring(contentDisposition.lastIndexOf("filename"))
      logger.info(String.format("Response body: with %s  and size=%s bytes", fileName, wrappedResponse.contentSize))
    } else {
      val buf = wrappedResponse.contentAsByteArray
      logger.info(String.format("Response body: %s ", getContentAsString(buf, response.characterEncoding)))
    }
    wrappedResponse.copyBodyToResponse() // IMPORTANT: copy content of response back into original response
    val responseHeaders = response.headerNames
        .stream()
        .collect(Collectors.toMap({ h: String? -> h }) { name: String? -> response.getHeader(name) })
    logger.info("Response headers: $responseHeaders")
  }

  private fun getContentAsString(buf: ByteArray?, charsetName: String): String {
    return if (buf == null || buf.size == 0) "" else try {
      String(buf, 0, buf.size, Charset.forName(charsetName))
    } catch (ex: UnsupportedEncodingException) {
      "Unsupported Encoding"
    }
  }

  private inner class MultiReadHttpServletRequest internal constructor(request: HttpServletRequest) : HttpServletRequestWrapper(request) {
    private val content: ByteArray
    private val servletInputStream: ServletInputStream
    val contentAsString: String
      get() = String(content)

    @Throws(IOException::class)
    override fun getInputStream(): ServletInputStream {
      val byteArrayInputStream = ByteArrayInputStream(content)
      return object : ServletInputStream() {
        override fun isFinished(): Boolean {
          return servletInputStream.isFinished
        }

        override fun isReady(): Boolean {
          return servletInputStream.isReady
        }

        override fun setReadListener(listener: ReadListener) {
          servletInputStream.setReadListener(listener)
        }

        @Throws(IOException::class)
        override fun read(): Int {
          return byteArrayInputStream.read()
        }
      }
    }

    @Throws(IOException::class)
    override fun getReader(): BufferedReader {
      return BufferedReader(InputStreamReader(this.inputStream))
    }

    init {
      servletInputStream = request.inputStream
      content = BufferedReader(InputStreamReader(servletInputStream)).lines().collect(Collectors.joining()).toByteArray()
    }
  }

  companion object {
    private val LINE_SEPARATOR = System.lineSeparator()
    private val EXCLUDED_PATHS = setOf(Mappings.BONJOUR)
  }
}