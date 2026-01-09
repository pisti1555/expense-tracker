package hu.projects.expense_tracker.features.transactions.dtos;

import hu.projects.expense_tracker.common.validations.slug.SlugFormat;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record CreateTransactionDto(
        @NotBlank(message = "You must provide the slug of category.")
        @SlugFormat(message = "Must be a valid slug format. (ex. I like pizza & beer -> i_like_pizza_and_beer)")
        String categorySlug,

        @Min(value = 0, message = "Cannot be less than 0.")
        double amount
) {}