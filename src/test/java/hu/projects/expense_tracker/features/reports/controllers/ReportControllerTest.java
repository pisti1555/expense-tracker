package hu.projects.expense_tracker.features.reports.controllers;

import bases.ComponentTestBase;
import factories.LocalDateTimeFactory;
import factories.PagedResultFactory;
import factories.ReportFactory;
import factories.TransactionFactory;
import hu.projects.expense_tracker.common.exceptions.NotFoundException;
import hu.projects.expense_tracker.features.reports.services.ReportService;
import hu.projects.expense_tracker.features.transactions.entities.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@WebMvcTest(value = ReportController.class)
class ReportControllerTest extends ComponentTestBase {
    private static final String BASE_URL = "/api/reports";

    @MockitoBean
    private ReportService reportService;

    @BeforeEach
    void setUp() {
        mockAuthentication();
    }

    @Test
    void getMonthlyReports_ShouldPass() throws Exception {
        // Arrange
        int year = 2026, month = 1;
        var report = ReportFactory.create(
                LocalDateTimeFactory.createMultiple(8, year, 1).stream().toList(),
                TEST_USER);

        when(reportService.getMonthlyReport(eq(TEST_USER.getUsername()), eq(year), eq(month)))
                .thenReturn(report);

        // Act & Assert
        mvc.perform(MockMvcRequestBuilders.get(BASE_URL + "?year=" + year + "&month=" + month)
                .with(authorization())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.year").value(year))
                .andExpect(MockMvcResultMatchers.jsonPath("$.transactions").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.transactions.length()").value(8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.expensesByCategory").isMap());
    }

    @Test
    void getMonthlyReports_WhenYMFilterMissing_ShouldReturnBadRequest() throws Exception {
        // Act & Assert
        mvc.perform(MockMvcRequestBuilders.get(BASE_URL)
                .with(authorization())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        verifyNoInteractions(reportService);
    }

    @Test
    void getTransactionInCategory_ShouldPass() throws Exception {
        // Arrange
        var category = "dining_and_restaurants";
        var transactions = TransactionFactory.createMultiple(5, TEST_USER)
                .stream()
                .map(Transaction::toDto);
        var pagedResult = PagedResultFactory.create(transactions, PageRequest.of(0, 10), 15);

        when(reportService.getTransactionsInCategory(eq(TEST_USER.getUsername()), eq(category), any(Pageable.class)))
                .thenReturn(pagedResult);

        // Act & Assert
        mvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/" + category)
                .with(authorization()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.items").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.items.length()").value(5))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalItems").value(15))
                .andExpect(MockMvcResultMatchers.jsonPath("$.page").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.size").value(10));

        verify(reportService).getTransactionsInCategory(eq(TEST_USER.getUsername()), eq(category), any(Pageable.class));
    }

    @Test
    void getTransactionInCategory_WhenCategoryDoesNotExist_ShouldReturnNotFound() throws Exception {
        // Arrange
        var category = "dining_and_restaurants";

        when(reportService.getTransactionsInCategory(any(String.class), any(String.class), any(Pageable.class)))
                .thenThrow(NotFoundException.class);

        // Act & Assert
        mvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/" + category)
                .with(authorization()))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}