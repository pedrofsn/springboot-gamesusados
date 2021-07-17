package br.com.jogosusados.payload

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken


data class LoginPOST(val email: String, val password: String) {
    fun converter() = UsernamePasswordAuthenticationToken(email, password)
}