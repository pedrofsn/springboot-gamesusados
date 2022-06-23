package br.com.jogosusados.controller

import br.com.jogosusados.error.ErrorDTO
import br.com.jogosusados.error.GameAnnouncementEnabledEqualsException
import br.com.jogosusados.error.GameAnnouncementNotFoundException
import br.com.jogosusados.error.GameNotFoundException
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
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
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

    @Autowired
    lateinit var imageUtilities: ImageUtilities

    @GetMapping("/{id}")
    fun getDetail(@PathVariable id: Long): ResponseEntity<GameAnnouncementDTO> {
        try {
            val game = gamesAnnouncementsRepository.findById(id).get().toDTO(imageUtilities)
            return ResponseEntity.ok(game)
        } catch (exception: NoSuchElementException) {
            throw GameAnnouncementNotFoundException()
        }
    }

    @GetMapping("game/{idGame}")
    fun getList(@PathVariable idGame: Long): ResponseEntity<GameWithAnnouncementDTO> {
        val announcements: List<GameAnnouncement> = gamesAnnouncementsRepository.findByGameIdAndEnabledTrue(idGame)
        val gameDTO = gameRepository.findByIdOrNull(idGame)?.toDTO(imageUtilities) ?: throw GameAnnouncementNotFoundException()
        val announcementsDTO = announcements.map { it.toAnnouncementDTO() }
        val response = GameWithAnnouncementDTO(game = gameDTO, announcements = announcementsDTO)
        return ResponseEntity.ok(response)
    }

    @GetMapping("my-games")
    fun getMyGameAnnouncements(@AuthenticationPrincipal userDetails: UserDetails): ResponseEntity<List<GameAnnouncementDTO>> {
        val user = usersRepository.getUser(userDetails)
        val announcements: List<GameAnnouncement> = gamesAnnouncementsRepository.findByOwnerId(idOwner = user.id)
        val response: List<GameAnnouncementDTO> = announcements.map { it.toDTO(imageUtilities) }
        return ResponseEntity.ok(response)
    }

    @PostMapping("game/{idGame}/price/{price}")
    fun saveAnnouncement(
        @AuthenticationPrincipal userDetails: UserDetails,
        @PathVariable idGame: Long,
        @PathVariable price: Double,
        uriBuilder: UriComponentsBuilder
    ): ResponseEntity<GameAnnouncementDTO> {
        val game = gameRepository.findByIdOrNull(idGame) ?: throw GameNotFoundException()
        val user = usersRepository.getUser(userDetails)
        val gameAnnouncement = GameAnnouncement(price = price, owner = user, game = game, id = 0)
        val saved = gamesAnnouncementsRepository.save(gameAnnouncement)
        return uriBuilder.toResponseEntity(saved.id, gameAnnouncement.toDTO(imageUtilities))
    }

    @PostMapping("/{id}/toggle/{enabled}")
    fun toggleAnnouncement(
        @AuthenticationPrincipal userDetails: UserDetails,
        @PathVariable id: Long,
        @PathVariable enabled: Boolean
    ): ResponseEntity<ErrorDTO> {
        return gamesAnnouncementsRepository.findByIdOrNull(id)?.run {
            if(this.enabled == enabled) {
                throw GameAnnouncementEnabledEqualsException()
            }

            val user = usersRepository.getUser(userDetails)
            val message = if((user.isRegular() && enabled.not()) || user.isManager()) {
                this.enabled = enabled
                gamesAnnouncementsRepository.save(this)
                val statusMessage = if (enabled) "habilitado" else "desabilitado"
                "Anúncio $statusMessage"
            } else {
                "Você não pode realizar esta operação"
            }

            ResponseEntity.ok(ErrorDTO(message = message, id = id))
        } ?: throw GameAnnouncementNotFoundException()
    }
}