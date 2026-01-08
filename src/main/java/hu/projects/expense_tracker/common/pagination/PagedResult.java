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

    private PagedResult(Collection<T> items, int page, int size, boolean hasPrevious, boolean hasNext, long totalItems, int totalPages) {
        this.items = items;
        this.page = page;
        this.size = size;
        this.hasPrevious = hasPrevious;
        this.hasNext = hasNext;
        this.totalItems = totalItems;
        this.totalPages = totalPages;
    }

    public static <T> PagedResult<T> create(Page<T> page) {
        return new PagedResult<T>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.hasPrevious(),
                page.hasNext(),
                page.getTotalElements(),
                page.getTotalPages());
    }
}
