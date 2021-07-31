package br.com.jogosusados.error

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest

@RestControllerAdvice
class ControllerAdvice {

    @ExceptionHandler(
        CustomNotFoundException::class,
        GameAnnouncementNotFoundException::class,
        GameNotFoundException::class,
        PlatformNotFoundException::class,
    )
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleNotFound(ex: Exception?, request: WebRequest?) = HttpStatus.NOT_FOUND.toResponse(ex)

    @ExceptionHandler(LoginException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleAccessDenied(ex: Exception?, request: WebRequest?) = HttpStatus.UNAUTHORIZED.toResponse(ex)

    private fun HttpStatus.toResponse(ex: Exception?) = ResponseEntity
        .status(this)
        .body(ex?.toDTO<CustomNotFoundException>())

}