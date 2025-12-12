## Table of contents
- [Overview](#overview)
- [Tech stack](#tech-stack)
- [Features by routes](#features-by-routes)
- [Usage](#usage)
    - [Versioning](#versioning)
    - [Pagination](#pagination)

## Overview
Java - Spring Boot 4 REST API for managing expenses.

## Tech stack
- Java 25 with Spring Boot 4
- PostgreSQL

## Features by routes
### Transactions
- Add transaction
- Remove transaction
- Get all transactions (paged)
- Get transaction by ID

## Usage
### Versioning
The version number must be passed in the API-Version header and is optional. 
The default version is 1.0.

### Pagination
When making a GET request for multiple elements, the API returns a Page type response. Specifying the pagination params are optional, but then
the default values are being used which are page=0 and size=10. If the maximum value of size (50) is exceeded, then the maximum is used instead.

Important note: The page number starts from 0, not 1.

So for example, you can see the value of "first" is false in the response body of this request: /api/transactions?page=1&size=20,
because page=1 refers to the second page.

    {
        "content": [],
        "empty": true,
        "first": false,
        "last": true,
        "number": 1,
        "numberOfElements": 0,
        "pageable": {
            "offset": 20,
            "pageNumber": 1,
            "pageSize": 20,
            "paged": true,
            "sort": {
                "empty": true,
                "sorted": false,
                "unsorted": true
            },
            "unpaged": false
        },
        "size": 20,
        "sort": {
            "empty": true,
            "sorted": false,
            "unsorted": true
        },
        "totalElements": 0,
        "totalPages": 0
    }
