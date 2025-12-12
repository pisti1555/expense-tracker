package hu.projects.expense_tracker.features.transactions.dtos;

public record TransactionDto(
        Long id,
        Long userId,
        String category,
        boolean isExpense,
        double amount,
        String createdAt
) {}