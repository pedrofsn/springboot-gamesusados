package br.com.jogosusados.model

import br.com.jogosusados.payload.PayloadFlagContent
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.ManyToOne


@Entity
data class FlagContent(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    var description: String,

    @ManyToOne(fetch = FetchType.LAZY)
    var game: Game?,

    @ManyToOne(fetch = FetchType.LAZY)
    var announcement: GameAnnouncement?
) : Metadata() {

    fun isGame() = game != null
    fun isAnnouncement() = announcement != null

    fun toDTO() = PayloadFlagContent(
        id = id,
        description = description,
        game = game?.toDTO(),
        announcement = announcement?.toDTO(),
    )

}