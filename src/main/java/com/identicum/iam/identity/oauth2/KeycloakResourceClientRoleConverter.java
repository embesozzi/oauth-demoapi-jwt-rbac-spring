package com.identicum.iam.identity.oauth2;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

@Configuration
public class KeycloakResourceClientRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

	public static final String RESOURCE_ACCESS = "resource_access";
	public static final String ROLES = "roles";
	public static final String ROLE_PREFIX = "ROLE_";

	private String roleClientId;
    private final Logger log = LoggerFactory.getLogger(this.getClass());

	private Converter<Jwt, Collection<GrantedAuthority>> jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

	public void setRoleClientId(String roleClientId) {
		this.roleClientId = roleClientId;
	}

	/**
	 *  Parse the jwt values for the keycloak client role and add as Spring GrantedAuthority
	 *  We expect the following format:
	 *  "resource_access": {
	 *         "{client-id}": {
	 *             "roles": [
	 *                 "write.product",
	 *                 "read.product",
	 *                 "delete.product",
	 *             ]
	 *         }
	 * @param jwt
	 * @return Collection of GrantedAuthority with values of the client roles
	 */

	@SuppressWarnings("unchecked")
	@Override
	public Collection<GrantedAuthority> convert(final Jwt jwt) {
        log.debug("Parsing JWT claims {}", jwt.getTokenValue());
		Collection<GrantedAuthority> collection  = jwtGrantedAuthoritiesConverter.convert(jwt);

        Map<String, Object> resourceAccess = (Map<String, Object>) jwt.getClaims().get(RESOURCE_ACCESS);

        log.debug("Check if claim resource_access contains client {} , resource: {} ", roleClientId, resourceAccess);
        if(!resourceAccess.containsKey(roleClientId)){
        	log.warn("Client {} doesn't have any role ",roleClientId);
			return collection;
		}

		resourceAccess = (Map<String, Object>) resourceAccess.get(roleClientId);
		log.debug("Parsing client {} roles {} ", roleClientId, resourceAccess);

		Collection<GrantedAuthority> roles = ((List<String>) resourceAccess.get(ROLES)).stream()
			.map(roleName -> ROLE_PREFIX + roleName)
			.map(SimpleGrantedAuthority::new)
			.collect(Collectors.toList());

		log.debug("Adding roles granted authority {}" , roles.toString());

		collection.addAll(roles);

		return collection;
	}
}