package hu.projects.expense_tracker.common.exceptions;

import org.springframework.http.HttpStatus;

public class NotFoundException extends ApiException {
    public NotFoundException(String message) {
        super(
                "Not Found",
                HttpStatus.NOT_FOUND.value(),
                message
        );
    }
}
