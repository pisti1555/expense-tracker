package hu.projects.expense_tracker.features.transactions.services;

import hu.projects.expense_tracker.common.exceptions.NotFoundException;
import hu.projects.expense_tracker.factories.TransactionFactory;
import hu.projects.expense_tracker.factories.UserFactory;
import hu.projects.expense_tracker.features.transactions.dtos.CreateTransactionDto;
import hu.projects.expense_tracker.features.transactions.entities.Transaction;
import hu.projects.expense_tracker.features.transactions.repositories.TransactionRepository;
import hu.projects.expense_tracker.features.users.entities.User;
import hu.projects.expense_tracker.features.users.repositories.UserRepository;
import org.junit.jupiter.api.BeforeAll;
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

    private static User user;
    private static Transaction transaction;

    @BeforeAll
    static void beforeAll() {
        user = UserFactory.create();
        transaction = TransactionFactory.create(user);
    }

    @Test
    void createTransaction_ShouldPass() {
        // Arrange
        var createTransactionDto = TransactionFactory.newCreateTransactionDto();
        var transaction = TransactionFactory.fromDto(createTransactionDto, user);

        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        // Act
        var savedTransaction = transactionService.createTransaction(createTransactionDto, user.getUsername());

        // Assert
        assertEquals(createTransactionDto.categorySlug(), savedTransaction.categorySlug());
        assertEquals(createTransactionDto.amount(), savedTransaction.amount());

        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void createTransaction_WhenCategoryDoesNotExist_ShouldThrowNotFoundException() {
        // Arrange
        var createTransactionDto = new CreateTransactionDto("not_existing_category", 10000.0);

        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        // Act & Assert
        assertThrows(NotFoundException.class, () -> transactionService.createTransaction(createTransactionDto, user.getUsername()));
        verify(transactionRepository, times(0)).save(any(Transaction.class));
    }

    @Test
    void deleteTransactionById_ShouldPass() {
        // Arrange
        when(transactionRepository.findById(transaction.getId())).thenReturn(Optional.of(transaction));

        // Act
        transactionService.deleteTransactionById(transaction.getId(), user.getUsername());

        // Assert
        verify(transactionRepository, times(1)).delete(any(Transaction.class));
    }

    @Test
    void deleteTransactionById_WhenTransactionDoesNotExist_ShouldThrowNotFoundException() {
        // Arrange
        when(transactionRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> transactionService.deleteTransactionById(1L, user.getUsername()));
        verify(transactionRepository, times(0)).delete(any(Transaction.class));
    }

    @Test
    void deleteTransactionById_WhenUserDoesNotOwnTransaction_ShouldThrowNotFoundException() {
        // Arrange
        var otherUser = UserFactory.create();

        when(transactionRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> transactionService.deleteTransactionById(transaction.getId(), otherUser.getUsername()));
        verify(transactionRepository, times(0)).delete(any(Transaction.class));
    }

    @Test
    void getTransactionById_ShouldPass() {
        // Arrange
        when(transactionRepository.findById(transaction.getId())).thenReturn(Optional.of(transaction));

        // Act
        var foundTransaction = transactionService.getTransactionById(transaction.getId(), user.getUsername());

        // Assert
        assertEquals(transaction.getId(), foundTransaction.id());
        assertEquals(transaction.getCategory().getName(), foundTransaction.categorySlug());
        assertEquals(transaction.getCategory().getDisplayName(), foundTransaction.categoryName());
        assertEquals(transaction.getAmount(), foundTransaction.amount());

        verify(transactionRepository, times(1)).findById(transaction.getId());
    }

    @Test
    void getTransactionById__WhenTransactionDoesNotExist_ShouldThrowNotFoundException() {
        // Arrange
        when(transactionRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> transactionService.getTransactionById(1L, user.getUsername()));
    }

    @Test
    void getTransactionById_WhenUserDoesNotOwnTransaction_ShouldThrowNotFoundException() {
        // Arrange
        var otherUser = UserFactory.create();

        when(transactionRepository.findById(any(Long.class))).thenReturn(Optional.of(transaction));

        // Act & Assert
        assertThrows(NotFoundException.class, () -> transactionService.deleteTransactionById(transaction.getId(), otherUser.getUsername()));
    }
}