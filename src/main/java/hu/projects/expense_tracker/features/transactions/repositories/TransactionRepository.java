package hu.projects.expense_tracker.features.transactions.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import hu.projects.expense_tracker.features.transactions.entities.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query("SELECT t FROM Transaction t WHERE t.user.username = :username ORDER BY t.createdAt DESC")
    Page<Transaction> findPagedByUsername(String username, Pageable pageable);
}
