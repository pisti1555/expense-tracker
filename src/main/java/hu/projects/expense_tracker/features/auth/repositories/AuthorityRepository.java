package hu.projects.expense_tracker.features.auth.repositories;

import hu.projects.expense_tracker.features.auth.entities.AppAuthority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorityRepository extends JpaRepository<AppAuthority, Long> {
    Optional<AppAuthority> findByAuthority(String authority);
}
