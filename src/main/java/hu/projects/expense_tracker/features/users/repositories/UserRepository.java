package hu.projects.expense_tracker.features.users.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import hu.projects.expense_tracker.features.users.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}