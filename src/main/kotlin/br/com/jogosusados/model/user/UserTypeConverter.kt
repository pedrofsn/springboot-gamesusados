package br.com.jogosusados.model.user

import javax.persistence.AttributeConverter
import javax.persistence.Converter

@Converter
class UserTypeConverter : AttributeConverter<UserType, String> {
    override fun convertToDatabaseColumn(attribute: UserType?) = attribute?.typeName ?: Regular.typeName
    override fun convertToEntityAttribute(dbData: String?) = UserType.getUsertType(dbData)
}