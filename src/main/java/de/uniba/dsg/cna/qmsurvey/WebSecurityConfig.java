package de.uniba.dsg.cna.qmsurvey;

import de.uniba.dsg.cna.qmsurvey.application.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;

@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AccountService accountService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MyBasicAuthenticationEntryPoint authenticationEntryPoint;

    @Bean
    public TokenBasedRememberMeServices rememberMeServices() {
        return new TokenBasedRememberMeServices("remember-me-key", accountService);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .eraseCredentials(true)
                .userDetailsService(accountService)
                .passwordEncoder(passwordEncoder);
    }

    @Configuration
    @Order(1)
    public class ApiSecurityConfig extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .csrf().disable()
                    .authorizeRequests()
                    .antMatchers(HttpMethod.OPTIONS, "/**")
                    .permitAll()
                    .antMatchers(HttpMethod.GET,  "/api/v3/api-docs",
                            "/api/v3/api-docs/*",
                            "/api/swagger-ui",
                            "/api/swagger-ui/*",
                            "/api/api-docs",
                            "/api/api-docs/*").permitAll()
                    .antMatchers(HttpMethod.PUT,"/api/*/clientstate")
                    .permitAll()
                    .antMatchers(HttpMethod.POST, "/api/submit", "/api/*/factors", "/api/*/feedback", "/api/*/demographics", "/api/*/contact")
                    .permitAll()
                    .antMatchers("/api/**")
                    .authenticated();
        }
    }

    @Configuration
    @Order(2)
    public class WebAppSecurityConfig extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .authorizeRequests()
                    .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                    .antMatchers( "/admin/signin", "/fonts/**", "/favicon.ico").permitAll()
                    .anyRequest().authenticated()
                    .and()
                    .formLogin()
                    .loginPage("/admin/signin")
                    .failureUrl("/admin/signin?error=1")
                    .loginProcessingUrl("/admin/authenticate")
                    .permitAll()
                    .and()
                    .logout()
                    .logoutUrl("/admin/logout")
                    .permitAll()
                    .logoutSuccessUrl("/admin/signin")
                    .and()
                    .rememberMe()
                    .rememberMeServices(rememberMeServices())
                    .key("remember-me-key");
        }
    }

    @Bean(name = "authenticationManager")
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}