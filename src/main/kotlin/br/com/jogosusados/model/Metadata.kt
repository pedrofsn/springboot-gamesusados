package br.com.jogosusados.model

import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.util.*
import javax.persistence.EntityListeners
import javax.persistence.MappedSuperclass

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class Metadata {

    @CreatedBy
    lateinit var createdBy: String

    @CreatedDate
    var createdAt: Calendar = Calendar.getInstance()

    @LastModifiedBy
    var updatedBy: String? = null

    @LastModifiedDate
    var updateAt: Calendar? = null
}
