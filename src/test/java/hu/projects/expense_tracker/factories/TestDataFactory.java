package hu.projects.expense_tracker.factories;

import hu.projects.expense_tracker.features.users.entities.User;
import net.datafaker.Faker;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public abstract class TestDataFactory {
    protected static final Faker faker = new Faker();
    protected static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
}
