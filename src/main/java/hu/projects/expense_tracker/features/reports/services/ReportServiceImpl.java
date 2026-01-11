package hu.projects.expense_tracker.features.reports.services;

import hu.projects.expense_tracker.common.exceptions.BadRequestException;
import hu.projects.expense_tracker.common.pagination.PagedResult;
import hu.projects.expense_tracker.common.validations.app_validator_services.PageableValidator;
import hu.projects.expense_tracker.features.reports.dtos.MonthlyReportDto;
import hu.projects.expense_tracker.features.transactions.dtos.TransactionDto;
import hu.projects.expense_tracker.features.transactions.entities.Transaction;
import hu.projects.expense_tracker.features.transactions.enums.TransactionCategory;
import hu.projects.expense_tracker.features.transactions.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class ReportServiceImpl implements ReportService {
    private final TransactionRepository transactionRepository;

    @Autowired
    public ReportServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public MonthlyReportDto getMonthlyReport(String username, int yearFilter, int monthFilter) {
        var dateRange = getDateRangeFromYMFilters(yearFilter, monthFilter);

        var transactions = transactionRepository.findInTimeRangeByUsername(username, dateRange.start, dateRange.end);
        var mappedTransactions = transactions.stream().map(Transaction::toDto).toList();

        var totalIncome = calculateTotalAmount(transactions, false);
        var totalExpense = calculateTotalAmount(transactions, true);

        var expensesByCategory = calculateExpensesInMonthByCategory(transactions);

        return new MonthlyReportDto(
                dateRange.start.getYear(),
                dateRange.start.getMonthValue(),
                totalIncome,
                totalExpense,
                mappedTransactions,
                expensesByCategory
        );
    }

    @Override
    public PagedResult<TransactionDto> getTransactionsInCategory(String username, String category, Pageable pageable) {
        var transactionCategory = TransactionCategory.getCategoryBySlugOrThrow(category);

        PageableValidator.throwIfSortInvalid(pageable, List.of("id", "createdAt", "amount"));

        var page = transactionRepository
                .findInCategoryByUsername(username, transactionCategory, pageable)
                .map(Transaction::toDto);

        return PagedResult.create(page);
    }

    record DateRange(LocalDateTime start, LocalDateTime end) {}

    private DateRange getDateRangeFromYMFilters(int year, int month) {
        var now = LocalDateTime.now();
        var nowYear = now.getYear();
        var nowMonth = now.getMonthValue();

        if (year == nowYear && month > nowMonth) throw new BadRequestException("Month cannot be in the future.");

        var startDate = LocalDateTime.of(year, month, 1, 0, 0);
        var endDate = month == 12 ?
                LocalDateTime.of(year + 1, 1, 1, 0, 0) :
                LocalDateTime.of(year, month + 1, 1, 0, 0);

        return new DateRange(startDate, endDate);
    }

    private double calculateTotalAmount(Collection<Transaction> transactions, boolean expense) {
        return transactions.stream()
                .filter(t -> t.getCategory().isExpense() == expense)
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    private Map<TransactionCategory, Double> calculateExpensesInMonthByCategory(Collection<Transaction> transactions) {
        var expenses = new HashMap<TransactionCategory, Double>();
        transactions.stream()
                .filter(t -> t.getCategory().isExpense())
                .forEach(t -> {
                    expenses.putIfAbsent(t.getCategory(), 0.0);
                    expenses.put(t.getCategory(), expenses.get(t.getCategory()) + t.getAmount());
                });
        return expenses;
    }
}
