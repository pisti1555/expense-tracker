package hu.projects.expense_tracker.common.models.error_responses;

public record ProductionErrorResponse(
        String title,
        String message,
        int status
) implements ErrorResponse {}