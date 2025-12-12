package hu.projects.expense_tracker.common.exceptions;

import lombok.Getter;

import java.time.LocalDateTime;

public abstract class ApiException extends RuntimeException {
    @Getter
    private final String title;
    @Getter
    private final int status;
    @Getter
    private final String timestamp;

    public ApiException(String title, int status, String message) {
        super(message);
        this.title = title;
        this.status = status;
        this.timestamp = LocalDateTime.now().toString();
    }
}