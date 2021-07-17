package br.com.jogosusados.repository

import br.com.jogosusados.model.GameAnnouncement
import org.springframework.data.jpa.repository.JpaRepository

interface GameAnnouncementRepository : JpaRepository<GameAnnouncement, Long> {

    fun findByGameId(idGame: Long): List<GameAnnouncement>

    // TODO fazer mais um endpoint para -> listar des jogos de um autor

}