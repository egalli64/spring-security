# Spring Security Tutorial

## Branches

- 1.1 - No security
- 1.2 - Default security
- 1.3 - HTTP Basic Authentication
- 1.5 - Form-Based Login

## 1.5 - Form-Based Login

Login for classic web application through HTML form

### Changes in the Controller

- The endpoint /login redirects to the ThymeLeaf template login.html

### ThymeLeaf template login.html

- Provide a POST form
- It should provide username and password
- The endpoint should be known to Security

### Changes in the Security Configuration

- HTTP Basic is disabled
- Form login is enabled, by calling .formLogin(), to specify:
	- The login page endpoint
	- The endpoint of the POST request from login
	- Where to redirect in case of success and failure
	- Which roles have access to it
