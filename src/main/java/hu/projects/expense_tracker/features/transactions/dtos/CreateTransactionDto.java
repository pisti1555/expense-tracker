package hu.projects.expense_tracker.features.transactions.dtos;

public record CreateTransactionDto(
        String category,
        boolean isExpense,
        double amount
) { }