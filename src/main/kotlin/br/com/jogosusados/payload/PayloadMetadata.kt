package br.com.jogosusados.payload

import java.text.SimpleDateFormat
import java.util.*

data class PayloadMetadata(
    val createdBy: String,
    val createdAt: Calendar,
    val updatedBy: String?,
    val updateAt: Calendar?,
    val textCreatedAt: String = parse(createdAt)
)

fun parse(calendar: Calendar): String {
    val localePtBr = Locale("pt", "BR")
    val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", localePtBr)
    return formatter.format(calendar.time)
}