package br.com.jogosusados.controller

import br.com.jogosusados.error.GameNotFoundException
import br.com.jogosusados.error.PlatformNotFoundException
import br.com.jogosusados.model.Game
import br.com.jogosusados.payload.GameDTO
import br.com.jogosusados.repository.GamePlatformRepository
import br.com.jogosusados.repository.GameRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort.Direction
import org.springframework.data.repository.findByIdOrNull
import org.springframework.data.web.PageableDefault
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.util.UriComponentsBuilder

@RestController
@RequestMapping("games")
class GamesController {

    @Autowired
    lateinit var gamesRepository: GameRepository

    @Autowired
    lateinit var platformRepository: GamePlatformRepository

    @Autowired
    lateinit var imageUtilities: ImageUtilities

    @GetMapping("/{id}")
    fun getDetail(@PathVariable id: Long): ResponseEntity<GameDTO> {
        val entity = gamesRepository.findByIdOrNull(id)?.toDTO(imageUtilities) ?: throw GameNotFoundException()
        return ResponseEntity.ok(entity)
    }

    @GetMapping
    fun getList(
        @RequestParam title: String?,
        @PageableDefault(sort = ["title"], direction = Direction.DESC, page = 0, size = 10) pageable: Pageable
    ): Page<GameDTO> = when (title != null) {
        true -> gamesRepository.findGameByTitleContainsIgnoreCase(title, pageable).map { it.toDTO(imageUtilities) }
        else -> gamesRepository.findAll(pageable).map { it.toDTO(imageUtilities) }
    }

    @PostMapping("platform/{idPlatform}/title/{title}")
    fun saveGame(
        @AuthenticationPrincipal userDetails: UserDetails,
        @PathVariable idPlatform: Long,
        @PathVariable title: String,
        uriBuilder: UriComponentsBuilder
    ): ResponseEntity<GameDTO> {
        return platformRepository.findByIdOrNull(idPlatform)?.let { platform ->

            val entity = Game(id = 0, title = title, gamePlatform = platform)
            val saved = gamesRepository.save(entity)

            return uriBuilder.toResponseEntity(saved.id, entity.toDTO(imageUtilities))
        } ?: throw PlatformNotFoundException()
    }

    @GetMapping("platform/{idPlatform}")
    fun findyGamePlatformById(@PathVariable idPlatform: Long): List<GameDTO> {
        return gamesRepository.findGameByGamePlatform_Id(idPlatform).map { it.toDTO(imageUtilities) }
    }

}