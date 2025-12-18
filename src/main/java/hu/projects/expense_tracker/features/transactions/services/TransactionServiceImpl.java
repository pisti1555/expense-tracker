package hu.projects.expense_tracker.features.transactions.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
    public TransactionDto createTransaction(CreateTransactionDto dto, String username) {
        var user = userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("User not found."));
        var category = getCategoryOrThrowNotFound(dto.category(), dto.isExpense());

        var transaction = new Transaction(user, category, dto.amount());
        var savedTransaction = transactionRepository.save(transaction);
        return Transaction.toDto(savedTransaction);
    }

    @Override
    public void deleteTransactionById(Long id, String username) {
        var transaction = getTransactionOrThrowNotFound(id, username);
        transactionRepository.delete(transaction);
    }

    @Override
    public TransactionDto getTransactionById(Long id, String username) {
        var transaction = getTransactionOrThrowNotFound(id, username);
        return Transaction.toDto(transaction);
    }

    @Override
    public Page<TransactionDto> getTransactionsPaged(String username, PaginationAttributes attributes) {
        var pageable = Pageable.ofSize(attributes.size()).withPage(attributes.page());
        var transactions = transactionRepository.findPagedByUsername(username, pageable);
        return transactions.map(Transaction::toDto);
    }

    private Transaction getTransactionOrThrowNotFound(Long id, String username) {
        var transaction = transactionRepository.findById(id).orElseThrow(() -> new NotFoundException("Transaction not found."));
        if (!transaction.getUser().getUsername().equals(username)) throw new NotFoundException("Transaction not found.");
        return transaction;
    }

    private TransactionCategory getCategoryOrThrowNotFound(String category, boolean isExpense) {
        return Arrays.stream(TransactionCategory.values())
                .filter(c -> c.getDisplayName().equals(category) && c.isExpense() == isExpense)
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Category not found."));
    }
}
