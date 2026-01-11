package hu.projects.expense_tracker.common.validations.annotation_validators.slug;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = SlugFormatValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SlugFormat {
    String message() default "Invalid slug format. (ex. I like pizza & beer -> i_like_pizza_and_beer)";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
