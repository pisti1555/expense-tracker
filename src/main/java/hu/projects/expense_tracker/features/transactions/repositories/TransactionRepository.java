package hu.projects.expense_tracker.features.transactions.repositories;

import hu.projects.expense_tracker.features.transactions.enums.TransactionCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import hu.projects.expense_tracker.features.transactions.entities.Transaction;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query("SELECT t FROM Transaction t WHERE t.user.username = :username")
    Page<Transaction> findPagedByUsername(String username, Pageable pageable);

    @Query("SELECT t FROM Transaction t WHERE t.user.username = :username AND t.createdAt >= :start AND t.createdAt < :end")
    List<Transaction> findInTimeRangeByUsername(String username, LocalDateTime start, LocalDateTime end);

    @Query("SELECT t FROM Transaction t WHERE t.user.username = :username AND t.category = :category")
    Page<Transaction> findInCategoryByUsername(String username, TransactionCategory category, Pageable pageable);
}
