package hu.projects.expense_tracker.features.transactions.services;

import hu.projects.expense_tracker.common.exceptions.NotFoundException;
import hu.projects.expense_tracker.common.pagination.PagedResult;
import hu.projects.expense_tracker.common.validations.app_validator_services.PageableValidator;
import hu.projects.expense_tracker.features.transactions.dtos.CreateTransactionDto;
import hu.projects.expense_tracker.features.transactions.dtos.TransactionDto;
import hu.projects.expense_tracker.features.transactions.entities.Transaction;
import hu.projects.expense_tracker.features.transactions.enums.TransactionCategory;
import hu.projects.expense_tracker.features.transactions.repositories.TransactionRepository;
import hu.projects.expense_tracker.features.users.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
    public TransactionDto createTransaction(CreateTransactionDto dto, String username) {
        var user = userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("User not found."));
        var categorySlug = dto.categorySlug();
        var category = TransactionCategory.getCategoryBySlugOrThrow(categorySlug);

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
    public PagedResult<TransactionDto> getTransactionsPaged(String username, Pageable pageable) {
        PageableValidator.throwIfSortInvalid(pageable, List.of("id", "createdAt", "amount"));
        var transactions = transactionRepository.findPagedByUsername(username, pageable);
        return PagedResult.create(transactions.map(Transaction::toDto));
    }

    private Transaction getTransactionOrThrowNotFound(Long id, String username) {
        var transaction = transactionRepository.findById(id).orElseThrow(() -> new NotFoundException("Transaction not found."));
        if (!transaction.getUser().getUsername().equals(username)) throw new NotFoundException("Transaction not found.");
        return transaction;
    }
}
