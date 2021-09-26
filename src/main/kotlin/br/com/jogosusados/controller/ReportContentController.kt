package br.com.jogosusados.controller

import br.com.jogosusados.error.ErrorDTO
import br.com.jogosusados.error.FailWhenReportContentException
import br.com.jogosusados.payload.PayloadReportContent
import br.com.jogosusados.payload.PayloadSimpleReportContent
import br.com.jogosusados.repository.GameAnnouncementRepository
import br.com.jogosusados.repository.GameRepository
import br.com.jogosusados.repository.ReportContentRepository
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
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.util.UriComponentsBuilder

@RestController
@RequestMapping("report")
class ReportContentController {

    @Autowired
    lateinit var reportContentRepository: ReportContentRepository

    @Autowired
    lateinit var gameRepository: GameRepository

    @Autowired
    lateinit var announcementRepository: GameAnnouncementRepository

    @GetMapping
    fun getList(
        @PageableDefault(sort = ["description"], direction = Direction.DESC, page = 0, size = 10) pageable: Pageable
    ): Page<PayloadReportContent> = reportContentRepository.findAll(pageable).map { it.toDTO() }

    @PostMapping
    fun save(
        @AuthenticationPrincipal userDetails: UserDetails,
        @RequestBody body: PayloadSimpleReportContent,
        uriBuilder: UriComponentsBuilder
    ): ResponseEntity<ErrorDTO> {

        val entity = when {
            body.isGame() -> gameRepository.findByIdOrNull(body.id)?.let { entity ->
                body.toModel(game = entity)
            }
            body.isAnnouncement() -> announcementRepository.findByIdOrNull(body.id)?.let { entity ->
                body.toModel(announcement = entity)
            }
            else -> null
        }

        return entity?.let {
            val saved = reportContentRepository.save(entity)
            val response = ErrorDTO(message = body.getSuccessMessage(), id = saved.id)
            return@let ResponseEntity.ok(response)
        } ?: throw FailWhenReportContentException()
    }

}