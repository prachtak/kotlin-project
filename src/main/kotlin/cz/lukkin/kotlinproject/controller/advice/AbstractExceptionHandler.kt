package cz.lukkin.kotlinproject.controller.advice

import com.netflix.config.validation.ValidationException
import cz.lukkin.kotlinproject.exception.DetailAware
import cz.lukkin.kotlinproject.exception.KotlinProjectException
import cz.lukkin.kotlinproject.model.common.ErrorResponse
import jdk.internal.net.http.common.Log
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.support.WebExchangeBindException
import org.springframework.web.server.ResponseStatusException
import java.util.*
import java.util.function.BiConsumer

/**
 * Base cz.lukkin.kotlinproject.smsgateway.exception handler class containing helper methods and provides an unified
 * approach how to log exceptions.
 */

abstract class AbstractExceptionHandler {

  companion object : Log()

  private val GENERIC_ERROR_DESCRIPTION = "Unexpected server error occurred."

  /**
   * Handles HTTP 500 errors - Internal Server Error. Generic handler for all unexpected exceptions.
   *
   * @param e checked or unchecked exceptions
   * @return Error common
   */
  @ExceptionHandler(Exception::class)
  fun handle500(e: Exception): ResponseEntity<ErrorResponse> {
    return if (e is KotlinProjectException) {
      response(HttpStatus.INTERNAL_SERVER_ERROR, e.label, e, logError())
    } else {
      response(HttpStatus.INTERNAL_SERVER_ERROR, GENERIC_ERROR_DESCRIPTION, e, logError())
    }
  }

  /**
   * Handles exceptions associated with specific HTTP response status codes thrown by Spring.
   */
  @ExceptionHandler(ResponseStatusException::class, HttpMessageNotReadableException::class)
  fun handle400(e: ResponseStatusException): ResponseEntity<ErrorResponse> {
    val ex = Objects.requireNonNullElse(e.rootCause, e)
    return response(e.status, ex?.message, ex, logWarn())
  }

  /**
   * Handles Spring validation errors
   */
  @ExceptionHandler(WebExchangeBindException::class)
  fun handle400(e: WebExchangeBindException): ResponseEntity<ErrorResponse> {
    val ex = ValidationException(e)
    return response(e.status, ex.label, ex, logWarn())
  }

  /**
   * Handles Spring validation errors
   */
  @ExceptionHandler(MethodArgumentNotValidException::class)
  fun handle400(e: MethodArgumentNotValidException): ResponseEntity<ErrorResponse> {
    val ex = ValidationException(e)
    return response(HttpStatus.BAD_REQUEST, ex.label, ex, logWarn())
  }

  /**
   * Handles custom validation errors
   */
  @ExceptionHandler(ValidationException::class)
  fun handle400(e: ValidationException): ResponseEntity<ErrorResponse> {
    return response(HttpStatus.BAD_REQUEST, e.label, e, logWarn())
  }

  /**
   * Returns [ResponseEntity] with [ErrorResponse]
   *
   * @param status         Http status dependeing on cz.lukkin.kotlinproject.exception
   * @param message        Fills `message` field in a common object, for SMS Gateway business exceptions,
   * [cz.lukkin.kotlinproject.exception.SmsGatewayException.getLabel] should be used to obtain value for this argument
   * @param e              Caught cz.lukkin.kotlinproject.exception
   * @param doWithResponse Consumer with purpose mainly for logging
   * @return Error object
   */
  protected fun response(status: HttpStatus, message: String?, e: Throwable?, doWithResponse: BiConsumer<ErrorResponse, Throwable>): ResponseEntity<ErrorResponse> {
    val response = ResponseEntity.status(status).body(
        ErrorResponse(message, if (e is DetailAware) (e as DetailAware).detail.orElse(e.message) else e?.message))
    doWithResponse.accept(response.body!!, e!!)
    return response
  }


  /**
   * Logs cz.lukkin.kotlinproject.exception in [org.slf4j.event.Level.WARN] level (according to
   * when stacktrace is visible only if debug level is enabled.
   *
   * @return BiConsumer
   */
  fun logWarn(): BiConsumer<ErrorResponse, Throwable> {
    return BiConsumer { r: ErrorResponse, e: Throwable ->
      log.warn("{} [UUID: {}]", e.message, r.uuid)
      log.debug(e.message, e)
    }
  }

  /**
   * Logs cz.lukkin.kotlinproject.exception in [org.slf4j.event.Level.ERROR] level (according to
   * when stacktrace is always visible.
   *
   * @return
   */
  fun logError(): BiConsumer<ErrorResponse, Throwable> {
    return BiConsumer { r: ErrorResponse, e: Throwable -> AbstractExceptionHandler.log.error("{} [UUID: {}]", e.message, r.uuid, e) }
  }
}