package com.ishan.blogapi.security;

import com.ishan.blogapi.security.authtokens.AuthTokenAuthenticationFilter;
import com.ishan.blogapi.security.authtokens.AuthTokenService;
import com.ishan.blogapi.security.jwt.JWTAuthenticationFilter;
import com.ishan.blogapi.security.jwt.JWTService;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;

@EnableWebSecurity
public class AppSecurityConfig extends WebSecurityConfigurerAdapter {
    private final AuthTokenService authTokenService;

    private final JWTService jwtService;

    // Constructor injection is used here because we want to use the same instance of the services in the filters
    public AppSecurityConfig(AuthTokenService authTokenService, JWTService jwtService) {
        this.authTokenService = authTokenService;
        this.jwtService = jwtService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().cors().disable(); // Disable CSRF and CORS
        http
                .authorizeRequests() // Authorize requests (which requests are allowed and which are not)
                .antMatchers(HttpMethod.POST, "/users/**").permitAll() // Allow POST requests to /users/** without authentication (for registration)
                .antMatchers(HttpMethod.GET, "/articles/**").permitAll() // Allow GET requests to /articles/dummy without authentication //TODO Remove this line
                .antMatchers(HttpMethod.GET, "/profiles/**").permitAll() // Allow GET requests to /profiles without authentication
                .anyRequest().authenticated(); // All other requests must be authenticated

        http.addFilterBefore(new JWTAuthenticationFilter(jwtService), AnonymousAuthenticationFilter.class); // Add JWT filter (1 of 2) - Authentication filter (performs authentication) - This filter will intercept all requests and perform authentication before the request is processed by the controller (if authentication is successful) - If authentication is successful, the request will be processed by the controller - If authentication is unsuccessful, the request will not be processed by the controller and a 401/403 Unauthorized response will be returned
        http.addFilterBefore(new AuthTokenAuthenticationFilter(authTokenService), AnonymousAuthenticationFilter.class); // Add AuthToken filter (2 of 2) - Authentication filter (performs authentication) - This filter will intercept all requests and perform authentication before the request is processed by the controller (if authentication is successful) - If authentication is successful, the request will be processed by the controller - If authentication is unsuccessful, the request will not be processed by the controller and a 401/403 Unauthorized response will be returned
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }
}