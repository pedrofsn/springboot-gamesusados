package br.com.jogosusados.security


import br.com.jogosusados.model.user.Manager
import br.com.jogosusados.model.user.Admin
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
        http.authorizeRequests()
            .handleOpenEndpoints()
            .handleAdminEndpoints()
            .handleManagerEndpoints()
            .handleLoggedInEndpoints()
            .handleStaticResources()
            .and().csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and().addFilterBefore(
                AuthenticationByTokenFilter(tokenService, userRepository),
                UsernamePasswordAuthenticationFilter::class.java
            )
    }

    // Static resources settings (js, css, images, etc.)
    @Throws(Exception::class)
    override fun configure(web: WebSecurity) {
        web.ignoring().antMatchers(
            "/**.html",
            "/v2/api-docs",
            "/webjars/**",
            "/configuration/**",
            "/swagger-resources/**",
            "/swagger-ui/**",
            "/swagger-ui/index.html**",
            "/webjars/**",
            "/actuator/health"
        )
    }

    private fun ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry.handleOpenEndpoints(): ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry {
        return antMatchers(HttpMethod.GET, "/images/*/*").permitAll()
            .antMatchers(HttpMethod.POST, "/images/upload/*").permitAll()
            .antMatchers(HttpMethod.POST, "/upload").permitAll()
            .antMatchers(HttpMethod.GET, "/platforms").permitAll()
            .antMatchers(HttpMethod.GET, "/games").permitAll()
            .antMatchers(HttpMethod.GET, "/games/search/**").permitAll()
            .antMatchers(HttpMethod.POST, "/users/register").permitAll()
            .antMatchers(HttpMethod.GET, "/auth/refreshtoken").permitAll()
            .antMatchers(HttpMethod.POST, "/auth").permitAll()
    }

    private fun ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry.handleStaticResources(): ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry {
        return antMatchers("/h2-console/**").permitAll().anyRequest().authenticated()
    }

    private fun ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry.handleManagerEndpoints() = and()
        .authorizeRequests()
        .antMatchers(HttpMethod.GET, "/report").hasAuthority(Manager.authority)
        .antMatchers(HttpMethod.POST, "/games/platform/*/title/*").hasAuthority(Manager.authority)
        .antMatchers(HttpMethod.POST, "/announcements/*/toggle/*").hasAnyAuthority(Manager.authority, Regular.authority)

    private fun ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry.handleLoggedInEndpoints() = and()
        .authorizeRequests()
        .antMatchers(HttpMethod.GET, "/announcements/*").hasAuthority(Regular.authority)
        .antMatchers(HttpMethod.GET, "/games/platform/*").hasAuthority(Regular.authority)
        .antMatchers(HttpMethod.POST, "/announcements/game/*/price/*").hasAuthority(Regular.authority)
        .antMatchers(HttpMethod.POST, "/report").hasAuthority(Regular.authority)

    private fun ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry.handleAdminEndpoints() = and()
        .authorizeRequests()
        .antMatchers(HttpMethod.POST, "/users/register/manager").hasAuthority(Admin.authority)
}