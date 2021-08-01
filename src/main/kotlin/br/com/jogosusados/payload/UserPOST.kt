package br.com.jogosusados.payload

import br.com.jogosusados.model.user.Regular
import br.com.jogosusados.model.user.User

data class UserPOST(
    val name: String,
    val phone: String,
    val email: String,
    val password: String
) {
    fun toEntity() = User(
        id = 0,
        name = name,
        phone = phone,
        email = email,
        password = password,
        type = Regular
    )
}