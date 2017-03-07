package com.wolfbeacon.security;

import com.auth0.spring.security.api.JwtWebSecurityConfigurer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity(debug = true)
@Configuration
@ConditionalOnProperty(prefix = "auth0", value = "secret")
public class HSSecurityConfig extends WebSecurityConfigurerAdapter {

    private static Logger logger = LoggerFactory.getLogger(HSSecurityConfig.class);

    @Value("${auth0.issuer}")
    private String issuer;

    @Value("${auth0.audience}")
    private String audience;

    @Value("${auth0.secret}")
    private String secret;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        logger.info("Configuring for Jwt token with iss {} & aud {}", issuer, audience);

        JwtWebSecurityConfigurer.forHS256(audience, issuer, secret.getBytes())
                .configure(http)
                .authorizeRequests()
                .antMatchers("/v1/secured/**").fullyAuthenticated();
    }
}
