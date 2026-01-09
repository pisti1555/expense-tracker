package hu.projects.expense_tracker.features.transactions.services;

import hu.projects.expense_tracker.common.exceptions.NotFoundException;
import hu.projects.expense_tracker.factories.TransactionFactory;
import hu.projects.expense_tracker.factories.UserFactory;
import hu.projects.expense_tracker.features.transactions.dtos.CreateTransactionDto;
import hu.projects.expense_tracker.features.transactions.entities.Transaction;
import hu.projects.expense_tracker.features.transactions.repositories.TransactionRepository;
import hu.projects.expense_tracker.features.users.entities.User;
import hu.projects.expense_tracker.features.users.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    private static final User USER = UserFactory.create();
    private static final Transaction TRANSACTION = TransactionFactory.create(USER);

    @Test
    void createTransaction_ShouldPass() {
        // Arrange
        var createTransactionDto = TransactionFactory.newCreateTransactionDto();
        var transaction = TransactionFactory.fromDto(createTransactionDto, USER);

        when(userRepository.findByUsername(USER.getUsername())).thenReturn(Optional.of(USER));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        // Act
        var savedTransaction = transactionService.createTransaction(createTransactionDto, USER.getUsername());

        // Assert
        assertEquals(createTransactionDto.categorySlug(), savedTransaction.categorySlug());
        assertEquals(createTransactionDto.amount(), savedTransaction.amount());

        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void createTransaction_WhenCategoryDoesNotExist_ShouldThrowNotFoundException() {
        // Arrange
        var createTransactionDto = new CreateTransactionDto("not_existing_category", 10000.0);

        when(userRepository.findByUsername(USER.getUsername())).thenReturn(Optional.of(USER));

        // Act & Assert
        assertThrows(NotFoundException.class, () -> transactionService.createTransaction(createTransactionDto, USER.getUsername()));
        verify(transactionRepository, never()).save(any(Transaction.class));
    }

    @Test
    void deleteTransactionById_ShouldPass() {
        // Arrange
        when(transactionRepository.findById(TRANSACTION.getId())).thenReturn(Optional.of(TRANSACTION));

        // Act
        transactionService.deleteTransactionById(TRANSACTION.getId(), USER.getUsername());

        // Assert
        verify(transactionRepository, times(1)).delete(any(Transaction.class));
    }

    @Test
    void deleteTransactionById_WhenTransactionDoesNotExist_ShouldThrowNotFoundException() {
        // Arrange
        when(transactionRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> transactionService.deleteTransactionById(1L, USER.getUsername()));
        verify(transactionRepository, never()).delete(any(Transaction.class));
    }

    @Test
    void deleteTransactionById_WhenUserDoesNotOwnTransaction_ShouldThrowNotFoundException() {
        // Arrange
        var otherUser = UserFactory.create();

        when(transactionRepository.findById(any(Long.class))).thenReturn(Optional.of(TRANSACTION));

        // Act & Assert
        assertThrows(NotFoundException.class, () -> transactionService.deleteTransactionById(TRANSACTION.getId(), otherUser.getUsername()));
        verify(transactionRepository, never()).delete(any(Transaction.class));
    }

    @Test
    void getTransactionById_ShouldPass() {
        // Arrange
        when(transactionRepository.findById(TRANSACTION.getId())).thenReturn(Optional.of(TRANSACTION));

        // Act
        var foundTransaction = transactionService.getTransactionById(TRANSACTION.getId(), USER.getUsername());

        // Assert
        assertEquals(TRANSACTION.getId(), foundTransaction.id());
        assertEquals(TRANSACTION.getCategory().getName(), foundTransaction.categorySlug());
        assertEquals(TRANSACTION.getCategory().getDisplayName(), foundTransaction.categoryName());
        assertEquals(TRANSACTION.getAmount(), foundTransaction.amount());

        verify(transactionRepository, times(1)).findById(TRANSACTION.getId());
    }

    @Test
    void getTransactionById__WhenTransactionDoesNotExist_ShouldThrowNotFoundException() {
        // Arrange
        when(transactionRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> transactionService.getTransactionById(1L, USER.getUsername()));
    }

    @Test
    void getTransactionById_WhenUserDoesNotOwnTransaction_ShouldThrowNotFoundException() {
        // Arrange
        var otherUser = UserFactory.create();

        when(transactionRepository.findById(any(Long.class))).thenReturn(Optional.of(TRANSACTION));

        // Act & Assert
        assertThrows(NotFoundException.class, () -> transactionService.deleteTransactionById(TRANSACTION.getId(), otherUser.getUsername()));
    }
}