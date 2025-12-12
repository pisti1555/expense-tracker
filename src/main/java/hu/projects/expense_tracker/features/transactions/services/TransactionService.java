package hu.projects.expense_tracker.features.transactions.services;

import hu.projects.expense_tracker.features.transactions.dtos.CreateTransactionDto;
import hu.projects.expense_tracker.features.transactions.dtos.TransactionDto;
import hu.projects.expense_tracker.common.pagination.PaginationAttributes;
import org.springframework.data.domain.Page;

public interface TransactionService {
    TransactionDto createTransaction(CreateTransactionDto dto, Long userId);
    void deleteTransactionById(Long id, Long userId);

    TransactionDto getTransactionById(Long id, Long userId);
    Page<TransactionDto> getTransactionsPaged(Long userId, PaginationAttributes attributes);
}
