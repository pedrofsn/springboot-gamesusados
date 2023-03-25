package br.com.jogosusados.model

import br.com.jogosusados.model.user.User
import br.com.jogosusados.payload.AnnouncementDTO
import br.com.jogosusados.payload.GameAnnouncementDTO
import br.com.jogosusados.payload.GameDTO
import br.com.jogosusados.payload.OwnerDTO
import java.text.NumberFormat
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.ManyToOne
import java.util.Locale


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

    var enabled: Boolean = false
) : Metadata() {

    fun toDTO(gameDTO: GameDTO, ownerDTO: OwnerDTO) = GameAnnouncementDTO(
        id = id,
        price = price,
        game = gameDTO,
        owner = ownerDTO,
        enabled = enabled,
        priceMasked = getPriceMasked(price),
        metadata = getMetadataDTO()
    )

    fun toDTO() = GameAnnouncementDTO(
        id = id,
        price = price,
        game = game.toDTO(),
        owner = owner.toDTO(),
        enabled = enabled,
        priceMasked = getPriceMasked(price),
        metadata = getMetadataDTO()
    )

    fun toAnnouncementDTO() = AnnouncementDTO(
        id = id,
        price = price,
        owner = owner.toDTO()
    )

    private fun getPriceMasked(price: Double): String {
        val localePtBr = Locale("pt", "BR")
        val formatter = NumberFormat.getCurrencyInstance(localePtBr)
        return formatter.format(price)
    }

}