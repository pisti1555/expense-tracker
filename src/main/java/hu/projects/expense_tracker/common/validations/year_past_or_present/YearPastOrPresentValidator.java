package hu.projects.expense_tracker.common.validations.year_past_or_present;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class YearPastOrPresentValidator implements ConstraintValidator<YearPastOrPresent, Integer> {
    @Override
    public boolean isValid(Integer year, ConstraintValidatorContext constraintValidatorContext) {
        var nowYear = LocalDate.now().getYear();
        return year != null && year <= nowYear;
    }
}
