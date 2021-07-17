package br.com.jogosusados.model

import br.com.jogosusados.payload.OwnerDTO
import org.springframework.security.core.userdetails.UserDetails
import javax.persistence.*

@Entity
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    val name: String,
    val phone: String,
    val email: String,
    private val password: String,
    @ManyToMany(fetch = FetchType.EAGER) var perfis: List<Profile>
) : UserDetails {


    override fun getAuthorities() = perfis

    override fun getPassword(): String {
        return this.password
    }

    override fun getUsername(): String = email

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = true

    fun toDTO() = OwnerDTO(
        name = name,
        phone = phone,
        email = email
    )
}