package br.com.jogosusados.repository

import br.com.jogosusados.model.GameAnnouncement
import org.springframework.data.jpa.repository.JpaRepository

interface GameAnnouncementRepository : JpaRepository<GameAnnouncement, Long> {

    fun findByGameId(idGame: Long): List<GameAnnouncement>
    fun findByGameIdAndEnabledTrue(idGame: Long): List<GameAnnouncement>
    fun findByEnabledFalse(): List<GameAnnouncement>
    fun findByEnabledTrue(): List<GameAnnouncement>
    fun findByOwnerId(idOwner: Long): List<GameAnnouncement>
}