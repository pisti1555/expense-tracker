package hu.projects.expense_tracker.features.transactions.services;

import hu.projects.expense_tracker.common.pagination.PagedResult;
import hu.projects.expense_tracker.features.transactions.dtos.CreateTransactionDto;
import hu.projects.expense_tracker.features.transactions.dtos.TransactionDto;
import hu.projects.expense_tracker.common.pagination.PaginationAttributes;

public interface TransactionService {
    TransactionDto createTransaction(CreateTransactionDto dto, String username);
    void deleteTransactionById(Long id, String username);

    TransactionDto getTransactionById(Long id, String username);
    PagedResult<TransactionDto> getTransactionsPaged(String username, PaginationAttributes attributes);
}
