package hu.projects.expense_tracker.services.http;

import hu.projects.expense_tracker.common.pagination.PagedResult;
import org.springframework.http.HttpHeaders;

public class HttpService {
    public static HttpHeaders GeneratePaginationHeaders(PagedResult<?> page) {
        var headers = new HttpHeaders();

        var xCurrentPage = String.valueOf(page.getPage());
        var xPageSize = String.valueOf(page.getSize());
        var xTotalPages = String.valueOf(page.getTotalPages());
        var xTotalElements= String.valueOf(page.getTotalItems());

        headers.add("X-Current-Page", xCurrentPage);
        headers.add("X-Page-Size", xPageSize);
        headers.add("X-Total-Pages", xTotalPages);
        headers.add("X-Total-Elements", xTotalElements);

        headers.add("Access-Control-Expose-Headers", "X-Current-Page, X-Page-Size, X-Total-Pages, X-Total-Elements");

        return headers;
    }
}
