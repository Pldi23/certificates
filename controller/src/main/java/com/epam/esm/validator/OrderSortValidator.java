package com.epam.esm.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Map;

import static com.epam.esm.constant.RequestConstant.SORT_PARAMETER;

/**
 * gift-certificates
 *
 * @author Dzmitry Platonov on 2019-10-15.
 * @version 0.0.1
 */
public class OrderSortValidator implements ConstraintValidator<OrderSortValid, Map<String, String>> {
    @Override
    public boolean isValid(Map<String, String> stringStringMap, ConstraintValidatorContext constraintValidatorContext) {
        return !stringStringMap.containsKey(SORT_PARAMETER) ||
                stringStringMap.get(SORT_PARAMETER).matches("(^(-)?price)|(^(-)?id)");
    }
}
