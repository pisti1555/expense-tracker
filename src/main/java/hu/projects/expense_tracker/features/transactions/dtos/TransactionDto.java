package hu.projects.expense_tracker.features.transactions.dtos;

public record TransactionDto(
        Long id,
        String categoryName,
        String categorySlug,
        boolean isExpense,
        double amount,
        String createdAt
) {}