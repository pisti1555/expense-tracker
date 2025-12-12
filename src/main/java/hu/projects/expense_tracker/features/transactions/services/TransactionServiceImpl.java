package hu.projects.expense_tracker.features.transactions.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import hu.projects.expense_tracker.common.exceptions.NotFoundException;
import hu.projects.expense_tracker.features.transactions.dtos.CreateTransactionDto;
import hu.projects.expense_tracker.features.transactions.dtos.TransactionDto;
import hu.projects.expense_tracker.features.transactions.entities.Transaction;
import hu.projects.expense_tracker.features.transactions.enums.TransactionCategory;
import hu.projects.expense_tracker.features.transactions.repositories.TransactionRepository;
import hu.projects.expense_tracker.features.users.repositories.UserRepository;
import hu.projects.expense_tracker.common.pagination.PaginationAttributes;
import java.util.Arrays;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionServiceImpl(UserRepository userRepository, TransactionRepository transactionRepository) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public TransactionDto createTransaction(CreateTransactionDto dto, Long userId) {
        var user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found."));
        var category = getCategoryOrThrowNotFound(dto.category(), dto.isExpense());

        var transaction = new Transaction(user, category, dto.amount());
        var savedTransaction = transactionRepository.save(transaction);
        return Transaction.toDto(savedTransaction);
    }

    @Override
    public void deleteTransactionById(Long id, Long userId) {
        var transaction = getTransactionOrThrowNotFound(id, userId);
        transactionRepository.delete(transaction);
    }

    @Override
    public TransactionDto getTransactionById(Long id, Long userId) {
        var transaction = getTransactionOrThrowNotFound(id, userId);
        return Transaction.toDto(transaction);
    }

    @Override
    public List<TransactionDto> getTransactionsPaged(Long userId, PaginationAttributes attributes) {
        var pageable = Pageable.ofSize(attributes.size()).withPage(attributes.page() - 1);
        var transactions = transactionRepository.findPagedByUserId(userId, pageable);
        return transactions.stream().map(Transaction::toDto).toList();
    }

    private Transaction getTransactionOrThrowNotFound(Long id, Long userId) {
        var transaction = transactionRepository.findById(id).orElseThrow(() -> new NotFoundException("Transaction not found."));
        if (!transaction.getUser().getId().equals(userId)) throw new NotFoundException("Transaction not found.");
        return transaction;
    }

    private TransactionCategory getCategoryOrThrowNotFound(String category, boolean isExpense) {
        return Arrays.stream(TransactionCategory.values())
                .filter(c -> c.getDisplayName().equals(category) && c.isExpense() == isExpense)
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Category not found."));
    }
}
