package com.epam.esm.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Map;

import static com.epam.esm.constant.RequestConstant.SORT_PARAMETER;


public class TagSortValidator implements ConstraintValidator<TagSortValid, Map<String, String>> {
    @Override
    public boolean isValid(Map<String, String> stringStringMap, ConstraintValidatorContext constraintValidatorContext) {
        return !stringStringMap.containsKey(SORT_PARAMETER) ||
                stringStringMap.get(SORT_PARAMETER).matches("(^(-)?title)|(^(-)?id)");
    }
}
