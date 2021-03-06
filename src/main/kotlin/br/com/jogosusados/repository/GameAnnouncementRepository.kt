package br.com.jogosusados.repository

import br.com.jogosusados.model.GameAnnouncement
import org.springframework.data.jpa.repository.JpaRepository

interface GameAnnouncementRepository : JpaRepository<GameAnnouncement, Long> {

    fun findByGameId(idGame: Long): List<GameAnnouncement>
    fun findByGameIdAndEnabledTrue(idGame: Long): List<GameAnnouncement>
    fun findByGameIdAndEnabledFalse(idGame: Long): List<GameAnnouncement>

    fun findByOwnerId(idOwner: Long): List<GameAnnouncement>
}