package br.com.jogosusados.controller

import br.com.jogosusados.repository.GamePlatformRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("platforms")
@CrossOrigin(origins = ["*"])
class PlatformsController {

    @Autowired
    lateinit var gamesPlatforms: GamePlatformRepository

    @GetMapping
    fun getList() = gamesPlatforms.findAll().map { it.toDTO() }

}