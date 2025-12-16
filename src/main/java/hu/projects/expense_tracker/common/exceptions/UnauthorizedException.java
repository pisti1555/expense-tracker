package hu.projects.expense_tracker.common.exceptions;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends ApiException {
    public UnauthorizedException(String message) {
        super(
                "Unauthorized",
                HttpStatus.UNAUTHORIZED.value(),
                message
        );
    }
}
