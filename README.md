# Spring Security Tutorial

## Branches

- 1.1 - No security
- 1.2 - Default security
- 1.3 - HTTP Basic Authentication
- 1.5 - Form-Based Login
- 2.2 - Security users management
- 2.3 - Database Authentication Setup
- 2.4 - Method-Level Security
- 2.5 - Advanced Exception Handling
- 2.6 - Advanced URL-based Authorization /1

## 2.6 - Advanced URL-based Authorization /1

- Add a new entity to represent authorities
	- data.sql: new tables authorities and user_authorities
	- SecAuthority (new): JPA entity
	- SecAuthorityRepository (new): JPA repository
	- SecUser: many-to-many relation to authority
	- SecUserDetailsService: a UserDetails can also have (plain) authorities
	- SecUserService: a SecUser can also have (plain) authorities
	- UserController: a new user can also have (plain) authorities

- Add an endpoint accessible only for a given authority
	- SecurityConfig: the filter chain has a request matcher between /reports and a (plain) authority
	- AppController: map /reports endpoint to simple ThymeLeaf template
	
- View changes
	- home.html: link to /reports showed only if the user has the authority to access it
	- user-create.html: available (plain) authorities for new user
	- user-details.html: show (plain) authorities for the current user
