package br.com.jogosusados.repository

import br.com.jogosusados.model.user.User
import br.com.jogosusados.model.user.UserType
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface UserRepository : JpaRepository<User, Long> {
    fun findByEmail(email: String): Optional<User>
    fun findByTypeIsNot(userType: UserType): List<User>
}