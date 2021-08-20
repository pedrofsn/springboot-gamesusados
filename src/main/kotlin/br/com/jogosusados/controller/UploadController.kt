package br.com.jogosusados.controller

import br.com.jogosusados.error.FileEmptyException
import br.com.jogosusados.payload.ResponseImageUploaded
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.servlet.http.HttpServletResponse
import kotlin.io.path.outputStream
import org.h2.util.IOUtils
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.support.ServletUriComponentsBuilder


@RestController
@RequestMapping("images")
class UploadController {

    private val root: Path = Paths.get("uploads")

    @PostMapping("/upload/{folderName}")
    fun singleFileUpload(
        @PathVariable("folderName") folderName: String,
        @RequestParam("file") file: MultipartFile
    ): ResponseEntity<ResponseImageUploaded> {
        if (file.isEmpty) throw FileEmptyException()

        // TODO [pedrofsn] tratamento quando não enviar o arquivo | MultipartException: Current request is not a multipart request
        // TODO [pedrofsn] tratamento do tamanho do disco
        // TODO [pedrofsn] tratamento do tamanho do arquivo
        // TODO [pedrofsn] limpar formatações e caracteres especiais do nome da pasta para evitar injections e problemas com nomes de pastas
        root.createFolder()
        val folderPath = getFolderPath(folderName).also { it.createFolder() }

        val fileName = "${getTimeStamp()}_${file.originalFilename.orEmpty()}"
        val pathFile: Path = getFilePath(folderPath, fileName)
        IOUtils.copy(file.inputStream, pathFile.outputStream())

        // TODO [pedrofsn] Será que tem como pegar o valor da annotation do controller dinamicamente? Se sim, matar o "images" do path
        val imageUrl = createImageURL(folderName, fileName)
        val response = ResponseImageUploaded(folder = folderName, fileName = fileName, url = imageUrl.toString())
        return ResponseEntity.created(imageUrl).body(response)
    }

    private fun createImageURL(folderName: String, fileName: String) = ServletUriComponentsBuilder
        .fromCurrentContextPath()
        .path("images/{folderName}/{fileName}")
        .buildAndExpand(folderName, fileName)
        .toUri()

    private fun getTimeStamp(): String {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
        val now = LocalDateTime.now()
        return formatter.format(now).replace(" ", "_")
            .replace("/", "-")
            .replace(":", "-")
    }

    private fun Path.createFolder() {
        try {
            if (Files.exists(this).not()) {
                Files.createDirectory(this)
            }
        } catch (e: IOException) {
            throw RuntimeException("Could not initialize folder for upload!")
        }
    }

    @GetMapping(path = ["/{folderName}/{fileName}"], produces = [MediaType.IMAGE_PNG_VALUE])
    fun getImage(
        @PathVariable("folderName") folderName: String,
        @PathVariable("fileName") fileName: String,
        response: HttpServletResponse
    ) {
        val stream = FileInputStream(getFile(folderName, fileName))
        IOUtils.copy(stream, response.outputStream)
    }

    private fun getFilePath(folderPath: Path, fileName: String) = folderPath.resolve(fileName)
    private fun getFolderPath(folderName: String) = File(root.toFile(), folderName).toPath()

    private fun getFile(folderName: String, fileName: String): File {
        val folderPath = getFolderPath(folderName)
        val filePath = getFilePath(folderPath, fileName)
        return filePath.toFile()
    }
}