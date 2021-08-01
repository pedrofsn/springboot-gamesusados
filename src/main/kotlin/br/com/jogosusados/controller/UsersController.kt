package br.com.jogosusados.controller

import br.com.jogosusados.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("users")
class UsersController {

    @Autowired
    lateinit var usersRepository: UserRepository
/*
    @GetMapping
    fun registerUser(@RequestBody @Valid form: UserPOST) : ResponseEntity<ResponseUserRegistered> {
        usersRepository.save(form.toEntity())

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