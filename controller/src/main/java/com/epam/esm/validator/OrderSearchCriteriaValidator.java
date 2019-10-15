package com.epam.esm.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.Map;

/**
 * gift-certificates
 *
 * @author Dzmitry Platonov on 2019-10-14.
 * @version 0.0.1
 */
public class OrderSearchCriteriaValidator implements ConstraintValidator<OrderSearchCriteriaValid, Map<String, String>> {

    @Override
    public boolean isValid(Map<String, String> params, ConstraintValidatorContext constraintValidatorContext) {
        if (!validateUserCriteria(params)) return false;
        if (!validateCertificateCriteria(params)) return false;
        if (params.containsKey("email") && params.get("email").isBlank()) return false;
        if (params.containsKey("user_id") && (params.get("user_id").isBlank()
                || !params.get("user_id").matches("\\d+")
                || !isParsable(params.get("user_id")))) return false;
        if (params.containsKey("c_name") && params.get("c_name").isBlank()) return false;
        if (params.containsKey("c_id") && (params.get("c_id").isBlank()
                || Arrays.stream(params.get("c_id").split(",")).noneMatch(s -> s.matches("\\d+"))
                || !isParsable(params.get("c_id")))) return false;
        return true;
    }

    private boolean validateUserCriteria(Map<String, String> params) {
        return !params.containsKey("email") || !params.containsKey("user_id");
    }

    private boolean validateCertificateCriteria(Map<String, String> params) {
        return !params.containsKey("c_name") || !params.containsKey("c_id");
    }

    private boolean isParsable(String input) {
        try {
            Long.parseLong(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
