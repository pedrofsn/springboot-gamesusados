package br.com.jogosusados.controller

import br.com.jogosusados.model.user.*
import br.com.jogosusados.payload.LoggedDTO
import br.com.jogosusados.payload.ProfileDTO
import br.com.jogosusados.payload.UserPOST
import br.com.jogosusados.repository.UserRepository
import br.com.jogosusados.security.TokenService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest
import javax.validation.Valid

@RestController
@RequestMapping("users")
@CrossOrigin(origins = ["*"])
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
    ): ResponseEntity<LoggedDTO> {
        val selfCreated = form.email
        return registerUser(user = form.createUserWithPassword(type = Regular, createdBy = selfCreated))
    }

    @GetMapping
    fun getUsers(@AuthenticationPrincipal userDetails: UserDetails): List<ProfileDTO> {
        val user = usersRepository.getUser(userDetails)
        val users = usersRepository.findByTypeIsNot(user.type).map { it.toProfileDTO() }

        return when {
            user.isManager() -> users.filterNot { it.type == Admin.typeName }
            user.isAdmin() -> users
            else -> emptyList()
        }
    }

    @PostMapping("/register/manager")
    fun registerUserManager(
        @AuthenticationPrincipal userDetails: UserDetails,
        @RequestBody @Valid form: UserPOST,
        request: HttpServletRequest
    ): ResponseEntity<LoggedDTO> {
        return usersRepository.getUser(userDetails)
            .takeIf { it.isAdmin() }
            ?.let { registerUser(user = form.createUserWithPassword(type = Manager, createdBy = it.email)) }
            ?: ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
    }

    private fun UserPOST.createUserWithPassword(type: UserType, createdBy: String): User {
        val passwordEncrypted = passwordEncoder.encode(password)
        return toEntity(type, passwordEncrypted, createdBy)
    }

    private fun registerUser(user: User): ResponseEntity<LoggedDTO> {
        return usersRepository.save(user).toUsernamePasswordAuthenticationToken()
            .let { tokenService.createToken(it) }
            .let { ResponseEntity.ok(LoggedDTO(it, user.type.typeName)) }
    }

    @GetMapping("my-profile")
    fun getMyProfile(@AuthenticationPrincipal userDetails: UserDetails): ProfileDTO {
        val user = usersRepository.getUser(userDetails)
        val fileName = imageUtilities.addExtension(user.id.toString())
        val image = imageUtilities.createImageURL(fileName, "my-profile")
        val imageURL = image?.toString()
        return user.toProfileDTO().copy(image = imageURL)
    }
}