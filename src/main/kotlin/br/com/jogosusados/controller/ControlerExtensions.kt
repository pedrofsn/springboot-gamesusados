package br.com.jogosusados.controller

import br.com.jogosusados.model.User
import br.com.jogosusados.repository.UserRepository
import org.springframework.http.ResponseEntity
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI

fun UserRepository.getUser(userDetails: UserDetails): User {
    val email = userDetails.username
    return findByEmail(email).get()
}

fun <T> UriComponentsBuilder.toResponseEntity(id : Long, body : T, path :String = "/{id}"): ResponseEntity<T> {
    val uri: URI = path(path).buildAndExpand(id).toUri()
    return ResponseEntity.created(uri).body(body)
}