package br.com.jogosusados.model

import br.com.jogosusados.payload.PayloadReportContent
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.ManyToOne


@Entity
data class ReportContent(
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

    fun toDTO() = PayloadReportContent(
        id = id,
        description = description,
        game = game?.toDTO(),
        announcement = announcement?.toDTO(),
    )

}