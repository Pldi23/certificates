package com.epam.esm.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Set;

/**
 * gift-certificates
 *
 * @author Dzmitry Platonov on 2019-10-13.
 * @version 0.0.1
 */
public class NotEmptySetValidator implements ConstraintValidator<ValidCertificatesSet, Set<String>> {
    @Override
    public void initialize(ValidCertificatesSet constraintAnnotation) {

    }

    @Override
    public boolean isValid(Set<String> strings, ConstraintValidatorContext constraintValidatorContext) {
//        return strings.stream().allMatch(nef -> nef != null && !nef.trim().isEmpty());
        return strings != null && !strings.isEmpty() && strings.stream()
                .allMatch(this::isValidName);

    }

    private boolean isValidName(String name) {
        return !name.isBlank() && name.matches("([\\w-]+(?: [\\w-]+)+)|([\\w-]+)") && name.length() >= 1 && name.length() <= 30;
    }

//    @Override
//    public void initialize(NotEmptyFields notEmptyFields) {
//    }
//
//    @Override
//    public boolean isValid(List<String> objects, ConstraintValidatorContext context) {
//        return objects.stream().allMatch(nef -> nef != null && !nef.trim().isEmpty());
//    }

}
