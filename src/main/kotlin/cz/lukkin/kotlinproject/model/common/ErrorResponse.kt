package cz.lukkin.kotlinproject.model.common

import org.apache.commons.lang3.StringUtils
import org.slf4j.MDC
import java.time.Instant
import java.util.*

/**
 * Generic Error common object which is returned to a user from
 * cz.lukkin.kotliproject.exception handler.
 */

data class ErrorResponse(
    var message: String?,
    var detail: Any?,
    val timestamp: Instant,
    var uuid: String
) {
  /**
   * If UUID is not specified it tries to obtain Sleuth Trace Id from [MDC].
   * If trace id is null or empty, it generates [UUID] instead.
   *
   * @param message
   * @param detail
   */
  constructor (message: String?, detail: Any?) : this(message, detail, Instant.now(), if (StringUtils.isNotBlank(MDC.get(SLEUTH_TRACE_ID_MDC))) MDC.get(SLEUTH_TRACE_ID_MDC) else UUID.randomUUID().toString())

  companion object {
    /**
     * MDC constant, see [brave.propagation.B3Propagation.TRACE_ID_NAME]
     */
    private const val SLEUTH_TRACE_ID_MDC = "X-B3-TraceId"
  }


}
