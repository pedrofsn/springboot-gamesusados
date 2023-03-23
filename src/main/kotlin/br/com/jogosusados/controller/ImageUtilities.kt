package br.com.jogosusados.controller

import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.Calendar
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.net.URI

@Service
class ImageUtilities {

    private val rootFolder = "uploads"
    private val root: Path = Paths.get(rootFolder)

    fun createImageURL(fileName: String, vararg folders: String): URI? {
        var tempPath: String = rootFolder
        val imageURI = ServletUriComponentsBuilder
            .fromCurrentContextPath()
            .path("images")
            .apply {
                for(folder in folders) {
                    val folderName = "/$folder"
                    path(folderName)
                    tempPath += folderName
                }
                val fileNameWithSlash = "/$fileName"
                path(fileNameWithSlash)
                tempPath += fileNameWithSlash
            }
            .buildAndExpand(*folders, fileName)
            .toUri()
        return if(File(tempPath).exists()) imageURI else null
    }

    fun getFileName(idUser: Long, file: MultipartFile): String {
        val timeInMillis = Calendar.getInstance().timeInMillis
        val filenameWithoutExtension = file.originalFilename?.split(".")?.firstOrNull()
        // TODO Isto permitirá a sobrescrita de arquivos com o mesmo nome do lado do servidor
        return "${filenameWithoutExtension ?: timeInMillis}.${MediaType.IMAGE_PNG.subtype}"
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

    fun getFolderPath(folderName: String): Path = File(root.toFile(), folderName).toPath()

    fun addExtension(fileName: String): String {
        val extension = MediaType.IMAGE_PNG.subtype
        return if (fileName.endsWith(extension)) fileName else "$fileName.$extension"
    }

}