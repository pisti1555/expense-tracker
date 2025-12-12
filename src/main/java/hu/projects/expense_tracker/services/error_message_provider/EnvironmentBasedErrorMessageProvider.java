package hu.projects.expense_tracker.services.error_message_provider;

public interface EnvironmentBasedErrorMessageProvider {
    String getErrorMessage(String defaultMessage, String customMessage);
}
