package br.com.jogosusados.payload

data class GameWithAnnouncementDTO(
    val game: GameDTO,
    val announcements: List<AnnouncementDTO>
)