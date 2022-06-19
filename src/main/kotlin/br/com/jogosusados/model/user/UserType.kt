package br.com.jogosusados.model.user

import org.springframework.security.core.GrantedAuthority

sealed class UserType(val typeName: String) : GrantedAuthority {
    override fun getAuthority(): String = typeName

    companion object {
        fun getUsertType(typeName: String?) = when (typeName) {
            Admin.typeName -> Admin
            Manager.typeName -> Manager
            else -> Regular
        }
        object Admin : UserType("ADMIN")
        object Manager : UserType("MANAGER")
        object Regular : UserType("USER")
    }
}