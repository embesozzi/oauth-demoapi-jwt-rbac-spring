# oauth-demoapi-jwt-rbac-spring
Spring OAuth demo api acting as [OAuth2 Resource Server](https://docs.spring.io/spring-security/site/docs/current/reference/html5/#oauth2resourceserver).

The API Protection is based on user's Keycloak Client Role in the access token (jwt) following the Open API specs for the Product API.
See  [v1-openapi.yml](/src/main/resources/v1-openapi.yml)

## Run the API

### Run locally

* Clone this repository
```
git clone https://github.com/embesozzi/oauth-demoapi-jwt-rbac-spring.git
```
- Adjust the [aplication.yml](/src/main/resources/application.yml)
- Run the app
```
mvn spring-boot:run
```
- You can access to the API on http://hostname:8081/api/products
