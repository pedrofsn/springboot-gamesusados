package br.com.jogosusados.payload

data class AnnouncementDTO(
    val id: Long,
    val owner: OwnerDTO,
    val price: Double
)