package br.com.jogosusados.error

abstract class CustomNotFoundException(message: String) : Exception(message)
class GameAnnouncementNotFoundException : CustomNotFoundException("Anúncio de jogo não encontrado")
class GameNotFoundException : CustomNotFoundException("Jogo não encontrado")
class PlatformNotFoundException : CustomNotFoundException("Plataforma não encontrada")

class LoginException : CustomNotFoundException("Usuário ou senha inválidos")