package br.com.jogosusados.payload

import br.com.jogosusados.model.FlagContent
import br.com.jogosusados.model.Game
import br.com.jogosusados.model.GameAnnouncement

data class PayloadRegisterFlagContent(
    val id: Long,
    val description: String,
    val type: String
) {

    fun isGame() = "game" == type
    fun isAnnouncement() = "announcement" == type

    fun toModel(game: Game? = null, announcement: GameAnnouncement? = null) = FlagContent(
        id = 0,
        description = description,
        game = game,
        announcement = announcement
    )

    private fun getTypeName() = when {
        isGame() -> "Jogo"
        isAnnouncement() -> "Anúncio"
        else -> ""
    }

    fun getSuccessMessage() = "${getTypeName()} denunciado com sucesso"

}