package hu.projects.expense_tracker.features.users.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import hu.projects.expense_tracker.features.transactions.entities.Transaction;
import hu.projects.expense_tracker.features.users.dtos.UserDto;
import java.time.LocalDateTime;
import java.util.Collection;

@Entity
@Table(name = "users")
@Getter @Setter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Collection<Transaction> transactions;

    public static UserDto toDto(User user) {
        return new UserDto(
                user.getId(),
                user.getCreatedAt().toString());
    }
}