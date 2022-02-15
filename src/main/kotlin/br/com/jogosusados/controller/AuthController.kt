package br.com.jogosusados.controller

import br.com.jogosusados.error.LoginException
import br.com.jogosusados.model.user.toUsernamePasswordAuthenticationToken
import br.com.jogosusados.payload.LoggedDTO
import br.com.jogosusados.payload.LoginPOST
import br.com.jogosusados.security.TokenService
import io.jsonwebtoken.impl.DefaultClaims
import javax.servlet.http.HttpServletRequest
import javax.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.AuthenticationException
import org.springframework.web.bind.annotation.GetMapping
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
    fun handleLogin(@RequestBody form: @Valid LoginPOST?): ResponseEntity<LoggedDTO> = try {
        form?.toUsernamePasswordAuthenticationToken().login()
    } catch (e: AuthenticationException) {
        throw LoginException()
    }

    private fun UsernamePasswordAuthenticationToken?.login(): ResponseEntity<LoggedDTO> {
        val authentication = authManager.authenticate(this)
        val token: String = tokenService.createToken(authentication)
        return ResponseEntity.ok(LoggedDTO(token))
    }

    @GetMapping("refreshtoken")
    @Throws(Exception::class)
    fun refreshToken(request: HttpServletRequest): ResponseEntity<LoggedDTO>? {
        // From the HttpRequest get the claims
        val claims = request.getAttribute("claims") as DefaultClaims
        val expectedMap = getMapFromIoJsonwebtokenClaims(claims)
        val token: String = tokenService.doGenerateRefreshToken(expectedMap, expectedMap["sub"].toString())
        return ResponseEntity.ok(LoggedDTO(token))
    }

    fun getMapFromIoJsonwebtokenClaims(claims: DefaultClaims): Map<String, Any> {
        val result = hashMapOf<String, Any>()
        claims.forEach { entry, value -> result[entry] = value }
        return result
    }
}