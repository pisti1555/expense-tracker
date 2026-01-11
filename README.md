## Table of contents
- [Overview](#overview)
- [Tech stack](#tech-stack)
- [Features by routes](#features-by-routes)
- [Usage](#usage)
    - [API documentation](#api-documentation)
    - [Authentication](#authentication)
    - [Versioning](#versioning)
    - [Paging & Sorting](#paging--sorting)

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

### Paging & Sorting
When making a GET request for multiple elements, the API returns a PagedList type response. Specifying the pagination params are optional, but then
the default values are being used which are page=1 and size=10. If the maximum value of size (50) is exceeded, then the maximum is used instead.

Sorting can be achieved by adding the param sort={field name},{direction (asc/desc)}

Example of a paged response: 

    api/transactions?page=1&size=55&sort=createdAt,desc

    {
        "hasNext": false,
        "hasPrevious": false,
        "items": [
            {
                "id": 3,
                "categoryName": "Travel",
                "categorySlug": "travel",
                "isExpense": true,
                "amount": 24000.0,
                "createdAt": "2026-01-09T13:52:56.211807"
            },
            {
                "id": 2,
                "categoryName": "Bills",
                "categorySlug": "bills",
                "isExpense": true,
                "amount": 11000.0,
                "createdAt": "2026-01-09T13:52:47.354522"
            },
            {
                "id": 1,
                "categoryName": "Bills",
                "categorySlug": "bills",
                "isExpense": true,
                "amount": 7600.0,
                "createdAt": "2026-01-09T13:52:37.304648"
            }
        ],
        "page": 1,
        "size": 50,
        "sorted": true,
        "sortedBy": [
            {
                "property": "createdAt",
                "direction": "DESC"
            }
        ],
        "totalItems": 3,
        "totalPages": 1
    }
