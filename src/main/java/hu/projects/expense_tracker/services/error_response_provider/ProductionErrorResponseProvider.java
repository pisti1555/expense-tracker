package hu.projects.expense_tracker.services.error_response_provider;

import hu.projects.expense_tracker.common.exceptions.ApiException;
import hu.projects.expense_tracker.common.models.error_responses.ErrorResponse;
import hu.projects.expense_tracker.common.models.error_responses.ProductionErrorResponse;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("production")
public class ProductionErrorResponseProvider implements EnvironmentBasedErrorResponseProvider {
    @Override
    public ErrorResponse convertException(ApiException exception, String customMessage) {
        return new ProductionErrorResponse(
                exception.getTitle(),
                customMessage,
                exception.getStatus()
        );
    }
}
