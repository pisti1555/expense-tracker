package factories;

import hu.projects.expense_tracker.features.users.entities.User;

import java.time.LocalDateTime;

public class UserFactory extends TestDataFactory {
    public static User create() {
        var user = new User(
                faker.credentials().username(),
                passwordEncoder.encode("abc123")
        );
        user.setId(1L);
        user.setCreatedAt(LocalDateTime.now());

        return user;
    }
}
