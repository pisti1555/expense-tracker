package hu.projects.expense_tracker.features.transactions.enums;

import lombok.Getter;

@Getter
public enum TransactionCategory {
    BANK_TRANSFER_INCOMING("Bank Transfer (income)", false),
    BANK_TRANSFER_OUTGOING("Bank Transfer (outgoing)", true),
    TRANSPORTATION("Transportation", true),
    GROCERIES("Groceries", true),
    TRAVEL("Travel", true),
    BILLS("Bills", true),
    ENTERTAINMENT("Entertainment", true),
    DINING_AND_RESTAURANTS("Dining & Restaurants", true),
    CLOTHING("Clothing", true),
    OTHER("Other", true);

    TransactionCategory(String displayName, boolean isExpense) {
        this.displayName = displayName;
        this.isExpense = isExpense;
    }

    private final String displayName;
    private final boolean isExpense;
}
