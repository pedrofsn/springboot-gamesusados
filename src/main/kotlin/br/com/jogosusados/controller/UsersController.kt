package br.com.jogosusados.controller

import br.com.jogosusados.model.user.Regular
import br.com.jogosusados.payload.LoggedDTO
import br.com.jogosusados.payload.UserPOST
import br.com.jogosusados.repository.UserRepository
import br.com.jogosusados.security.TokenService
import javax.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
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
    lateinit var authManager: AuthenticationManager

    @Autowired
    lateinit var tokenService: TokenService

    @PostMapping("/register")
    fun registerUser(@RequestBody @Valid form: UserPOST): ResponseEntity<LoggedDTO> {
        return usersRepository.save(form.toEntity(Regular)).toLogin()
            .let { authManager.authenticate(it) }
            .let { tokenService.createToken(it) }
            .let { ResponseEntity.ok(LoggedDTO(it)) }
    }

}