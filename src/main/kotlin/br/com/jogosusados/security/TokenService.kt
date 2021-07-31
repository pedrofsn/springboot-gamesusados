package br.com.jogosusados.security


import br.com.jogosusados.model.user.User
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import java.util.Date


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

    fun isTokenValido(token: String?) = try {
        getClaims(token)
        true
    } catch (e: Exception) {
        false
    }

    fun getIdUsuario(token: String?) = getClaims(token).body.subject.toLong()
    private fun getClaims(token: String?) = Jwts.parser().setSigningKey(secret).parseClaimsJws(token)
}
