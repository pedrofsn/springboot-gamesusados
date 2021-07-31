package br.com.jogosusados.payload

data class GameAnnouncementDTO(
    val id: Long,
    val game: GameDTO,
    val owner: OwnerDTO,
    val price: Double
)