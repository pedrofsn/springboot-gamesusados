package br.com.jogosusados.controller

import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

@Service
class ImageUtilities {

    private val root: Path = Paths.get("uploads")

    fun createImageURL(fileName: String, vararg folders: String) = ServletUriComponentsBuilder
        .fromCurrentContextPath()
        .path("images")
        .apply {
            for(folder in folders) {
                path("/$folder")
            }
            path("/$fileName")
        }
        .buildAndExpand(*folders, fileName)
        .toUri()

    fun getFileName(idUser: Long, folderName: String, file: MultipartFile): String {
        if(folderName == "my-profile") {
            return "$idUser.${MediaType.IMAGE_PNG.subtype}"
        }
        val timeInMillis = Calendar.getInstance().timeInMillis
        val filenameWithoutExtension = file.originalFilename?.split(".")?.firstOrNull()
        // TODO Isto permitirá a sobrescrita de arquivos com o mesmo nome do lado do servidor
        return "${filenameWithoutExtension ?: timeInMillis}.${MediaType.IMAGE_PNG.subtype}"
    }

    private fun getTimeStamp(): String {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
        val now = LocalDateTime.now()
        return formatter.format(now).replace(" ", "_")
            .replace("/", "-")
            .replace(":", "-")
    }

    fun createFolder() {
        try {
            if (Files.exists(root).not()) {
                Files.createDirectory(root)
            }
        } catch (e: IOException) {
            throw RuntimeException("Could not initialize folder for upload!")
        }
    }

    fun getFolderPath(folderName: String) = File(root.toFile(), folderName).toPath()

    fun addExtension(fileName: String): String {
        val extension = MediaType.IMAGE_PNG.subtype
        return if (fileName.endsWith(extension)) fileName else "$fileName.$extension"
    }

}