# Spring Security Tutorial

## Branches

- 1.1 - No security
- 1.2 - Default security
- 1.3 - HTTP Basic Authentication
- 1.4 - Form-Based Login
- 2.2 - Security users management
- 2.3 - Database Authentication Setup
- 2.4 - Method-Level Security

## 2.4 - Method-Level Security

- Method-Level Security activation
	- SecurityConfig: annotated also with @EnableMethodSecurity
- @PreAuthorize
	- SecUserService: using SpEL hasRole(), hasAnyRole(), authentication.name == #username"
- @PostAuthorize
	- SecUserService: using also specific returnObject SpEL
- @RolesAllowed
	- SecUserService: if @Secured is used instead, prefix the role name with "ROLE_"
- Flexibility:
	- SecurityConfig: could just require to user to be logged to access some endpoints
		- invoke authenticated() on the endpoint requestMatchers()
		- delegate security check to the service
- AccessDeniedException
	- UserController: check if the service throws an exception
		- Generate an error message
		- Use RedirectAttributes to pass the error message to the redirected endpoint
	