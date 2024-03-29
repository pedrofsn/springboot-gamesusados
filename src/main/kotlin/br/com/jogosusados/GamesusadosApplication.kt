package br.com.jogosusados

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.data.web.config.EnableSpringDataWebSupport
//import springfox.documentation.swagger2.annotations.EnableSwagger2

@SpringBootApplication
//@EnableSwagger2
@EnableSpringDataWebSupport
@EnableCaching
class GamesusadosApplication

fun main(args: Array<String>) {
    runApplication<GamesusadosApplication>(*args)
}
