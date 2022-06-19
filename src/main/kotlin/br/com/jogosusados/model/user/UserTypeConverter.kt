package br.com.jogosusados.model.user

import javax.persistence.AttributeConverter
import javax.persistence.Converter

@Converter
class UserTypeConverter : AttributeConverter<UserType, String> {
    override fun convertToDatabaseColumn(attribute: UserType?): String {
        return attribute?.typeName ?: Regular.typeName
    }
    override fun convertToEntityAttribute(dbData: String?): UserType {
        return UserType.getUsertType(dbData)
    }
}