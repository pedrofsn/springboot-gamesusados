package br.com.jogosusados.documentation

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiKey
import springfox.documentation.spi.DocumentationType
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
        .apiInfo(apiInfo)
        .securitySchemes(listOf(apiKey()))

    fun apiKey(): ApiKey = ApiKey("Bearer", "Authorization", "header")
}