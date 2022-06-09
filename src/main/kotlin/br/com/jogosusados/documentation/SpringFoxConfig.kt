package br.com.jogosusados.documentation

import io.swagger.v3.oas.models.security.SecurityScheme.In
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiKey
import springfox.documentation.service.AuthorizationScope
import springfox.documentation.service.SecurityReference
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spi.service.contexts.SecurityContext
import springfox.documentation.spring.web.plugins.Docket


@Configuration
class SpringFoxConfig {

    private val apiInfo by lazy {
        ApiInfoBuilder()
            .title("Games Usados")
            .description("API do Sistema de Games Usados")
            .version("1.0")
            .build()
    }

    @Bean
    fun api(): Docket = Docket(DocumentationType.SWAGGER_2)
        .select()
        .apis(RequestHandlerSelectors.basePackage("br.com.jogosusados.controller"))
        .paths(PathSelectors.any())
        .build()
        .useDefaultResponseMessages(false)
        .securitySchemes(listOf(apiKey()))
        .securityContexts(listOf(securityContext()))
        .apiInfo(apiInfo)

    private fun apiKey() = ApiKey("Token Access", HttpHeaders.AUTHORIZATION, In.HEADER.name)

    private fun securityContext() = SecurityContext.builder().securityReferences(defaultAuth()).build()

    private fun defaultAuth(): List<SecurityReference> {
        val authorizationScopes = arrayOf(AuthorizationScope("global", "accessEverything"))
        return listOf(SecurityReference("Token Access", authorizationScopes))
    }
}