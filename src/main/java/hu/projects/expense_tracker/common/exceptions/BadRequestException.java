package hu.projects.expense_tracker.common.exceptions;

import org.springframework.http.HttpStatus;

public class BadRequestException extends ApiException {
    public BadRequestException(String message) {
        super(
                "Bad Request",
                HttpStatus.BAD_REQUEST.value(),
                message
        );
    }
}
