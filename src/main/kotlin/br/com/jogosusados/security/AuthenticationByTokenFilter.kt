package br.com.jogosusados.security


import br.com.jogosusados.repository.UserRepository
import io.jsonwebtoken.ExpiredJwtException
import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.util.StringUtils
import org.springframework.web.filter.OncePerRequestFilter


class AuthenticationByTokenFilter(private val tokenService: TokenService, private val repository: UserRepository) :
    OncePerRequestFilter() {

    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(
        request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain
    ) {
        val jwtToken = extractJwtFromRequest(request)
        try {
            if (StringUtils.hasText(jwtToken) && tokenService.validateToken(jwtToken)) {
                login(jwtToken)
            }
        } catch (ex: ExpiredJwtException) {
            val isRefreshToken = request.getHeader("isRefreshToken") ?: false
            val requestURL = request.requestURL.toString()
            // allow for Refresh Token creation if following conditions are true.
            val isTrue = isRefreshToken == "true"
            val isExpectedEndpoint = requestURL.endsWith("auth/refreshtoken")
            if (isTrue && isExpectedEndpoint) {
                allowForRefreshToken(ex, request)
            } else {
                request.setAttribute("exception", ex)
            }
        } catch (ex: BadCredentialsException) {
            request.setAttribute("exception", ex)
        } catch (ex: Exception) {
            println(ex)
        }
        filterChain.doFilter(request, response)
    }

    private fun allowForRefreshToken(ex: ExpiredJwtException, request: HttpServletRequest) {
        val usernamePasswordAuthenticationToken = UsernamePasswordAuthenticationToken(
            null, null, null
        )
        SecurityContextHolder.getContext().authentication = usernamePasswordAuthenticationToken
        request.setAttribute("claims", ex.claims)
    }

    private fun login(token: String?) {
        val idUser: Long = tokenService.getUserIdFromToken(token)
        val user = repository.findById(idUser).get()
        val authentication = UsernamePasswordAuthenticationToken(user, null, user.authorities)
        SecurityContextHolder.getContext().authentication = authentication
    }

    private fun extractJwtFromRequest(request: HttpServletRequest): String? {
        val bearerToken = request.getHeader("Authorization")
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length)
        }
        return null
    }
}