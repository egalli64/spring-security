# Spring Security Tutorial

## Branches

- 1.1 - No security
- 1.2 - Default security
- 1.3 - HTTP Basic Authentication
- 1.4 - Form-Based Login
- 2.2 - Security users management

## 2.2 - Security users management

- Define a JavaBean, SecUser, to represent the user in the business logic
	- Usually unrelated to UserDetails, user as seen by security
- Define a @Repository, UserRepository, to manage users
	- Currently based on HashMap, would be replaced with a database for production code
- Define a @Service, SecUserDetailsService, implementing UserDetailsService
	- Mapping between business and security user
- Update the @Configuration @EnableWebSecurity class, SecurityConfig
	- The custom UserDetailsService is injected in and passed to the SecurityFilterChain
	- More endpoints accessible to ADMIN role
	- User creation(/management) delegated to the user repository
- Extract PasswordEncoder @Bean factory from SecurityConfig to PasswordEncoderConfig
	- Avoid cyclic bean dependency in the simplest way

### ThymeLeaf templates

Edited:
- home: /users endopoint

Created:
- user-list: list of existing users
- user-create: create a user
- user-details: view/modify a user
