package hu.projects.expense_tracker.services.error_message_provider;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("dev")
public class DevelopmentErrorMessageProvider implements EnvironmentBasedErrorMessageProvider {
    @Override
    public String getErrorMessage(String defaultMessage, String customMessage) {
        return defaultMessage;
    }
}
