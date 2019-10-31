package com.epam.esm.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Map;

/**
 * to be implemented later
 *
 * @author Dzmitry Platonov on 2019-10-31.
 * @version 0.0.1
 */
public class TagDetailsSearchValidator implements ConstraintValidator<TagDetailsSearchValid, Map<String, String>> {

    @Override
    public boolean isValid(Map<String, String> params, ConstraintValidatorContext constraintValidatorContext) {

        return false;
    }
}
