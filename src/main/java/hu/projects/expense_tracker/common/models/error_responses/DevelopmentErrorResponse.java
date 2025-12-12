package hu.projects.expense_tracker.common.models.error_responses;

public record DevelopmentErrorResponse(
        String title,
        String message,
        int status,
        String timestamp,
        String stackTrace
) implements ErrorResponse {}