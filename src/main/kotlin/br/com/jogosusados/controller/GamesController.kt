package br.com.jogosusados.controller

import br.com.jogosusados.error.NotFoundException
import br.com.jogosusados.extensions.toResponseEntity
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
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder
import java.util.*

@RestController
@RequestMapping("games")
class GamesController {

    @Autowired
    lateinit var gamesRepository: GameRepository

    @Autowired
    lateinit var platformRepository: GamePlatformRepository

    @GetMapping("/{id}")
    fun getDetail(@PathVariable id: Long): Optional<GameDTO> {
        val game = gamesRepository.findById(id).get().toDTO()
        return Optional.ofNullable(game)
    }

    @GetMapping
    fun getList(
        @RequestParam title: String?,
        @PageableDefault(sort = ["title"], direction = Direction.DESC, page = 0, size = 10) pageable: Pageable
    ): Page<GameDTO> = when (title != null) {
        true -> gamesRepository.findGameByTitleContainsIgnoreCase(title, pageable).map { it.toDTO() }
        else -> gamesRepository.findAll(pageable).map { it.toDTO() }
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

            return uriBuilder.toResponseEntity(saved.id, entity.toDTO())
        } ?: throw NotFoundException()
    }

}