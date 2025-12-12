package hu.projects.expense_tracker.features.users.dtos;

import hu.projects.expense_tracker.features.transactions.dtos.TransactionDto;
import java.util.List;

public record UserDto(
        Long id,
        List<TransactionDto> transactions,
        String createdAt
) {}