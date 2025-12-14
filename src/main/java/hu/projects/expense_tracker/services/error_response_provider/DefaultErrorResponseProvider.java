package hu.projects.expense_tracker.services.error_response_provider;

import hu.projects.expense_tracker.common.exceptions.ApiException;
import hu.projects.expense_tracker.common.models.error_responses.ErrorResponse;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile({"!production & !dev"})
public class DefaultErrorResponseProvider implements EnvironmentBasedErrorResponseProvider {
    @Override
    public ErrorResponse convertException(ApiException exception, String customMessage) {
        return new ProductionErrorResponseProvider().convertException(exception, customMessage);
    }
}
