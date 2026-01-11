package factories;

import hu.projects.expense_tracker.features.reports.dtos.MonthlyReportDto;
import hu.projects.expense_tracker.features.transactions.entities.Transaction;
import hu.projects.expense_tracker.features.transactions.enums.TransactionCategory;
import hu.projects.expense_tracker.features.users.entities.User;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

public class ReportFactory {
    public static MonthlyReportDto create(List<LocalDateTime> dates, User user) {
        var transactions = TransactionFactory.createMultiple(dates, user);

        var income = transactions.stream()
                .filter(t -> !t.getCategory().isExpense())
                .mapToDouble(Transaction::getAmount)
                .sum();

        var expense = transactions.stream()
                .filter(t -> t.getCategory().isExpense())
                .mapToDouble(Transaction::getAmount)
                .sum();

        var expensesByCategory = new HashMap<TransactionCategory, Double>();
        transactions.stream()
                .filter(t -> t.getCategory().isExpense())
                .forEach(t -> {
                    expensesByCategory.putIfAbsent(t.getCategory(), 0.0);
                    expensesByCategory.put(t.getCategory(), expensesByCategory.get(t.getCategory()) + t.getAmount());
                });

        return new MonthlyReportDto(
                dates.getFirst().getYear(), dates.getFirst().getMonthValue(), income, expense,
                transactions.stream().map(Transaction::toDto).toList(), expensesByCategory);
    }
}
