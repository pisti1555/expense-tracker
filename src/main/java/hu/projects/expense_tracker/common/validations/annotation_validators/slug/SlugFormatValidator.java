package hu.projects.expense_tracker.common.validations.annotation_validators.slug;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class SlugFormatValidator implements ConstraintValidator<SlugFormat, String> {
    @Override
    public boolean isValid(String str, ConstraintValidatorContext constraintValidatorContext) {
        if (str == null || str.isBlank()) return false;
        return str.matches("[a-z_]+");
    }
}
