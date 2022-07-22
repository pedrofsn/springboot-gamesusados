package br.com.jogosusados.repository

import br.com.jogosusados.model.GamePlatform
import org.springframework.data.jpa.repository.JpaRepository

interface GamePlatformRepository : JpaRepository<GamePlatform, Long> {
    fun findGamePlatformByName(name: String): GamePlatform?
}