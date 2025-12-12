package hu.projects.expense_tracker.middlewares;

import hu.projects.expense_tracker.common.exceptions.ApiException;
import hu.projects.expense_tracker.common.exceptions.NotFoundException;
import hu.projects.expense_tracker.services.error_message_provider.EnvironmentBasedErrorMessageProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ApiExceptionHandler {
    private final EnvironmentBasedErrorMessageProvider errorMessageProvider;

    @Autowired
    public ApiExceptionHandler(EnvironmentBasedErrorMessageProvider errorMessageProvider) {
        this.errorMessageProvider = errorMessageProvider;
    }

    private record ApiErrorResponse(
            String title,
            String message,
            int status,
            String timestamp
    ) {}

    private static ApiErrorResponse createErrorResponse(ApiException ex) {
        return new ApiErrorResponse(ex.getTitle(), ex.getMessage(), ex.getStatus(), ex.getTimestamp());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrorResponse handleNotFound(NotFoundException ex) {
        return createErrorResponse(ex);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiErrorResponse handleInternalServerError(Throwable ex) {
        return new ApiErrorResponse(
                "Internal Server Error",
                errorMessageProvider.getErrorMessage(ex.getMessage(), "An unexpected error occurred."),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                LocalDateTime.now().toString()
        );
    }
}
