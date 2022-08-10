package br.com.jogosusados.security


import br.com.jogosusados.model.user.User
import io.jsonwebtoken.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import java.util.*

@Service
class TokenService {

    @Value("\${sistema.jwt.expiration}")
    private lateinit var expiration: String

    @Value("\${sistema.jwt.secret}")
    private lateinit var secret: String

    fun createToken(authentication: Authentication): String {
        val userLoggedIn = authentication.principal as User
        val today = Date()
        val dateExpiration = Date(today.time + expiration.toLong())

        return Jwts.builder()
            .setIssuer("Games Usados")
            .setSubject(userLoggedIn.id.toString())
            .setIssuedAt(today)
            .setExpiration(dateExpiration)
            .signWith(SignatureAlgorithm.HS256, secret)
            .compact()
    }

    fun getUserIdFromToken(token: String?) = getClaims(token).body.subject.toLong()
    private fun getClaims(token: String?) = Jwts.parser().setSigningKey(secret).parseClaimsJws(token)

    fun validateToken(authToken: String?) = try {
        if (authToken?.isNotBlank() == true) {
            Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(authToken)
            true
        } else {
            false
        }
    } catch (ex: SignatureException) {
        throw BadCredentialsException("INVALID_CREDENTIALS", ex)
    } catch (ex: MalformedJwtException) {
        throw BadCredentialsException("INVALID_CREDENTIALS", ex)
    } catch (ex: UnsupportedJwtException) {
        throw BadCredentialsException("INVALID_CREDENTIALS", ex)
    } catch (ex: IllegalArgumentException) {
        throw BadCredentialsException("INVALID_CREDENTIALS", ex)
    } catch (ex: ExpiredJwtException) {
        throw BadCredentialsException("EXPIRED_JWT_EXCEPTION", ex)
    }
}
