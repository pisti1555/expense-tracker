package hu.projects.expense_tracker.common.filters;

import hu.projects.expense_tracker.common.validations.year_past_or_present.YearPastOrPresent;
import jakarta.validation.constraints.*;

public record YMFilter(
        @NotNull(message = "Year is required")
        @Min(value = 1900, message = "Year cannot be less than 1900")
        @Max(value = 2100, message = "Year cannot be greater than 2100")
        @YearPastOrPresent
        Integer year,

        @NotNull(message = "Month is required")
        @Min(value = 1, message = "Month cannot be less than 1")
        @Max(value = 12, message = "Month cannot be greater than 12")
        Integer month
) {}