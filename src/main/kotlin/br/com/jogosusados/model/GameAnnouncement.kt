package br.com.jogosusados.model

import br.com.jogosusados.payload.AnnouncementDTO
import br.com.jogosusados.payload.GameAnnouncementDTO
import javax.persistence.*


@Entity
data class GameAnnouncement(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    var price: Double,

    @ManyToOne(fetch = FetchType.LAZY)
    val owner: User,

    @ManyToOne(fetch = FetchType.LAZY)
    var game: Game
) : Metadata() {

    fun toDTO() = GameAnnouncementDTO(
        price = price,
        game = game.toDTO(),
        owner = owner.toDTO()
    )

    fun toAnnouncementDTO() = AnnouncementDTO(
        id = id,
        price = price,
        owner = owner.toDTO()
    )

}