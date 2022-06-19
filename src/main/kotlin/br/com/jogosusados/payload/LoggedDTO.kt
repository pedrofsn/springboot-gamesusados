package br.com.jogosusados.payload

class LoggedDTO(
    val token: String,
    val usertype: String,
    val type: String = "Bearer"
)