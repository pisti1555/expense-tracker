package hu.projects.expense_tracker.features.reports.dtos;

import hu.projects.expense_tracker.features.transactions.dtos.TransactionDto;
import hu.projects.expense_tracker.features.transactions.enums.TransactionCategory;

import java.util.Collection;
import java.util.Map;

public record MonthlyReportDto(
        int year,
        int month,
        double totalIncomeAmount,
        double totalExpenseAmount,
        Collection<TransactionDto> transactions,
        Map<TransactionCategory, Double> expensesByCategory
) {}
