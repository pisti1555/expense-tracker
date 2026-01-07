package hu.projects.expense_tracker.common.exceptions;

import org.springframework.http.HttpStatus;

public class ForbiddenException extends ApiException {
    public ForbiddenException(String message) {
        super(
                "Forbidden",
                HttpStatus.FORBIDDEN.value(),
                message
        );
    }
}
