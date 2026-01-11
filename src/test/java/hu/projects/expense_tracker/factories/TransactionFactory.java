package hu.projects.expense_tracker.factories;

import hu.projects.expense_tracker.features.transactions.dtos.CreateTransactionDto;
import hu.projects.expense_tracker.features.transactions.entities.Transaction;
import hu.projects.expense_tracker.features.transactions.enums.TransactionCategory;
import hu.projects.expense_tracker.features.users.entities.User;

import java.time.LocalDateTime;
import java.util.*;

public class TransactionFactory extends TestDataFactory {
    private static final Random random = new Random();

    public static Collection<Transaction> createMultiple(Collection<LocalDateTime> dates, User u) {
        var transactions = new ArrayList<Transaction>();
        for (var date : dates) {
            transactions.add(create(u, date));
        }
        return transactions;
    }

    public static Collection<Transaction> createMultiple(int quantity, User u) {
        var transactions = new ArrayList<Transaction>();
        for (int i = 0; i < quantity; i++) {
            transactions.add(create(u));
        }
        return transactions;
    }

    public static Transaction create(User u) {
        var user = u == null ? UserFactory.create() : u;
        var category = getRandomTransactionCategory();
        var amount = getRandomAmount();
        var transaction = new Transaction(user, category, amount);

        transaction.setId( random.nextLong(99999) + 1L);
        transaction.setCreatedAt(LocalDateTime.now());

        return transaction;
    }

    public static Transaction create(User u, LocalDateTime createdAt) {
        var user = u == null ? UserFactory.create() : u;
        var category = getRandomTransactionCategory();
        var amount = getRandomAmount();
        var transaction = new Transaction(user, category, amount);

        transaction.setId( random.nextLong(99999) + 1L);
        transaction.setCreatedAt(createdAt);

        return transaction;
    }

    public static Transaction create(User u, double amount, LocalDateTime createdAt) {
        var user = u == null ? UserFactory.create() : u;
        var category = getRandomTransactionCategory();
        var transaction = new Transaction(user, category, amount);

        transaction.setId( random.nextLong(99999) + 1L);
        transaction.setCreatedAt(createdAt);

        return transaction;
    }

    public static Transaction create(User u, TransactionCategory category, double amount, LocalDateTime createdAt) {
        var user = u == null ? UserFactory.create() : u;
        var transaction = new Transaction(user, category, amount);

        transaction.setId( random.nextLong(99999) + 1L);
        transaction.setCreatedAt(createdAt);

        return transaction;
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
        var randomCategory = getRandomTransactionCategory();
        var randomAmount = getRandomAmount();
        return new CreateTransactionDto(randomCategory.getName(), randomAmount);
    }

    private static TransactionCategory getRandomTransactionCategory() {
        var randomCategoryIndex = random.nextInt(TransactionCategory.values().length);
        return TransactionCategory.values()[randomCategoryIndex];
    }

    private static double getRandomAmount() {
        return random.nextDouble(40000.0) + 2000.0;
    }
}
