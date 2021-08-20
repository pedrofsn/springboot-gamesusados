package br.com.jogosusados.payload

data class ResponseImageUploaded(
    val folder: String,
    val fileName: String,
    val url: String
)