package br.com.jogosusados.controller

import br.com.jogosusados.model.user.Manager
import br.com.jogosusados.model.user.Regular
import br.com.jogosusados.model.user.UserType
import br.com.jogosusados.model.user.toUsernamePasswordAuthenticationToken
import br.com.jogosusados.payload.LoggedDTO
import br.com.jogosusados.payload.ProfileDTO
import br.com.jogosusados.payload.UserPOST
import br.com.jogosusados.repository.UserRepository
import br.com.jogosusados.security.TokenService
import javax.servlet.http.HttpServletRequest
import javax.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.GetMapping
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

    @Autowired
    lateinit var imageUtilities: ImageUtilities

    @PostMapping("/register")
    fun registerUserRegular(
        @RequestBody @Valid form: UserPOST,
        request: HttpServletRequest
    ) = form.createUser(Regular)

    @PostMapping("/register/manager")
    fun registerUserManager(
        @AuthenticationPrincipal userDetails: UserDetails,
        @RequestBody @Valid form: UserPOST,
        request: HttpServletRequest
    ): ResponseEntity<LoggedDTO> = usersRepository.getUser(userDetails)
        .takeIf { it.isAdmin() }
        ?.let { form.createUser(Manager) }
        ?: ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()

    private fun UserPOST.createUser(type: UserType): ResponseEntity<LoggedDTO> {
        val passwordEncrypted = passwordEncoder.encode(password)
        val entity = toEntity(type, passwordEncrypted)
        return usersRepository.save(entity).toUsernamePasswordAuthenticationToken()
            .let { tokenService.createToken(it) }
            .let { ResponseEntity.ok(LoggedDTO(it)) }
    }

    @GetMapping("my-profile")
    fun getMyProfile(@AuthenticationPrincipal userDetails: UserDetails): ProfileDTO {
        val user = usersRepository.getUser(userDetails)
        val fileName = imageUtilities.addExtension(user.id.toString())
        val image = imageUtilities.createImageURL(fileName, "my-profile")
        return user.toProfileDTO().copy(image = image.toString())
    }
}