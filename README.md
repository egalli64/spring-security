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
- 2.7 - Advanced URL-based Authorization /2

## 2.7 - Advanced URL-based Authorization /2

- Add a admin user with no VIEW_REPORTS authority
	- data.sql: new user admin2

- Add an endpoint accessible only for a given role/authority
	- SecurityConfig: add a request matcher between /admin/reports and role + authority
	- AppController: map /admin/reports endpoint to simple ThymeLeaf template	
	- home.html: link to /admin/reports showed only if the user has the role + authority to access it
