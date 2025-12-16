package hu.projects.expense_tracker.features.auth.entities;

import hu.projects.expense_tracker.features.users.entities.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDateTime;
import java.util.Collection;

@Entity
@Table(name = "authorities")
@Getter @Setter
@NoArgsConstructor
public class AppAuthority implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "authority", nullable = false)
    private String authority;

    @ManyToMany(mappedBy = "authorities")
    private Collection<User> users;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Override
    public @Nullable String getAuthority() {
        return authority;
    }
}
