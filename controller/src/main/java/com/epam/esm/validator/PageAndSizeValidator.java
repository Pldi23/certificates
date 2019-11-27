package com.epam.esm.validator;


import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Map;

import static com.epam.esm.constant.RequestConstant.PAGE_PARAMETER;
import static com.epam.esm.constant.RequestConstant.SIZE_PARAMETER;

public class PageAndSizeValidator implements ConstraintValidator<PageAndSizeValid, Map<String, String>> {


    @Override
    public boolean isValid(Map<String, String> stringStringMap, ConstraintValidatorContext constraintValidatorContext) {

        if (stringStringMap.containsKey(PAGE_PARAMETER) && isParsable(stringStringMap.get(PAGE_PARAMETER))) {
            return false;
        }
        if (stringStringMap.containsKey(SIZE_PARAMETER) && isParsable(stringStringMap.get(SIZE_PARAMETER))) {
            return false;
        }
        if (stringStringMap.containsKey(PAGE_PARAMETER) && !stringStringMap.get(PAGE_PARAMETER).isBlank()
                && Integer.parseInt(stringStringMap.get(PAGE_PARAMETER)) < 1) {
            return false;
        }
        if (stringStringMap.containsKey(SIZE_PARAMETER) && !stringStringMap.get(SIZE_PARAMETER).isBlank()
                && Integer.parseInt(stringStringMap.get(SIZE_PARAMETER)) < 1) {
            return false;
        }
        return !stringStringMap.containsKey(PAGE_PARAMETER) || stringStringMap.containsKey(SIZE_PARAMETER)
                || Integer.parseInt(stringStringMap.get(PAGE_PARAMETER)) <= 1;
    }

    private boolean isParsable(String input) {
        try {
            Integer.parseInt(input);
            return false;
        } catch (NumberFormatException e) {
            return true;
        }
    }
}
