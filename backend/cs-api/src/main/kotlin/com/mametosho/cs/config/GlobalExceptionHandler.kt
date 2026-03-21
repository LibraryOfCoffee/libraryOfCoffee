package com.mametosho.cs.config

import com.mametosho.cs.presentation.dto.response.ErrorResponse
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.resource.NoResourceFoundException
import java.time.OffsetDateTime

@RestControllerAdvice
class GlobalExceptionHandler {

    private val log = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)

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
                path = request.requestURI,
            ),
        )
    }
}
