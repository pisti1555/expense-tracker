package hu.projects.expense_tracker.exceptions;

import lombok.Getter;

public class NotFoundException extends RuntimeException {
    @Getter
    private final int status = 404;
    public NotFoundException(String message) {
        super(message);
    }
}
