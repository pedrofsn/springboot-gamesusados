package br.com.jogosusados.controller

import br.com.jogosusados.error.LoginException
import br.com.jogosusados.model.user.User
import br.com.jogosusados.model.user.toUsernamePasswordAuthenticationToken
import br.com.jogosusados.payload.LoggedDTO
import br.com.jogosusados.payload.LoginPOST
import br.com.jogosusados.security.TokenService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.AuthenticationException
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = ["*"])
class AuthController {

    @Autowired
    lateinit var authManager: AuthenticationManager

    @Autowired
    lateinit var tokenService: TokenService

    @PostMapping
    fun handleLogin(@RequestBody form: @Valid LoginPOST?): ResponseEntity<LoggedDTO> = try {
        form?.toUsernamePasswordAuthenticationToken().login()
    } catch (e: AuthenticationException) {
        throw LoginException()
    }

    private fun UsernamePasswordAuthenticationToken?.login(): ResponseEntity<LoggedDTO> {
        val authentication = authManager.authenticate(this)
        val user = authentication.principal as User
        val usertype = user.type.typeName
        val token: String = tokenService.createToken(authentication)
        return ResponseEntity.ok()
            .header("Access-Control-Allow-Origin", "*")
            .headers {
                it.accessControlAllowOrigin = "*"
                it.contentType = MediaType.APPLICATION_JSON
            }
            .body(LoggedDTO(token, usertype))
    }
}