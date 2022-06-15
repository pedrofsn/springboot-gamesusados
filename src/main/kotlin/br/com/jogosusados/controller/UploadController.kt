package br.com.jogosusados.controller

import br.com.jogosusados.error.FileEmptyException
import br.com.jogosusados.payload.ResponseImageUploaded
import br.com.jogosusados.repository.UserRepository
import org.h2.util.IOUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.MediaType.IMAGE_PNG
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.FileInputStream
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import javax.servlet.http.HttpServletResponse
import kotlin.io.path.exists

@RestController
@RequestMapping("images")
class UploadController {

    @Autowired
    lateinit var imageUtilities: ImageUtilities

    @Autowired
    lateinit var usersRepository: UserRepository

    @PostMapping("/upload/{folderName}")
    fun singleFileUpload(
        @AuthenticationPrincipal userDetails: UserDetails,
        @PathVariable("folderName") folderName: String,
        @RequestParam("file") file: MultipartFile
    ): ResponseEntity<ResponseImageUploaded> {
        if (file.isEmpty) throw FileEmptyException()
        val user = usersRepository.getUser(userDetails)

        // TODO [pedrofsn] tratamento quando não enviar o arquivo | MultipartException: Current request is not a multipart request
        // TODO [pedrofsn] tratamento do tamanho do disco
        // TODO [pedrofsn] tratamento do tamanho do arquivo
        // TODO [pedrofsn] limpar formatações e caracteres especiais do nome da pasta para evitar injections e problemas com nomes de pastas
        imageUtilities.createFolder()
        val folderPath = imageUtilities.getFolderPath(folderName)
        if (folderPath.exists().not()) {
            Files.createDirectory(folderPath)
        }

        val fileName = imageUtilities.getFileName(user.id, folderName, file)
        val pathFile: Path = getFilePath(folderPath, fileName)

        Files.copy(file.inputStream, pathFile, StandardCopyOption.REPLACE_EXISTING)

        // TODO [pedrofsn] Será que tem como pegar o valor da annotation do controller dinamicamente? Se sim, matar o "images" do path
        val imageUrl = imageUtilities.createImageURL(folderName, fileName)
        val response = ResponseImageUploaded(folder = folderName, fileName = fileName, url = imageUrl.toString())
        return ResponseEntity.created(imageUrl).body(response)
    }

    @GetMapping(path = ["/{folderName}/{fileName}"], produces = [MediaType.IMAGE_PNG_VALUE])
    fun getImage(
        @PathVariable("folderName") folderName: String,
        @PathVariable("fileName") fileName: String,
        response: HttpServletResponse
    ) {
        val extension = IMAGE_PNG.subtype
        val name = if (fileName.endsWith(extension)) fileName else fileName + extension
        val stream = FileInputStream(getFile(folderName, name))
        IOUtils.copy(stream, response.outputStream)
    }

    private fun getFilePath(folderPath: Path, fileName: String) = folderPath.resolve(fileName)

    private fun getFile(folderName: String, fileName: String): File {
        val folderPath = imageUtilities.getFolderPath(folderName)
        val filePath = getFilePath(folderPath, fileName)
        return filePath.toFile()
    }
}