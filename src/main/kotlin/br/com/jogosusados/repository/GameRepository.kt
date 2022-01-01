package br.com.jogosusados.repository

import br.com.jogosusados.model.Game
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface GameRepository : JpaRepository<Game, Long> {
    fun findGameByTitleContainsIgnoreCase(name: String, pagination: Pageable): Page<Game>
    fun findGameByGamePlatform_Id(id: Long): List<Game>
}