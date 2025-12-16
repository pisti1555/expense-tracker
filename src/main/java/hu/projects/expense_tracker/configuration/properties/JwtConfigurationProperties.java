package hu.projects.expense_tracker.configuration.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "spring.security.jwt")
@Getter @Setter
public class JwtConfigurationProperties {
    private String secretKey;
    private long expirationTime;
    private String issuer;
}
