package br.com.jogosusados.payload

data class ProfileDTO(
    val id: Long,
    val name: String,
    val phone: String,
    val email: String,
    val type: String,
    val image: String? = null,
    val metadata: PayloadMetadata
)