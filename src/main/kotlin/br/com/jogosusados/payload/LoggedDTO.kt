package br.com.jogosusados.payload


private const val TYPE = "Bearer"

class LoggedDTO(val token: String, val type: String = TYPE)