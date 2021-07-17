package br.com.jogosusados.repository

import br.com.jogosusados.model.Game
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface GameRepository : JpaRepository<Game, Long> {
    fun findGameByTitleLikeIgnoreCase(name: String, pagination: Pageable): Page<Game>
}