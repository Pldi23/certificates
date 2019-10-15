package com.epam.esm.validator;


import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Map;
import static com.epam.esm.constant.RequestConstant.*;

/**
 * gift-certificates
 *
 * @author Dzmitry Platonov on 2019-10-14.
 * @version 0.0.1
 */
public class PageAndSizeValidator implements ConstraintValidator<PageAndSizeValid, Map<String, String>> {


    @Override
    public boolean isValid(Map<String, String> stringStringMap, ConstraintValidatorContext constraintValidatorContext) {

        if (stringStringMap.containsKey(PAGE_PARAMETER) && !stringStringMap.get(PAGE_PARAMETER).isBlank()
                && Integer.parseInt(stringStringMap.get(PAGE_PARAMETER)) < 1) {
            return false;
        }
        if (stringStringMap.containsKey(SIZE_PARAMETER) && !stringStringMap.get(SIZE_PARAMETER).isBlank()
                && Integer.parseInt(stringStringMap.get(SIZE_PARAMETER)) < 1) {
            return false;
        }
        return true;
    }
}
