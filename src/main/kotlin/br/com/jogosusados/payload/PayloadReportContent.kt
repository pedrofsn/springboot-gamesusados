package br.com.jogosusados.payload

data class PayloadReportContent(
    val id: Long,
    val description: String,
    val game: GameDTO?,
    val announcement: GameAnnouncementDTO?
)