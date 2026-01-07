package hu.projects.expense_tracker.features.reports.services;

import hu.projects.expense_tracker.common.pagination.PaginationAttributes;
import hu.projects.expense_tracker.features.reports.dtos.MonthlyReportDto;
import hu.projects.expense_tracker.features.transactions.dtos.TransactionDto;
import org.springframework.data.domain.Page;

public interface ReportService {
    MonthlyReportDto getMonthlyReport(String username, int yearFilter, int monthFilter);
    Page<TransactionDto> getTransactionsInCategory(String username, String category, PaginationAttributes pagination);
}
