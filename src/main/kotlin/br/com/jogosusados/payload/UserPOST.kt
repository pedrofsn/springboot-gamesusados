package br.com.jogosusados.payload

import br.com.jogosusados.model.user.User
import br.com.jogosusados.model.user.UserType

data class UserPOST(
    val name: String,
    val phone: String,
    val email: String,
    val password: String
) {
    fun toEntity(type: UserType, passwordEncrypted: String, createdBy: String) = User(
        id = 0,
        name = name,
        phone = phone,
        email = email,
        password = passwordEncrypted,
        type = type
    ).apply { this.createdBy = createdBy }
}