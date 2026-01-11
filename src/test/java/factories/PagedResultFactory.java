package factories;

import hu.projects.expense_tracker.common.pagination.PagedResult;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

public class PagedResultFactory {
    public static <T> PagedResult<T> create(List<T> items, Pageable pageable, int totalItems) {
        var page = new PageImpl<>(items, pageable, totalItems);
        return PagedResult.create(page);
    }

    public static <T> PagedResult<T> create(Stream<T> itemsStream, Pageable pageable, int totalItems) {
        var page = new PageImpl<>(itemsStream.toList(), pageable, totalItems);
        return PagedResult.create(page);
    }
}
