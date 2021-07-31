package br.com.jogosusados.controller

import br.com.jogosusados.error.NotFoundException
import br.com.jogosusados.extensions.getUser
import br.com.jogosusados.extensions.toResponseEntity
import br.com.jogosusados.model.GameAnnouncement
import br.com.jogosusados.payload.GameAnnouncementDTO
import br.com.jogosusados.payload.GameWithAnnouncementDTO
import br.com.jogosusados.repository.GameAnnouncementRepository
import br.com.jogosusados.repository.GameRepository
import br.com.jogosusados.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder


@RestController
@RequestMapping("announcements")
class GamesAnnouncementsController {

    @Autowired
    lateinit var gamesAnnouncementsRepository: GameAnnouncementRepository

    @Autowired
    lateinit var gameRepository: GameRepository

    @Autowired
    lateinit var usersRepository: UserRepository

    @GetMapping("/{id}")
    fun getDetail(@PathVariable id: Long): ResponseEntity<GameAnnouncementDTO> {
        try {
            val game = gamesAnnouncementsRepository.findById(id).get().toDTO()
            return ResponseEntity.ok(game)
        } catch (exception : NoSuchElementException) {
            throw NotFoundException()
        }
    }

    @GetMapping("game/{idGame}")
    fun getList(@PathVariable idGame: Long): ResponseEntity<GameWithAnnouncementDTO> {
        val announcements: List<GameAnnouncement> = gamesAnnouncementsRepository.findByGameId(idGame)
        val gameDTO = announcements.firstOrNull()?.game?.toDTO() ?: throw NotFoundException()
        val announcementsDTO = announcements.map { it.toAnnouncementDTO() }
        val response = GameWithAnnouncementDTO(game = gameDTO, announcements = announcementsDTO)
        return ResponseEntity.ok(response)
    }

    @PostMapping("game/{idGame}/price/{price}")
    fun saveAnnouncement(
        @AuthenticationPrincipal userDetails: UserDetails,
        @PathVariable idGame: Long,
        @PathVariable price: Double,
        uriBuilder: UriComponentsBuilder
    ): ResponseEntity<GameAnnouncementDTO> {
        return gameRepository.findByIdOrNull(idGame)?.let { game ->

            val user = usersRepository.getUser(userDetails)

            val gameAnnouncent = GameAnnouncement(price = price, owner = user, game = game, id = 0)
            val saved = gamesAnnouncementsRepository.save(gameAnnouncent)

            return uriBuilder.toResponseEntity(saved.id, gameAnnouncent.toDTO())
        } ?: throw NotFoundException()
    }

}