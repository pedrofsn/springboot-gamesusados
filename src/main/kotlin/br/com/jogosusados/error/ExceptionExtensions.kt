package br.com.jogosusados.error

@Suppress("UNCHECKED_CAST")
fun <T : Exception> Exception.toDTO(): ErrorDTO {
    val customMessage = (this as? T)?.message
    return ErrorDTO(message = customMessage.orEmpty())
}