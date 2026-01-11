package hu.projects.expense_tracker.factories;

import hu.projects.expense_tracker.features.users.entities.User;

public class UserFactory extends TestDataFactory {
    public static User create() {
        return new User(
                faker.credentials().username(),
                passwordEncoder.encode("abc123")
        );
    }
}
