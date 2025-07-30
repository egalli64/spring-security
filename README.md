# Spring Security Tutorial

## Branches

- 1.1 - No security
- 1.2 - Default security
- 1.3 - HTTP Basic Authentication

## 1.3 - HTTP Basic Authentication

Let's override the Spring Security auto-configuration:
- No login form, basic authorization dialog is shown instead
- Credentials are sent with every request
- Allows public endpoints
- What is not explicitly allowed is forbidden

### See the class SecurityConfig
- Annotated @Configuration @EnableWebSecurity
- SecurityFilterChain bean
	- Generates a DefaultSecurityFilterChain
	- Configured for HTTP Basic Authentication by .httpBasic()
	- Disabling form-based authentication by .formLogin()
	- Authorizing users / roles on given paths by .authorizeHttpRequests()
		- Using requestMatchers(), permitAll(), hasRole(), anyRequest(), denyAll()
	- It is possible to disable (partially) CSRF when required
- UserDetailsService bean
	- Generates a InMemoryUserDetailsManager with predefined users / roles
- PasswordEncoder bean
	- Generates a BCrypt password encoder
	- Used by the UserDetailsService bean

## Security Headers

Basic Auth automatically adds headers:
- WWW-Authenticate: Basic realm="Spring Security Tutorial"
- Standard security headers (X-Frame-Options, etc.)
