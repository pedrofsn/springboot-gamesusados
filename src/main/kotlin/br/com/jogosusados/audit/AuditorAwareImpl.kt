package br.com.jogosusados.audit

import br.com.jogosusados.model.user.User
import org.springframework.data.domain.AuditorAware
import org.springframework.security.core.context.SecurityContextHolder
import java.util.*

class AuditorAwareImpl : AuditorAware<String> {

    override fun getCurrentAuditor() = (SecurityContextHolder.getContext().authentication.principal)
        ?.let { (it as? User)?.email }
        ?.let { Optional.of(it) }
        ?: Optional.empty<String>()
}