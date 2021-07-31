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
    fun handleAccessDeniedException(ex: Exception?, request: WebRequest?) = ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .body(ex?.toDTO<CustomNotFoundException>())

}