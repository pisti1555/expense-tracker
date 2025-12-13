package hu.projects.expense_tracker.features.transactions.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import hu.projects.expense_tracker.features.transactions.dtos.TransactionDto;
import hu.projects.expense_tracker.features.transactions.enums.TransactionCategory;
import hu.projects.expense_tracker.features.users.entities.User;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions", indexes = {
        @Index(name = "transaction_category_idx", columnList = "category")
})
@Getter
@Setter
@NoArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @NotNull
    private User user;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false, length = 30)
    private TransactionCategory category;

    @NotNull
    @Column(name = "amount", nullable = false)
    private double amount;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public Transaction(User user, TransactionCategory category, double amount) {
        this.user = user;
        this.category = category;
        this.amount = amount;
    }

    public static TransactionDto toDto(Transaction transaction) {
        return new TransactionDto(
                transaction.getId(),
                transaction.getUser().getId(),
                transaction.getCategory().getDisplayName(),
                transaction.getCategory().isExpense(),
                transaction.getAmount(),
                transaction.getCreatedAt().toString()
        );
    }
}