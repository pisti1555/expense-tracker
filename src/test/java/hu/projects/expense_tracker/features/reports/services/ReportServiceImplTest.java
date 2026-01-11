package hu.projects.expense_tracker.features.reports.services;

import factories.LocalDateTimeFactory;
import factories.TransactionFactory;
import factories.UserFactory;
import hu.projects.expense_tracker.common.exceptions.BadRequestException;
import hu.projects.expense_tracker.common.exceptions.NotFoundException;
import hu.projects.expense_tracker.features.transactions.entities.Transaction;
import hu.projects.expense_tracker.features.transactions.enums.TransactionCategory;
import hu.projects.expense_tracker.features.transactions.repositories.TransactionRepository;
import hu.projects.expense_tracker.features.users.entities.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReportServiceImplTest {
    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private ReportServiceImpl reportService;

    private static final User USER = UserFactory.create();
    private static final List<Transaction> TRANSACTIONS = new ArrayList<>();

    @BeforeAll
    static void beforeAll() {
        var january = LocalDateTimeFactory.createMultiple(12, 2025, 1);
        var february = LocalDateTimeFactory.createMultiple(9, 2025, 2);

        TRANSACTIONS.addAll(TransactionFactory.createMultiple(january, USER));
        TRANSACTIONS.addAll(TransactionFactory.createMultiple(february, USER));
    }

    @Test
    void getMonthlyReport_ShouldPass() {
        // Arrange
        int year = 2025, month = 1;
        when(transactionRepository.findInTimeRangeByUsername(any(), any(), any())).thenReturn(TRANSACTIONS);

        // Act
        var result = reportService.getMonthlyReport(USER.getUsername(), year, month);

        // Assert
        assertEquals(year, result.year());
        assertEquals(month, result.month());
    }

    @Test
    void getMonthlyReport_ShouldCalculateExpensesCorrectly() {
        // Arrange
        int year = 2025, month = 1;
        var customTransactions = List.of(
                TransactionFactory.create(USER, TransactionCategory.TRANSPORTATION, 10000, LocalDateTimeFactory.create(year, month)),
                TransactionFactory.create(USER, TransactionCategory.TRANSPORTATION, 5000, LocalDateTimeFactory.create(year, month)),
                TransactionFactory.create(USER, TransactionCategory.BANK_TRANSFER_INCOMING, 90000, LocalDateTimeFactory.create(year, month)),
                TransactionFactory.create(USER, TransactionCategory.CLOTHING, 8000, LocalDateTimeFactory.create(year, month))
        );
        when(transactionRepository.findInTimeRangeByUsername(any(), any(), any())).thenReturn(customTransactions);

        // Act
        var result = reportService.getMonthlyReport(USER.getUsername(), year, month);

        // Assert
        assertEquals(23000, result.totalExpenseAmount());
        assertEquals(90000, result.totalIncomeAmount());
        assertEquals(15000, result.expensesByCategory().get(TransactionCategory.TRANSPORTATION));
        assertEquals(8000, result.expensesByCategory().get(TransactionCategory.CLOTHING));
        assertFalse(result.expensesByCategory().containsKey(TransactionCategory.BANK_TRANSFER_INCOMING));
    }

    @Test
    void getMonthlyReport_WhenNoTransactionsExistInYM_ShouldReturnEmptyMonthlyReportDtoWith0Values() {
        // Arrange
        int year = 2025, month = 1;
        when(transactionRepository.findInTimeRangeByUsername(any(), any(), any())).thenReturn(List.of());

        // Act
        var result = reportService.getMonthlyReport(USER.getUsername(), year, month);

        // Assert
        assertEquals(year, result.year());
        assertEquals(month, result.month());
        assertEquals(0, result.totalExpenseAmount());
        assertEquals(0, result.totalIncomeAmount());
        assertTrue(result.expensesByCategory().isEmpty());
        assertTrue(result.transactions().isEmpty());
    }

    @Test
    void getTransactionsInCategory_ShouldPass() {
        // Arrange
        var category = "transportation";
        var pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());
        var page = new PageImpl<>(TRANSACTIONS, pageable, TRANSACTIONS.size());

        when(transactionRepository.findInCategoryByUsername(any(), any(), any()))
                .thenReturn(page);

        // Act
        var result = reportService.getTransactionsInCategory(USER.getUsername(), category, pageable);

        // Assert
        assertEquals(page.stream().map(Transaction::toDto).toList(), result.getItems().stream().toList());
        assertEquals(page.getTotalPages(), result.getTotalPages());
        assertEquals(page.getTotalElements(), result.getTotalItems());
        assertTrue(page.getSort().isSorted());

        verify(transactionRepository, times(1))
                .findInCategoryByUsername(USER.getUsername(), TransactionCategory.TRANSPORTATION, pageable);
    }

    @Test
    void getTransactionsInCategory_WhenCategoryDoesNotExists_ShouldThrowNotFoundException() {
        // Arrange
        var category = "not_existing_category";
        var pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> reportService.getTransactionsInCategory(USER.getUsername(), category, pageable));
        verifyNoInteractions(transactionRepository);
    }

    @Test
    void getTransactionsInCategory_WhenSlugInvalid_ShouldThrowBadRequestException() {
        // Arrange
        var category = "transportation";
        var pageable = PageRequest.of(0, 10, Sort.by("invalid").descending());

        // Act & Assert
        assertThrows(BadRequestException.class, () -> reportService.getTransactionsInCategory(USER.getUsername(), category, pageable));
        verifyNoInteractions(transactionRepository);
    }
}