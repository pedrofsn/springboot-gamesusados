package br.com.jogosusados.model

import br.com.jogosusados.payload.GamePlatformDTO
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class GamePlatform(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    val name: String
) {
    fun toDTO() = GamePlatformDTO(id = id, title = name)
}