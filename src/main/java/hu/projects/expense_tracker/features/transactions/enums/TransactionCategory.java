package hu.projects.expense_tracker.features.transactions.enums;

import hu.projects.expense_tracker.common.exceptions.NotFoundException;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum TransactionCategory {
    BANK_TRANSFER_INCOMING("bank_transfer_income", "Bank Transfer (income)", false),
    BANK_TRANSFER_OUTGOING("bank_transfer_outgoing", "Bank Transfer (outgoing)", true),
    TRANSPORTATION("transportation", "Transportation", true),
    GROCERIES("groceries", "Groceries", true),
    TRAVEL("travel", "Travel", true),
    BILLS("bills", "Bills", true),
    ENTERTAINMENT("entertainment", "Entertainment", true),
    DINING_AND_RESTAURANTS("dining_and_restaurants", "Dining & Restaurants", true),
    CLOTHING("clothing", "Clothing", true),
    OTHER("other", "Other", true);

    private final String name;
    private final String displayName;
    private final boolean isExpense;

    TransactionCategory(String name, String displayName, boolean isExpense) {
        this.name = name;
        this.displayName = displayName;
        this.isExpense = isExpense;
    }

    public static TransactionCategory getCategoryBySlugOrThrow(String categorySlug) {
        return Arrays.stream(values())
                .filter(c -> c.getName().equals(categorySlug))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Category not found. Options are: " + Arrays.toString(Arrays.stream(values()).map(c -> c.name).toArray())));
    }
}
