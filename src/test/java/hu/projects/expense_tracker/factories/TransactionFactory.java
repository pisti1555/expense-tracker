package hu.projects.expense_tracker.factories;

import hu.projects.expense_tracker.features.transactions.dtos.CreateTransactionDto;
import hu.projects.expense_tracker.features.transactions.dtos.TransactionDto;
import hu.projects.expense_tracker.features.transactions.entities.Transaction;
import hu.projects.expense_tracker.features.transactions.enums.TransactionCategory;
import hu.projects.expense_tracker.features.users.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.*;

public class TransactionFactory extends TestDataFactory {
    private static final Random random = new Random();

    public static Collection<Transaction> multiple(int quantity, User u) {
        var transactions = new ArrayList<Transaction>();
        for (int i = 0; i < quantity; i++) {
            transactions.add(fromDto(newCreateTransactionDto(), u));
        }
        return transactions;
    }

    public static Transaction fromDto(CreateTransactionDto dto, User u) {
        var user = u == null ? UserFactory.create() : u;

        var category = Arrays.stream(TransactionCategory.values())
                .filter(c -> c.getName().equals(dto.categorySlug()))
                .findFirst()
                .orElseThrow();

        var transaction = new Transaction(user, category, dto.amount());

        transaction.setId( random.nextLong(99999) + 1L);
        transaction.setCreatedAt(LocalDateTime.now());

        return transaction;
    }

    public static CreateTransactionDto newCreateTransactionDto() {
        var randomCategoryIndex = random.nextInt(TransactionCategory.values().length);
        var randomCategory = TransactionCategory.values()[randomCategoryIndex];

        var randomAmount = random.nextDouble(40000.0) + 2000.0;

        return new CreateTransactionDto(randomCategory.getName(), randomAmount);
    }
}
