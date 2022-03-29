package com.lillya.piemon.config.security;

import com.lillya.piemon.auth.user.model.OAuth2UserImpl;
import com.lillya.piemon.auth.user.service.OAuth2UserServiceImpl;
import com.lillya.piemon.auth.user.service.UserDetailsServiceImpl;
import com.lillya.piemon.jwt.JwtAuthenticationFilter;
import com.lillya.piemon.jwt.JwtTokenFilter;
import com.lillya.piemon.jwt.JwtUtils;
import com.lillya.piemon.jwt.config.JwtConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final OAuth2UserServiceImpl oauth2UserService;
    private final UserDetailsServiceImpl userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final JwtConfig jwtConfig;
    private final JwtUtils jwtUtils;

    @Autowired
    public WebSecurityConfiguration(OAuth2UserServiceImpl oauth2UserService, UserDetailsServiceImpl userDetailsService,
                                    PasswordEncoder passwordEncoder, JwtConfig jwtConfig, JwtUtils jwtUtils) {
        this.oauth2UserService = oauth2UserService;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.jwtConfig = jwtConfig;
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(new JwtAuthenticationFilter(authenticationManager(), jwtConfig, jwtUtils))
                .addFilterAfter(new JwtTokenFilter(jwtConfig, jwtUtils), JwtAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers("/api/*/users/register",
                        "/oauth2/**",
                        "/api/*/users/login/oauth").permitAll()
                .anyRequest().authenticated()
                .and()
                .oauth2Login()
                    .loginPage("/oauth2/authorization/google")
                    .userInfoEndpoint()
                        .userService(oauth2UserService)
                .and()
                .successHandler((request, response, authentication) -> {
                    OAuth2UserImpl oauthUser = (OAuth2UserImpl) authentication.getPrincipal();

                    oauth2UserService.postLogin(oauthUser.getEmail());
                });
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }

}
