package com.identicum.iam.identity.spring;

import com.identicum.iam.identity.Application;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import com.identicum.iam.identity.oauth2.KeycloakResourceClientRoleConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    @Value("${claim.roles.keycloak.client-id}")
    private String roleClientId;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors()
            .and()
              .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/api/products")
                  .hasAnyRole("read.product","admin")
                .antMatchers(HttpMethod.POST, "/api/products")
                  .hasAnyRole("write.product","admin")
                .anyRequest()
                  .authenticated()
            .and()
              .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {

        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        // grantedAuthoritiesConverter.setAuthorityPrefix("PERMISSION_");
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        KeycloakResourceClientRoleConverter keycloak = new KeycloakResourceClientRoleConverter();
        keycloak.setRoleClientId(roleClientId);
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(keycloak);
        return jwtAuthenticationConverter;
    }
    
}
