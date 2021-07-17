package br.com.jogosusados.payload

data class GameAnnouncementDTO(
    val game: GameDTO,
    val owner: OwnerDTO,
    val price: Double
)