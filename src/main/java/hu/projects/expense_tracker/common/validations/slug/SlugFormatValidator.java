package hu.projects.expense_tracker.common.validations.slug;

import hu.projects.expense_tracker.services.app_services.Slug;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class SlugFormatValidator implements ConstraintValidator<SlugFormat, String> {
    @Override
    public boolean isValid(String str, ConstraintValidatorContext constraintValidatorContext) {
        return Slug.isSlugFormat(str);
    }
}
