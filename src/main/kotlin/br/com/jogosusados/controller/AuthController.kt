package br.com.jogosusados.controller

import br.com.jogosusados.payload.LoginPOST
import br.com.jogosusados.payload.OnLoggedInResponse
import br.com.jogosusados.security.TokenService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.AuthenticationException
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/auth")
class AuthController {

    @Autowired
    lateinit var authManager: AuthenticationManager

    @Autowired
    lateinit var tokenService: TokenService

    @PostMapping
    fun login(@RequestBody form: @Valid LoginPOST?): ResponseEntity<OnLoggedInResponse?>? {
        val formLogin = form?.converter()
        return try {
            val authentication = authManager.authenticate(formLogin)
            val token: String = tokenService.createToken(authentication)
            ResponseEntity.ok<OnLoggedInResponse?>(OnLoggedInResponse(token, "Bearer"))
        } catch (e: AuthenticationException) {
            e.printStackTrace()
            ResponseEntity.badRequest().build<OnLoggedInResponse?>()
        }
    }
}