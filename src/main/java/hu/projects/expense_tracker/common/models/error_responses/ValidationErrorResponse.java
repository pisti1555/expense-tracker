package hu.projects.expense_tracker.common.models.error_responses;

import java.util.Collection;
import java.util.Map;

public record ValidationErrorResponse(
        String title,
        Map<String, Collection<String>> errors,
        int status
) implements ErrorResponse {}