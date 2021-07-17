package br.com.jogosusados.controller

import br.com.jogosusados.payload.GameDTO
import br.com.jogosusados.repository.GameRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort.Direction
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("games")
class GamesController {

    @Autowired
    lateinit var gamesRepository: GameRepository

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
        true -> gamesRepository.findGameByTitleLikeIgnoreCase(title.asSearchable(), pageable).map { it.toDTO() }
        else -> gamesRepository.findAll(pageable).map { it.toDTO() }
    }

}