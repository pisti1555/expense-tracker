package hu.projects.expense_tracker.common.exceptions;

import org.springframework.http.HttpStatus;

public class UnexpectedException extends ApiException {
    public UnexpectedException(String message) {
        super(
                "Internal Server Error",
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                message
        );
    }
}
