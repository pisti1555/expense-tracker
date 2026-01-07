package hu.projects.expense_tracker.middlewares;

import hu.projects.expense_tracker.common.exceptions.*;
import hu.projects.expense_tracker.common.models.error_responses.ErrorResponse;
import hu.projects.expense_tracker.common.models.error_responses.ValidationErrorResponse;
import hu.projects.expense_tracker.services.error_response_provider.EnvironmentBasedErrorResponseProvider;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

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
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequest(BadRequestException ex) {
        return errorResponseProvider.convertException(ex, ex.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleUnauthorized(UnauthorizedException ex) {
        return errorResponseProvider.convertException(ex, ex.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleForbidden(ForbiddenException ex) {
        return errorResponseProvider.convertException(ex, ex.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleTypeMismatch(TypeMismatchException ex) {
        var badRequestEx = new BadRequestException(ex.getMessage());
        return errorResponseProvider.convertException(badRequestEx, "Data type mismatch.");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorResponse handleValidation(BindException ex) {
        var errors = new HashMap<String, Collection<String>>();

        ex.getBindingResult().getFieldErrors().forEach(e -> {
            String fieldName = e.getField();
            errors.putIfAbsent(fieldName, new ArrayList<>());

            if (e.getCode().equals("typeMismatch")) {
                errors.get(fieldName).add("Invalid data type.");
            } else {
                errors.get(fieldName).add(e.getDefaultMessage());
            }
        });

        return new ValidationErrorResponse("Validation failed.", errors, HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler
    public ErrorResponse handleNoResourceFoundException(NoResourceFoundException ex) {
        var notFoundEx = new NotFoundException(ex.getMessage());
        return errorResponseProvider.convertException(notFoundEx, "No resource found.");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleInternalServerError(Throwable ex) {
        var unexpectedException = new UnexpectedException(ex.getMessage());
        return errorResponseProvider.convertException(unexpectedException, "An error occurred on the server side :(");
    }
}
