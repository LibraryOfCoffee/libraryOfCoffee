package com.mametosho.admin.config

import com.mametosho.admin.application.usecase.AuthenticationException
import com.mametosho.admin.presentation.dto.response.ErrorResponse
import jakarta.servlet.http.HttpServletRequest
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

    @ExceptionHandler(AuthenticationException::class)
    fun handleAuthentication(
        @Suppress("unused") ex: AuthenticationException,
        request: HttpServletRequest,
    ): ResponseEntity<ErrorResponse> {
        val status = HttpStatus.UNAUTHORIZED
        return ResponseEntity.status(status).body(
            ErrorResponse(
                timestamp = OffsetDateTime.now(),
                status = status.value(),
                error = status.reasonPhrase,
                path = request.requestURI,
            ),
        )
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgument(
        @Suppress("unused") ex: IllegalArgumentException,
        request: HttpServletRequest,
    ): ResponseEntity<ErrorResponse> {
        val status = HttpStatus.BAD_REQUEST
        return ResponseEntity.status(status).body(
            ErrorResponse(
                timestamp = OffsetDateTime.now(),
                status = status.value(),
                error = status.reasonPhrase,
                path = request.requestURI,
            ),
        )
    }

    @ExceptionHandler(DuplicateKeyException::class)
    fun handleDuplicateKey(
        @Suppress("unused") ex: DuplicateKeyException,
        request: HttpServletRequest,
    ): ResponseEntity<ErrorResponse> {
        val status = HttpStatus.CONFLICT
        return ResponseEntity.status(status).body(
            ErrorResponse(
                timestamp = OffsetDateTime.now(),
                status = status.value(),
                error = status.reasonPhrase,
                path = request.requestURI,
            ),
        )
    }

    @ExceptionHandler(DataIntegrityViolationException::class)
    fun handleDataIntegrityViolation(
        @Suppress("unused") ex: DataIntegrityViolationException,
        request: HttpServletRequest,
    ): ResponseEntity<ErrorResponse> {
        val status = HttpStatus.CONFLICT
        return ResponseEntity.status(status).body(
            ErrorResponse(
                timestamp = OffsetDateTime.now(),
                status = status.value(),
                error = status.reasonPhrase,
                path = request.requestURI,
            ),
        )
    }

    @ExceptionHandler(NoResourceFoundException::class)
    fun handleNoResourceFound(
        @Suppress("unused") ex: NoResourceFoundException,
        request: HttpServletRequest,
    ): ResponseEntity<ErrorResponse> {
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
        @Suppress("unused") ex: Exception,
        request: HttpServletRequest,
    ): ResponseEntity<ErrorResponse> {
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
