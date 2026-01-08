package hu.projects.expense_tracker.features.reports.services;

import hu.projects.expense_tracker.common.pagination.PagedResult;
import hu.projects.expense_tracker.common.pagination.PaginationAttributes;
import hu.projects.expense_tracker.features.reports.dtos.MonthlyReportDto;
import hu.projects.expense_tracker.features.transactions.dtos.TransactionDto;

public interface ReportService {
    MonthlyReportDto getMonthlyReport(String username, int yearFilter, int monthFilter);
    PagedResult<TransactionDto> getTransactionsInCategory(String username, String category, PaginationAttributes pagination);
}
