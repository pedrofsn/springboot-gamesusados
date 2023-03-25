package br.com.jogosusados.model

import br.com.jogosusados.payload.GameDTO
import javax.persistence.*


@Entity
data class Game(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    var title: String,

    @ManyToOne(fetch = FetchType.LAZY)
    var gamePlatform: GamePlatform
) : Metadata() {
    fun toDTO(image: String? = null) = GameDTO(
        id = id,
        title = title,
        platform = gamePlatform.name,
        image = image,
        metadata = getMetadataDTO()
    )
}