package br.com.jogosusados.error

abstract class CustomNotFoundException(message: String) : Exception(message)
class GameAnnouncementNotFoundException : CustomNotFoundException("Anúncio não encontrado")
class GameAnnouncementEnabledEqualsException : CustomNotFoundException("O anúncio não pode ser alterado para o mesmo status")
class GameNotFoundException : CustomNotFoundException("Jogo não encontrado")
class PlatformNotFoundException : CustomNotFoundException("Plataforma não encontrada")
class PlatformFoundException : CustomNotFoundException("Plataforma já cadastrada")
class FileEmptyException : CustomNotFoundException("Arquivo vazio")
class FailWhenReportContentException : CustomNotFoundException("Erro ao denunciar o conteúdo")

class LoginException : CustomNotFoundException("Usuário ou senha inválidos")