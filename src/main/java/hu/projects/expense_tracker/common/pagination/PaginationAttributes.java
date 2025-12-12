package hu.projects.expense_tracker.common.pagination;

public record PaginationAttributes(
        int size,
        int page
) {
    private static final int DEFAULT_SIZE = 10;
    private static final int MAX_SIZE = 50;
    private static final int MIN_SIZE = 1;

    private static final int MIN_NUMBER = 0;

    public PaginationAttributes {
        size = getValidPageSize(size);
        page = getValidPageNumber(page);
    }

    private static int getValidPageSize(int pgSize) {
        if (pgSize == 0) return DEFAULT_SIZE;
        if (pgSize > MAX_SIZE) return MAX_SIZE;
        if (pgSize < MIN_SIZE) return MIN_SIZE;
        return pgSize;
    }

    private static int getValidPageNumber(int pgNumber) {
        return Math.max(pgNumber, MIN_NUMBER);
    }
}