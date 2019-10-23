package com.epam.esm.validator;

import com.epam.esm.constant.RequestConstant;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.Map;


public class OrderSearchCriteriaValidator implements ConstraintValidator<OrderSearchCriteriaValid, Map<String, String>> {

    @Override
    public boolean isValid(Map<String, String> params, ConstraintValidatorContext constraintValidatorContext) {
        if (!validateUserCriteria(params)) return false;
        if (!validateCertificateCriteria(params)) return false;
        if (params.containsKey(RequestConstant.EMAIL) && params.get(RequestConstant.EMAIL).isBlank()) return false;
        if (params.containsKey(RequestConstant.USER_ID) && (params.get(RequestConstant.USER_ID).isBlank()
                || !params.get(RequestConstant.USER_ID).matches("\\d+")
                || !isParsable(params.get(RequestConstant.USER_ID)))) return false;
        if (params.containsKey(RequestConstant.CERTIFICATE_NAME) && params.get(RequestConstant.CERTIFICATE_NAME).isBlank()) return false;
        return !params.containsKey(RequestConstant.CERTIFICATE_ID) || (!params.get(RequestConstant.CERTIFICATE_ID).isBlank()
                && Arrays.stream(params.get(RequestConstant.CERTIFICATE_ID).split(",")).anyMatch(s -> s.matches("\\d+"))
                && isParsable(params.get(RequestConstant.CERTIFICATE_ID)));
    }

    private boolean validateUserCriteria(Map<String, String> params) {
        return !params.containsKey(RequestConstant.EMAIL) || !params.containsKey(RequestConstant.USER_ID);
    }

    private boolean validateCertificateCriteria(Map<String, String> params) {
        return !params.containsKey(RequestConstant.CERTIFICATE_NAME) || !params.containsKey(RequestConstant.CERTIFICATE_ID);
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
