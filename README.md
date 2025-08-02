# Spring Security Tutorial

## Branches

- 1.1 - No security
- 1.2 - Default security
- 1.3 - HTTP Basic Authentication
- 1.4 - Form-Based Login
- 2.2 - Security users management
- 2.3 - Database Authentication Setup

## 2.3 - Database Authentication Setup

- Database support
	- POM: add Spring dependencies for DBMS (H2) through JPA
	- application.properties: settings for JPA and the DBMS
	- data.sql (new): H2 bootstrap
	- SecurityConfig: permission to access h2-console (for development)
- JPA
	- SecRole (new): Role entity
	- SecUser: refactored User entity
	- SecRoleRepository (new): refactored Role repository
	- SecUserRepository: refactored User repository
- Service
	- SecUserService (new): refactored out the logic from the repository
	- SecUserDetailsService: use the service instead of accessing the repository
- Controller
	- UserController: use the service instead of accessing the repository
