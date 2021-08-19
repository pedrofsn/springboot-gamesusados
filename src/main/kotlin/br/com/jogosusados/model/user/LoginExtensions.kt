package br.com.jogosusados.model.user

import br.com.jogosusados.payload.LoginPOST
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken

fun User.toUsernamePasswordAuthenticationToken() = UsernamePasswordAuthenticationToken(this, password, authorities)
fun LoginPOST.toUsernamePasswordAuthenticationToken() = UsernamePasswordAuthenticationToken(email, password)