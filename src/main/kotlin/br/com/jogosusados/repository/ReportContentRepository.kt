package br.com.jogosusados.repository

import br.com.jogosusados.model.ReportContent
import org.springframework.data.jpa.repository.JpaRepository

interface ReportContentRepository : JpaRepository<ReportContent, Long>