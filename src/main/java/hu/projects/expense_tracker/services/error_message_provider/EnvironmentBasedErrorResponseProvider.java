package hu.projects.expense_tracker.services.error_message_provider;

import hu.projects.expense_tracker.common.exceptions.ApiException;
import hu.projects.expense_tracker.common.models.error_responses.ErrorResponse;

public interface EnvironmentBasedErrorResponseProvider {
    ErrorResponse convertException(ApiException exception, String customMessage);
}