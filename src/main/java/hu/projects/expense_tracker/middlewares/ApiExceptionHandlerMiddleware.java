package hu.projects.expense_tracker.middlewares;

import hu.projects.expense_tracker.common.exceptions.NotFoundException;
import hu.projects.expense_tracker.common.exceptions.UnexpectedException;
import hu.projects.expense_tracker.common.models.error_responses.ErrorResponse;
import hu.projects.expense_tracker.services.error_response_provider.EnvironmentBasedErrorResponseProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandlerMiddleware {
    private final EnvironmentBasedErrorResponseProvider errorResponseProvider;

    @Autowired
    public ApiExceptionHandlerMiddleware(EnvironmentBasedErrorResponseProvider errorResponseProvider) {
        this.errorResponseProvider = errorResponseProvider;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFound(NotFoundException ex) {
        return errorResponseProvider.convertException(ex, ex.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleInternalServerError(Throwable ex) {
        var unexpectedException = new UnexpectedException(ex.getMessage());
        return errorResponseProvider.convertException(unexpectedException, "An error occurred on the server side :(");
    }
}
