package br.com.jogosusados.model

import br.com.jogosusados.model.user.User
import br.com.jogosusados.payload.AnnouncementDTO
import br.com.jogosusados.payload.GameAnnouncementDTO
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.ManyToOne


@Entity
data class GameAnnouncement(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    var price: Double,

    @ManyToOne(fetch = FetchType.LAZY)
    val owner: User,

    @ManyToOne(fetch = FetchType.LAZY)
    var game: Game,

    var enabled : Boolean = false
) : Metadata() {

    fun toDTO(image: String? = null) = GameAnnouncementDTO(
        id = id,
        price = price,
        game = game.toDTO(image),
        owner = owner.toDTO()
    )
    fun toAnnouncementDTO() = AnnouncementDTO(
        id = id,
        price = price,
        owner = owner.toDTO()
    )

}