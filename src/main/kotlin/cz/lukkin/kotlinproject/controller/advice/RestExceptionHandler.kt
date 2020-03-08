package cz.lukkin.kotlinproject.controller.advice

import cz.lukkin.kotlinproject.exception.KotlinProjectException
import cz.lukkin.kotlinproject.exception.impl.AlreadyExistsException
import cz.lukkin.kotlinproject.exception.impl.BadGatewayException
import cz.lukkin.kotlinproject.exception.impl.BadRequestException
import cz.lukkin.kotlinproject.exception.impl.NotFoundException
import cz.lukkin.kotlinproject.model.common.ErrorResponse
import lombok.RequiredArgsConstructor
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MissingRequestHeaderException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler


/**
 * Main application cz.lukkin.kotlinproject.exception handler.
 */
@ControllerAdvice
@RequiredArgsConstructor
class RestExceptionHandler : AbstractExceptionHandler() {

  /**
   * Handles missing headers exception - Bad Request (e.g. consumer token validation)
   */
  @ExceptionHandler(MissingRequestHeaderException::class)
  fun handle400(e: MissingRequestHeaderException): ResponseEntity<ErrorResponse> {
    return response(BAD_REQUEST, "Request header missing", e, logWarn())
  }

  /**
   * Handles HTTP 400 errors - Bad Request
   *
   * @param e [cz.lukkin.kotlinproject.exception.impl.BadRequestException]
   * @return Error common
   */
  @ExceptionHandler(BadRequestException::class)
  fun handle400(e: KotlinProjectException): ResponseEntity<ErrorResponse> {
    return response(BAD_REQUEST, e.label, e, logWarn())
  }

  /**
   * Handles HTTP 401 errors - Access Denied
   *
   * @param e [cz.lukkin.kotlinproject.exception.impl.AccessDeniedException]
   * @return Error common
   */
  @ExceptionHandler(AccessDeniedException::class)
  fun handle401(e: KotlinProjectException): ResponseEntity<ErrorResponse> {
    return response(BAD_REQUEST, e.label, e, logWarn())
  }

  /**
   * Handles HTTP 404 errors - Not Found.
   *
   * @param e [cz.lukkin.kotlinproject.exception.KotlinProjectException]
   * @return Error common
   */
  @ExceptionHandler(NotFoundException::class)
  fun handle404(e: KotlinProjectException): ResponseEntity<ErrorResponse> {
    return response(HttpStatus.NOT_FOUND, e.label, e, logWarn())
  }

  /**
   * Handles HTTP 409 errors - Conflict.
   *
   * @param e [cz.lukkin.kotlinproject.exception.KotlinProjectException]
   * @return Error common
   */
  @ExceptionHandler(AlreadyExistsException::class)
  fun handle409(e: KotlinProjectException): ResponseEntity<ErrorResponse> {
    return response(HttpStatus.CONFLICT, e.label, e, logWarn())
  }

  /**
   * Handles HTTP 502 errors - BadGateway.
   *
   * @param e [cz.lukkin.kotlinproject.exception.KotlinProjectException]
   * @return Error common
   */
  @ExceptionHandler(BadGatewayException::class)
  fun handle502(e: KotlinProjectException): ResponseEntity<ErrorResponse> {
    return response(INTERNAL_SERVER_ERROR, e.label, e, logWarn())
  }

}