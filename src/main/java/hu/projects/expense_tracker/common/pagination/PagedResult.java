package hu.projects.expense_tracker.common.pagination;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.Collection;

@Getter
public class PagedResult<T> {
    private final Collection<T> items;
    private final int page;
    private final int size;

    private final boolean hasPrevious;
    private final boolean hasNext;

    private final long totalItems;
    private final int totalPages;

    private final boolean sorted;
    private final Collection<Sorting> sortedBy;

    private PagedResult(
            Collection<T> items, int page, int size, boolean hasPrevious, boolean hasNext,
            long totalItems, int totalPages,
            boolean sorted, Collection<Sorting> sortedBy
    ) {
        this.items = items;
        this.page = page;
        this.size = size;
        this.hasPrevious = hasPrevious;
        this.hasNext = hasNext;
        this.totalItems = totalItems;
        this.totalPages = totalPages;
        this.sorted = sorted;
        this.sortedBy = sortedBy;
    }

    public static <T> PagedResult<T> create(Page<T> page) {
        var sorts = page.getSort().stream()
                .map(s -> new Sorting(s.getProperty(), s.getDirection().name()))
                .toList();

        return new PagedResult<>(
                page.getContent(),
                page.getNumber() + 1,
                page.getSize(),
                page.hasPrevious(),
                page.hasNext(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.getSort().isSorted(),
                sorts
        );
    }

    private record Sorting (
            String property,
            String direction
    ) {};
}
