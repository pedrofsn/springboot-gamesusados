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
    val formatter = SimpleDateFormat("dd/MM/yyyy hh:mm")
    return formatter.format(calendar.time)
}