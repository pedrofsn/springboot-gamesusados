package br.com.jogosusados.controller

import java.io.FileInputStream
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.servlet.http.HttpServletResponse
import org.h2.util.IOUtils
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.mvc.support.RedirectAttributes


@Controller
class UploadController {

    private val root = Paths.get("uploads")

    @PostMapping("/upload")
    fun singleFileUpload(@RequestParam("file") file: MultipartFile, redirectAttributes: RedirectAttributes): String? {
        if (file.isEmpty) {
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload")
            return "redirect:uploadStatus"
        }

        root.createFolder()

        try {
            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
            val now = LocalDateTime.now()
            val timestamp = formatter.format(now).replace(" ", "_")
                .replace("/", "-")
                .replace(":", "-")

            val fileName = "${timestamp}_${file.originalFilename.orEmpty()}"
            val path = root.resolve(fileName)
            Files.copy(file.inputStream, path)

            /* Get the file and save it somewhere
            val bytes = file.bytes
            val path: Path = Paths.get(UPLOADED_FOLDER + file.originalFilename)
            Files.write(path, bytes)
            */
            redirectAttributes.addFlashAttribute("message", "You successfully uploaded '${file.originalFilename}'")

        } catch (e: IOException) {
            e.printStackTrace()
        }
        return "redirect:/uploadStatus"
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

    @GetMapping("/uploadStatus")
    fun uploadStatus() = "uploadStatus"

    @GetMapping(path = ["/images/{fileName}"], produces = [MediaType.IMAGE_PNG_VALUE])
    fun getImage(@PathVariable fileName: String, response : HttpServletResponse) {
        val path = root.resolve(fileName)
        val file = path.toFile()
        val stream = FileInputStream(file)
        IOUtils.copy(stream, response.outputStream)
    }

}