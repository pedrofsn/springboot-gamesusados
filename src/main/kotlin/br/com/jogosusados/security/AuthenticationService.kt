package br.com.jogosusados.security

import br.com.jogosusados.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class AuthenticationService : UserDetailsService {

    @Autowired
    lateinit var repository: UserRepository

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails = repository.findByEmail(username)
        .takeIf { it.isPresent }?.get() ?: throw UsernameNotFoundException("Dados inválidos!")
}