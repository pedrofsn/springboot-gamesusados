package br.com.jogosusados.repository

import br.com.jogosusados.model.FlagContent
import org.springframework.data.jpa.repository.JpaRepository

interface FlagContentRepository : JpaRepository<FlagContent, Long>