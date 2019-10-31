package com.epam.esm.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Map;

import static com.epam.esm.constant.RequestConstant.SORT_PARAMETER;

/**
 * gift-certificates
 *
 * @author Dzmitry Platonov on 2019-10-31.
 * @version 0.0.1
 */
public class TagDetailsSortValidator implements ConstraintValidator<TagDetailsSortValid, Map<String, String>> {
    @Override
    public boolean isValid(Map<String, String> params, ConstraintValidatorContext constraintValidatorContext) {
        return !params.containsKey(SORT_PARAMETER) ||
                params.get(SORT_PARAMETER).matches("(^(-)?cost)|(^(-)?orders_num)");
    }
}
