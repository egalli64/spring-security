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

- Create the Global Security Exception Handler
	- SecurityExceptionHandler (new): annotated @ControllerAdvice
		- with a method annotated @ExceptionHandler(AccessDeniedException.class)
- The controllers don't have to manage the exception explicitly
	- UserViewController (new): split from UserController
		- delegate AccessDeniedException to the global handler
- The controllers can have their own local @ExceptionHandler, overriding the global one
	- UserController: local exception handler, and even a method with its own handling
