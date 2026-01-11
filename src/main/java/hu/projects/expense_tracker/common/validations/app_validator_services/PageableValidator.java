package hu.projects.expense_tracker.common.validations.app_validator_services;

import hu.projects.expense_tracker.common.exceptions.BadRequestException;
import org.springframework.data.domain.Pageable;

import java.util.Collection;

public class PageableValidator {
    public static void throwIfSortInvalid(Pageable pageable, Collection<String> allowedProperties) {
        if (pageable.getSort().isSorted()) {
            pageable.getSort().stream()
                    .forEach(o -> {
                        var prop = o.getProperty();
                        var direction = o.getDirection().name();

                        if (!allowedProperties.contains(prop)) {
                            throw new BadRequestException("Sorting property " + prop + " is invalid. Allowed properties are: " + allowedProperties);
                        }

                        if (!direction.equalsIgnoreCase("asc") && !direction.equalsIgnoreCase("desc")) {
                            throw new BadRequestException("Sorting direction " + direction + " is invalid. Valid directions are 'asc' and 'desc'");
                        }
                    });
        }
    }
}
