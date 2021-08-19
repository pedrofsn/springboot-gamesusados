package br.com.jogosusados.controller

import br.com.jogosusados.model.user.Regular
import br.com.jogosusados.model.user.toUsernamePasswordAuthenticationToken
import br.com.jogosusados.payload.LoggedDTO
import br.com.jogosusados.payload.UserPOST
import br.com.jogosusados.repository.UserRepository
import br.com.jogosusados.security.TokenService
import javax.servlet.http.HttpServletRequest
import javax.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("users")
class UsersController {

    @Autowired
    lateinit var usersRepository: UserRepository

    @Autowired
    lateinit var tokenService: TokenService

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    @PostMapping("/register")
    fun registerUser(@RequestBody @Valid form: UserPOST, request: HttpServletRequest): ResponseEntity<LoggedDTO> {
        val passwordEncrypted = passwordEncoder.encode(form.password)
        val entity = form.toEntity(Regular, passwordEncrypted)
        return usersRepository.save(entity).toUsernamePasswordAuthenticationToken()
            .let { tokenService.createToken(it) }
            .let { ResponseEntity.ok(LoggedDTO(it)) }
    }
}