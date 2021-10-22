# oauth-demoapi-jwt-rbac-spring
Spring OAuth demo api acting as [OAuth2 Resource Server](https://docs.spring.io/spring-security/site/docs/current/reference/html5/#oauth2resourceserver).

The API Protection based on the Open API specs checks if the user has the required claim Keycloak Client Role in the access token (JWT). See [v1-openapi.yml](/src/main/resources/v1-openapi.yml)

We also developed a custom Converter for parsing the Keycloak client roles claim in the jwt token. The claim presents the following json structure: 
```
"resource_access": {
  "{client-id}": {
    "roles": [
      "{role-1}",
      "{role-2}",
    ]
  }
}  
```
For more detail see: [aplication.yml](/src/main/resources/application.yml).


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
