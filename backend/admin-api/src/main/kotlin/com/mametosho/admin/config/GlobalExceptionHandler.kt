package com.mametosho.admin.config

import com.mametosho.admin.application.usecase.AuthenticationException
import com.mametosho.admin.presentation.dto.response.ErrorResponse
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.dao.DuplicateKeyException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.resource.NoResourceFoundException
import java.time.OffsetDateTime

@RestControllerAdvice
class GlobalExceptionHandler {

    private val log = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)

    @ExceptionHandler(AuthenticationException::class)
    fun handleAuthentication(
        ex: AuthenticationException,
        request: HttpServletRequest,
    ): ResponseEntity<ErrorResponse> {
        log.warn("{} {}: {}", request.method, request.requestURI, ex.message)
        val status = HttpStatus.UNAUTHORIZED
        return ResponseEntity.status(status).body(
            ErrorResponse(
                timestamp = OffsetDateTime.now(),
                status = status.value(),
                error = status.reasonPhrase,
                message = ex.message,
                path = request.requestURI,
            ),
        )
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgument(
        ex: IllegalArgumentException,
        request: HttpServletRequest,
    ): ResponseEntity<ErrorResponse> {
        log.warn("{} {}: {}", request.method, request.requestURI, ex.message)
        val status = HttpStatus.BAD_REQUEST
        return ResponseEntity.status(status).body(
            ErrorResponse(
                timestamp = OffsetDateTime.now(),
                status = status.value(),
                error = status.reasonPhrase,
                message = ex.message,
                path = request.requestURI,
            ),
        )
    }

    @ExceptionHandler(DuplicateKeyException::class)
    fun handleDuplicateKey(
        ex: DuplicateKeyException,
        request: HttpServletRequest,
    ): ResponseEntity<ErrorResponse> {
        log.warn("{} {}: {}", request.method, request.requestURI, ex.message)
        val status = HttpStatus.CONFLICT
        return ResponseEntity.status(status).body(
            ErrorResponse(
                timestamp = OffsetDateTime.now(),
                status = status.value(),
                error = status.reasonPhrase,
                message = ex.message,
                path = request.requestURI,
            ),
        )
    }

    @ExceptionHandler(DataIntegrityViolationException::class)
    fun handleDataIntegrityViolation(
        ex: DataIntegrityViolationException,
        request: HttpServletRequest,
    ): ResponseEntity<ErrorResponse> {
        log.warn("{} {}: {}", request.method, request.requestURI, ex.message)
        val status = HttpStatus.CONFLICT
        return ResponseEntity.status(status).body(
            ErrorResponse(
                timestamp = OffsetDateTime.now(),
                status = status.value(),
                error = status.reasonPhrase,
                message = ex.message,
                path = request.requestURI,
            ),
        )
    }

    @ExceptionHandler(NoResourceFoundException::class)
    fun handleNoResourceFound(
        ex: NoResourceFoundException,
        request: HttpServletRequest,
    ): ResponseEntity<ErrorResponse> {
        log.warn("{} {}: {}", request.method, request.requestURI, ex.message)
        val status = HttpStatus.NOT_FOUND
        return ResponseEntity.status(status).body(
            ErrorResponse(
                timestamp = OffsetDateTime.now(),
                status = status.value(),
                error = status.reasonPhrase,
                message = ex.message,
                path = request.requestURI,
            ),
        )
    }

    @ExceptionHandler(Exception::class)
    fun handleException(
        ex: Exception,
        request: HttpServletRequest,
    ): ResponseEntity<ErrorResponse> {
        log.error("{} {}: {}", request.method, request.requestURI, ex.message, ex)
        val status = HttpStatus.INTERNAL_SERVER_ERROR
        return ResponseEntity.status(status).body(
            ErrorResponse(
                timestamp = OffsetDateTime.now(),
                status = status.value(),
                error = status.reasonPhrase,
                message = ex.message,
                path = request.requestURI,
            ),
        )
    }
}
