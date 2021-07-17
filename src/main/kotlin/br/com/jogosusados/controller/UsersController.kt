package br.com.jogosusados.controller

import br.com.jogosusados.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI
import javax.validation.Valid

@RestController
@RequestMapping("users")
class UsersController {

    @Autowired
    lateinit var usersRepository: UserRepository
/*
    @GetMapping
    fun registerUser(
        @AuthenticationPrincipal userDetails : UserDetails,
        @RequestBody @Valid form: TopicoForm,
        uriBuilder: UriComponentsBuilder
    ) : ResponseEntity<ResponseUserRegistered> {
        val email = userDetails.username
        val user = usersRepository.findByEmail(email).get()
        return form.converter(cursoRepository, user)?.let { topico ->
            val newTopico = topicoRepository.save(topico)
            val uri: URI = uriBuilder.path("/topicos/{id}").buildAndExpand(newTopico.id).toUri()
            ResponseEntity.created(uri).body(TopicoDto.converter(newTopico))
        } ?: ResponseEntity.notFound().build();
    }
*/
}
