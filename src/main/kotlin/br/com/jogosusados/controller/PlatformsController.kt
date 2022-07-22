package br.com.jogosusados.controller

import br.com.jogosusados.error.PlatformFoundException
import br.com.jogosusados.model.GamePlatform
import br.com.jogosusados.payload.GamePlatformDTO
import br.com.jogosusados.repository.GamePlatformRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder

@RestController
@RequestMapping("platforms")
@CrossOrigin(origins = ["*"])
class PlatformsController {

    @Autowired
    lateinit var gamesPlatforms: GamePlatformRepository

    @GetMapping
    fun getList() = gamesPlatforms.findAll().map { it.toDTO() }

    @PostMapping("register/{title}")
    fun saveGame(
        @AuthenticationPrincipal userDetails: UserDetails,
        @PathVariable title: String,
        uriBuilder: UriComponentsBuilder
    ): ResponseEntity<GamePlatformDTO> {
        val platformFound = gamesPlatforms.findGamePlatformByName(title)

        if (platformFound == null) {
            val saved = gamesPlatforms.save(GamePlatform(0, title))
            return uriBuilder.toResponseEntity(saved.id, saved.toDTO())
        }

        throw PlatformFoundException()
    }

}