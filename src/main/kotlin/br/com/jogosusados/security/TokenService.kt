package br.com.jogosusados.security


import br.com.jogosusados.model.user.User
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.SignatureException
import io.jsonwebtoken.UnsupportedJwtException
import java.util.Date
import java.util.concurrent.TimeUnit
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service


@Service
class TokenService {

    @Value("\${forum.jwt.expiration}")
    private lateinit var expiration: String

    @Value("\${forum.jwt.secret}")
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

    fun doGenerateRefreshToken(claims: Map<String, Any>, subject: String?): String {
        val refreshExpirationDateInMs = TimeUnit.MILLISECONDS.convert(1, TimeUnit.MINUTES)
        return Jwts.builder()
            .setIssuer("Games Usados")
            .setClaims(claims)
            .setSubject(subject)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + refreshExpirationDateInMs))
            .signWith(SignatureAlgorithm.HS256, secret).compact()
    }

    fun getUserIdFromToken(token: String?) = getClaims(token).body.subject.toLong()
    private fun getClaims(token: String?) = Jwts.parser().setSigningKey(secret).parseClaimsJws(token)

    fun validateToken(authToken: String?) = try {
        Jwts.parser().setSigningKey(secret).parseClaimsJws(authToken)
        true
    } catch (ex: SignatureException) {
        throw BadCredentialsException("INVALID_CREDENTIALS", ex)
    } catch (ex: MalformedJwtException) {
        throw BadCredentialsException("INVALID_CREDENTIALS", ex)
    } catch (ex: UnsupportedJwtException) {
        throw BadCredentialsException("INVALID_CREDENTIALS", ex)
    } catch (ex: IllegalArgumentException) {
        throw BadCredentialsException("INVALID_CREDENTIALS", ex)
    } catch (ex: ExpiredJwtException) {
        throw ex
    }
}
