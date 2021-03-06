package br.com.jogosusados.model.user

import br.com.jogosusados.payload.OwnerDTO
import br.com.jogosusados.payload.ProfileDTO
import javax.persistence.Convert
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import org.springframework.security.core.userdetails.UserDetails

@Entity
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    var name: String,
    var phone: String,
    var email: String,
    private var password: String,
    @Convert(converter = UserTypeConverter::class)
    val type: UserType
) : UserDetails {

    override fun getAuthorities() = listOf(type)
    override fun getPassword(): String = password
    override fun getUsername(): String = email

    override fun isAccountNonExpired(): Boolean = true
    override fun isAccountNonLocked(): Boolean = true
    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = true

    fun isAdmin() = authorities.contains(Admin)
    fun isManager() = authorities.contains(Manager)
    fun isRegular() = authorities.contains(Regular)

    fun toDTO() = OwnerDTO(
        name = name,
        phone = phone,
        email = email
    )

    fun toProfileDTO() = ProfileDTO(
        name = name,
        phone = phone,
        email = email,
        type = type.typeName
    )
}

