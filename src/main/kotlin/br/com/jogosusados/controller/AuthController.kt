package br.com.jogosusados.controller

import br.com.jogosusados.error.LoginException
import br.com.jogosusados.payload.LoggedDTO
import br.com.jogosusados.payload.LoginPOST
import br.com.jogosusados.security.TokenService
import javax.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.AuthenticationException
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController {

    @Autowired
    lateinit var authManager: AuthenticationManager

    @Autowired
    lateinit var tokenService: TokenService

    @PostMapping
    fun login(@RequestBody form: @Valid LoginPOST?): ResponseEntity<LoggedDTO> = try {
        val login = form?.converter()
        val authentication = authManager.authenticate(login)
        val token: String = tokenService.createToken(authentication)
        ResponseEntity.ok(LoggedDTO(token))
    } catch (e: AuthenticationException) {
        throw LoginException()
    }
}