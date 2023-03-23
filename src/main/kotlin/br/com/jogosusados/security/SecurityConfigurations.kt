package br.com.jogosusados.security

import br.com.jogosusados.model.user.Admin
import br.com.jogosusados.model.user.Manager
import br.com.jogosusados.model.user.Regular
import br.com.jogosusados.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@EnableWebSecurity
@Configuration
class SecurityConfigurations : WebSecurityConfigurerAdapter() {

    @Autowired
    private lateinit var authService: AuthenticationService

    @Autowired
    private lateinit var tokenService: TokenService

    @Autowired
    private lateinit var userRepository: UserRepository

    @Bean
    fun encoder() = BCryptPasswordEncoder()

    @Bean
    fun corsConfig() = CorsConfig()

    @Bean
    @Throws(Exception::class)
    override fun authenticationManager(): AuthenticationManager = super.authenticationManager()

    // Authentication settings
    @Throws(Exception::class)
    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService(authService).passwordEncoder(encoder())
    }

    // Authorization settings
    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http.headers().frameOptions().disable()
        http.cors().and().csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and().addFilterBefore(
                AuthenticationByTokenFilter(tokenService, userRepository),
                UsernamePasswordAuthenticationFilter::class.java
            )
            .authorizeRequests()
            .handleOpenEndpoints()
            .handleAdminEndpoints()
            .handleManagerEndpoints()
            .handleLoggedInEndpoints()
            .handleLoggedAndManagerEndpoints()
            .handleManagerAndAdminEndpoints()
            .handleStaticResources()

    }

    // Static resources settings (js, css, images, etc.)
    @Throws(Exception::class)
    override fun configure(web: WebSecurity) {
        web.ignoring().antMatchers(
            "/**.html",
            "/v2/api-docs",
            "/webjars/**",
            "/configuration/**",
//            "/swagger-resources/**",
//            "/swagger-ui/**",
//            "/swagger-ui/index.html**",
            "/webjars/**",
            "/actuator/health"
        )
    }

    private fun ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry.handleOpenEndpoints(): ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry {
        return antMatchers(HttpMethod.GET, "/images/*/*").permitAll()
            .antMatchers(HttpMethod.GET, "/platforms").permitAll()
            .antMatchers(HttpMethod.GET, "/games").permitAll()
            .antMatchers(HttpMethod.GET, "/games/search/**").permitAll()
            .antMatchers(HttpMethod.POST, "/users/register").permitAll()
            .antMatchers(HttpMethod.POST, "/auth").permitAll()
            .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
    }

    private fun ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry.handleStaticResources(): ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry {
        return antMatchers("/h2-console/**").permitAll().anyRequest().authenticated()
    }

    private fun ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry.handleManagerEndpoints() = and()
        .authorizeRequests()
        .antMatchers(HttpMethod.GET, "/report").hasAuthority(Manager.authority)
        .antMatchers(HttpMethod.POST, "/games/platform/*/title/*").hasAuthority(Manager.authority)
        .antMatchers(HttpMethod.POST, "/platforms/register/*").hasAuthority(Manager.authority)
        .antMatchers(HttpMethod.GET, "/announcements/all").hasAuthority(Manager.authority)
        .antMatchers(HttpMethod.GET, "/announcements/enabled").hasAuthority(Manager.authority)
        .antMatchers(HttpMethod.GET, "/announcements/disabled").hasAuthority(Manager.authority)

    private fun ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry.handleLoggedInEndpoints() = and()
        .authorizeRequests()
        .antMatchers(HttpMethod.GET, "/games/platform/*").hasAuthority(Regular.authority)
        .antMatchers(HttpMethod.GET, "/announcements/my-games").hasAuthority(Regular.authority)
        .antMatchers(HttpMethod.POST, "/announcements/game/*/price/*").hasAuthority(Regular.authority)
        .antMatchers(HttpMethod.POST, "/report").hasAuthority(Regular.authority)

    private fun ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry.handleLoggedAndManagerEndpoints() = and()
        .authorizeRequests()
        .antMatchers(HttpMethod.GET, "/announcements/*").hasAnyAuthority(Manager.authority, Regular.authority)
        .antMatchers(HttpMethod.POST, "/announcements/*/toggle/*").hasAnyAuthority(Manager.authority, Regular.authority)
        .antMatchers(HttpMethod.POST, "/upload/**").hasAnyAuthority(Manager.authority, Regular.authority)
        .antMatchers(HttpMethod.OPTIONS, "/images/**").hasAnyAuthority(Manager.authority, Regular.authority)
        .antMatchers(HttpMethod.GET, "/users/my-profile").hasAnyAuthority(Manager.authority, Regular.authority)
    private fun ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry.handleAdminEndpoints() = and()
        .authorizeRequests()
        .antMatchers(HttpMethod.POST, "/users/register/manager").hasAuthority(Admin.authority)

    private fun ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry.handleManagerAndAdminEndpoints() = and()
        .authorizeRequests()
        .antMatchers(HttpMethod.GET, "/users/*").hasAnyAuthority(Manager.authority, Admin.authority)
}