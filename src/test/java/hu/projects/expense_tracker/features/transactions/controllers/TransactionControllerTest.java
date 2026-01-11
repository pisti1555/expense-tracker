package hu.projects.expense_tracker.features.transactions.controllers;

import bases.ComponentTestBase;
import factories.PagedResultFactory;
import factories.TransactionFactory;
import hu.projects.expense_tracker.common.exceptions.NotFoundException;
import hu.projects.expense_tracker.features.transactions.dtos.CreateTransactionDto;
import hu.projects.expense_tracker.features.transactions.entities.Transaction;
import hu.projects.expense_tracker.features.transactions.services.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@WebMvcTest(value = TransactionController.class)
class TransactionControllerTest extends ComponentTestBase {
    private static final String BASE_URL = "/api/transactions";

    @MockitoBean
    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        mockAuthentication();
    }

    @Test
    void create_ShouldReturnTransactionDto_WithCreated() throws Exception {
        // Arrange
        var createTransactionDto = new CreateTransactionDto("dining_and_restaurants", 6000);
        var createdTransaction = TransactionFactory.fromDto(createTransactionDto, TEST_USER);

        when(transactionService.createTransaction(eq(createTransactionDto), eq(TEST_USER.getUsername())))
                .thenReturn(Transaction.toDto(createdTransaction));

        // Act & Assert
        mvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                .with(authorization())
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(createTransactionDto)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().string("Location", BASE_URL + "/" + createdTransaction.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(createdTransaction.getId()));
    }

    @ParameterizedTest
    @ValueSource(strings = {"dining and_restaurants", " dining_and_restaurants", "Dining_and_restaurants", "dining_and_restaurants0"})
    void create_WhenInvalidCategorySlug_ShouldReturnValidationErrorResponse_WithBadRequest(String slug) throws Exception {
        // Arrange
        var createTransactionDto = new CreateTransactionDto(slug, 6000);

        // Act & Assert
        mvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                .with(authorization())
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(createTransactionDto)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.header().doesNotExist("Location"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors.categorySlug").exists());

        verifyNoInteractions(transactionService);
    }

    @Test
    void delete_ShouldReturnNoContent() throws Exception {
        // Arrange
        var transactionId = 1L;

        // Act & Assert
        mvc.perform(MockMvcRequestBuilders.delete(BASE_URL + "/" + transactionId)
                .with(authorization()))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    void delete_WhenTransactionDoesNotExist_ShouldReturnNotFound() throws Exception {
        // Arrange
        Long transactionId = 1L;

        doThrow(NotFoundException.class).when(transactionService).deleteTransactionById(eq(transactionId), eq(TEST_USER.getUsername()));

        // Act & Assert
        mvc.perform(MockMvcRequestBuilders.delete(BASE_URL + "/" + transactionId)
                .with(authorization()))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void getTransactionById_ShouldReturnTransactionDto() throws Exception {
        // Arrange
        var transactionDto = Transaction.toDto(TransactionFactory.create(TEST_USER));

        when(transactionService.getTransactionById(eq(transactionDto.id()), eq(TEST_USER.getUsername())))
                .thenReturn(transactionDto);

        // Act & Assert
        mvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/" + transactionDto.id())
                .with(authorization()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(transactionDto.id()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.categorySlug").value(transactionDto.categorySlug()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.amount").value(transactionDto.amount()));
    }

    @Test
    void getTransactionById_WhenTransactionDoesNotExist_ShouldReturnNotFound() throws Exception {
        // Arrange
        var transactionDto = Transaction.toDto(TransactionFactory.create(TEST_USER));

        when(transactionService.getTransactionById(eq(transactionDto.id()), any(String.class)))
                .thenThrow(NotFoundException.class);

        // Act & Assert
        mvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/" + transactionDto.id())
                .with(authorization()))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void getTransactions_ShouldReturnPagedResult() throws Exception {
        // Arrange
        var pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());
        var transactionsStream = TransactionFactory.createMultiple(10, TEST_USER)
                .stream()
                .map(Transaction::toDto);
        var pagedResult = PagedResultFactory.create(transactionsStream, pageable, 16);

        when(transactionService.getTransactionsPaged(eq(TEST_USER.getUsername()), any()))
                .thenReturn(pagedResult);

        // Act & Assert
        mvc.perform(MockMvcRequestBuilders.get(BASE_URL)
                .with(authorization()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.items").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.items.length()").value(10))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalItems").value(16))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalPages").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.page").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.size").value(10));

        verify(transactionService).getTransactionsPaged(eq(TEST_USER.getUsername()), eq(pageable));
    }
}