package hu.projects.expense_tracker.common.validations.year_past_or_present;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = YearPastOrPresentValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface YearPastOrPresent {
    String message() default "Invalid year.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
