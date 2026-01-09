## Table of contents
- [Overview](#overview)
- [Tech stack](#tech-stack)
- [Features by routes](#features-by-routes)
- [Usage](#usage)
    - [API documentation](#api-documentation)
    - [Authentication](#authentication)
    - [Versioning](#versioning)
    - [Pagination](#pagination)

## Overview
Java - Spring Boot 4 REST API for managing expenses.

## Tech stack
- Java 25 with Spring Boot 4
- PostgreSQL

## Features by routes
### Auth
- Login
- Register

### Transactions
- Add transaction
- Remove transaction
- Get all transactions (paged)
- Get transaction by ID

### Reports
- Get monthly reports
- Get reports by transaction category

## Usage
### API documentation
API documentation is available at:
- {Base URL}/scalar → Scalar web UI for API docs.
- {Base URL}/v3/api-docs → API docs in JSON format.

### Authentication
There is JWT authentication implemented.
The JWT token must be passed in the Authorization header → Bearer { token }.

### Versioning
The version number must be passed in the API-Version header and is optional. 
The default version is 1.0.

### Pagination
When making a GET request for multiple elements, the API returns a PagedList type response. Specifying the pagination params are optional, but then
the default values are being used which are page=0 and size=10. If the maximum value of size (50) is exceeded, then the maximum is used instead.

Important note: The page number starts from 0, not 1.

So for example, you can see the value of "page" is 1 and "hasPrevious" is true in the response body of this request: /api/transactions?page=1&size=20,
because page=1 refers to the second page.

    {
        "hasNext": false,
        "hasPrevious": true,
        "items": [],
        "page": 1,
        "size": 20,
        "totalItems": 3,
        "totalPages": 1
    }
