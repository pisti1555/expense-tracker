package hu.projects.expense_tracker.features.reports.controllers;

import hu.projects.expense_tracker.common.pagination.PaginationAttributes;
import hu.projects.expense_tracker.features.reports.dtos.MonthlyReportDto;
import hu.projects.expense_tracker.common.filters.YMFilter;
import hu.projects.expense_tracker.features.reports.services.ReportService;
import hu.projects.expense_tracker.features.transactions.dtos.TransactionDto;
import hu.projects.expense_tracker.services.http.HttpService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<Page<TransactionDto>> getTransactionInCategory(
            Authentication authentication,
            @PathVariable String category,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "0") int page
    ) {
        var pagination = new PaginationAttributes(size, page);
        var transactions = reportService.getTransactionsInCategory(authentication.getName(), category, pagination);
        var headers = HttpService.GeneratePaginationHeaders(transactions);
        return ResponseEntity.ok().headers(headers).body(transactions);
    }
}
