package br.com.jogosusados.payload

data class GameDTO(
    val id: Long,
    val title: String,
    val platform: String,
    val image: String? = null
)