package hu.projects.expense_tracker.features.reports.controllers;

import hu.projects.expense_tracker.common.pagination.PagedResult;
import hu.projects.expense_tracker.features.reports.dtos.MonthlyReportDto;
import hu.projects.expense_tracker.common.filters.YMFilter;
import hu.projects.expense_tracker.features.reports.services.ReportService;
import hu.projects.expense_tracker.features.transactions.dtos.TransactionDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports")
public class ReportController {
    private final ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping
    public MonthlyReportDto getMonthlyReports(Authentication authentication, @Valid @ModelAttribute YMFilter filter) {
        return reportService.getMonthlyReport(authentication.getName(), filter.year(), filter.month());
    }

    @GetMapping("/{category}")
    public PagedResult<TransactionDto> getTransactionInCategory(
            Authentication authentication,
            @PathVariable String category,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return reportService.getTransactionsInCategory(authentication.getName(), category, pageable);
    }
}
