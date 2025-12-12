package hu.projects.expense_tracker.services.error_message_provider;

import hu.projects.expense_tracker.common.exceptions.ApiException;
import hu.projects.expense_tracker.common.models.error_responses.ErrorResponse;
import hu.projects.expense_tracker.common.models.error_responses.DevelopmentErrorResponse;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.io.PrintWriter;
import java.io.StringWriter;

@Service
@Profile("dev")
public class DevelopmentErrorResponseProvider implements EnvironmentBasedErrorResponseProvider {
    @Override
    public ErrorResponse convertException(ApiException exception, String customMessage) {
        StringWriter stackTraceStringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stackTraceStringWriter);
        exception.printStackTrace(printWriter);

        return new DevelopmentErrorResponse(
                exception.getTitle(),
                exception.getMessage(),
                exception.getStatus(),
                exception.getTimestamp(),
                stackTraceStringWriter.toString()
        );
    }
}
