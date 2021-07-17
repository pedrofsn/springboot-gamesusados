package br.com.jogosusados.controller

import br.com.jogosusados.model.GameAnnouncement
import br.com.jogosusados.payload.GameAnnouncementDTO
import br.com.jogosusados.payload.GameWithAnnouncementDTO
import br.com.jogosusados.repository.GameAnnouncementRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("announcements")
class GamesAnnouncementsController {

    @Autowired
    lateinit var gamesAnnouncementsRepository: GameAnnouncementRepository

    @GetMapping("/{id}")
    fun getDetail(@PathVariable id: Long): Optional<GameAnnouncementDTO> {
        val game = gamesAnnouncementsRepository.findById(id).get().toDTO()
        return Optional.ofNullable(game)
    }

    @GetMapping("game/{idGame}")
    fun getList(@PathVariable idGame: Long): Optional<GameWithAnnouncementDTO> {
        val announcements: List<GameAnnouncement> = gamesAnnouncementsRepository.findByGameId(idGame)
        val gameDTO = announcements.first().game.toDTO()
        val announcementsDTO = announcements.map { it.toAnnouncementDTO() }
        val response = GameWithAnnouncementDTO(game = gameDTO, announcements = announcementsDTO)
        return Optional.ofNullable(response)
    }

}