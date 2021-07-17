package br.com.jogosusados.security


import br.com.jogosusados.repository.UserRepository
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


class AuthenticationByTokenFilter(private val tokenService: TokenService, private val repository: UserRepository) :
    OncePerRequestFilter() {

    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        val token = recoverToken(request)
        if (tokenService.isTokenValido(token)) {
            login(token)
        }
        filterChain.doFilter(request, response)
    }

    private fun login(token: String?) {
        val idUser: Long = tokenService.getIdUsuario(token)
        val user = repository.findById(idUser).get()
        val authentication = UsernamePasswordAuthenticationToken(user, null, user.authorities)
        SecurityContextHolder.getContext().authentication = authentication
    }

    private fun recoverToken(request: HttpServletRequest): String? {
        val token = request.getHeader("Authorization")
        return when {
            token == null || token.isEmpty() || token.startsWith("Bearer ").not() -> null
            else -> token.substring(7, token.length)
        }
    }
}