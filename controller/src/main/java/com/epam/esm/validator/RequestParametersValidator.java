package com.epam.esm.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.epam.esm.constant.RequestConstant.*;
import static com.epam.esm.validator.RegexPatternConstant.*;

/**
 * gift certificates
 * class to validate input parameters from request
 *
 * @author Dzmitry Platonov on 2019-09-28.
 * @version 0.0.1
 */
@Component
public class RequestParametersValidator implements Validator {

    private ResourceBundleMessageSource messageSource;

    @Autowired
    public RequestParametersValidator(ResourceBundleMessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return Map.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        LinkedHashMap<String, String> parameters = (LinkedHashMap<String, String>) o;
        validateIdParameter(parameters, errors);
        if (errors.getAllErrors().isEmpty()) {
            validateTagIdParameter(parameters, errors);
        }
        if (errors.getAllErrors().isEmpty()) {
            validatePriceParameter(parameters, errors);
        }
        if (errors.getAllErrors().isEmpty()) {
            validateNameParameter(parameters, errors);
        }
        if (errors.getAllErrors().isEmpty()) {
            validateDescriptionParameter(parameters, errors);
        }
        if (errors.getAllErrors().isEmpty()) {
            validateCreationDateParameter(parameters, errors);
        }
        if (errors.getAllErrors().isEmpty()) {
            validateExpirationDateParameter(parameters, errors);
        }
        if (errors.getAllErrors().isEmpty()) {
            validateModificationDateParameter(parameters, errors);
        }
        if (errors.getAllErrors().isEmpty()) {
            validateSortParameter(parameters, errors);
        }
        if (errors.getAllErrors().isEmpty()) {
            validateLimitParameter(parameters, errors);
        }
        if (errors.getAllErrors().isEmpty()) {
            validateOffsetParameter(parameters, errors);
        }
    }

    private void validateIdParameter(Map<String, String> parameters, Errors errors) {
        if (parameters.containsKey(ID) && !parameters.get(ID).isBlank() && !parameters.get(ID).matches(ID_PARAMETER_REGEX_PATTERN)) {
            errors.reject(messageSource.getMessage( "violation.message.id", null, null));
        }
    }

    private void validateTagIdParameter(Map<String, String> parameters, Errors errors) {
        if (parameters.containsKey(TAG_ID) && !parameters.get(TAG_ID).isBlank() && !parameters.get(TAG_ID).matches(TAG_ID_PARAMETER_REGEX_PATTERN)) {
            errors.reject(messageSource.getMessage( "violation.message.tag", null, null));
        }
    }

    private void validatePriceParameter(Map<String, String> parameters, Errors errors) {
        if (parameters.containsKey(PRICE) && !parameters.get(PRICE).isBlank() && !parameters.get(PRICE).matches(PRICE_REGEX_PATTERN)) {
            errors.reject(messageSource.getMessage( "violation.message.price", null, null));
        }
    }

    private void validateNameParameter(Map<String, String> parameters, Errors errors) {
        if (parameters.containsKey(NAME) && !parameters.get(NAME).isBlank() && !parameters.get(NAME).matches(NAME_REGEX_PATTERN)) {
            errors.reject(messageSource.getMessage( "violation.message.name", null, null));
        }
    }

    private void validateDescriptionParameter(Map<String, String> parameters, Errors errors) {
        if (parameters.containsKey(DESCRIPTION) && !parameters.get(DESCRIPTION).isBlank()
                && !parameters.get(DESCRIPTION).matches(DESCRIPTION_REGEX_PATTERN)) {
            errors.reject(messageSource.getMessage( "violation.message.description", null, null));
        }
    }
    private void validateExpirationDateParameter(Map<String, String> parameters, Errors errors) {
        if (parameters.containsKey(EXPIRATION_DATE) && !parameters.get(EXPIRATION_DATE).isBlank()) {
            validateDate(parameters.get(EXPIRATION_DATE),
                    messageSource.getMessage( "violation.message.expirationdate", null, null), errors);
        }

    }
    private void validateCreationDateParameter(Map<String, String> parameters, Errors errors) {
        if (parameters.containsKey(CREATION_DATE) && !parameters.get(CREATION_DATE).isBlank()) {
            validateDate(parameters.get(CREATION_DATE),
                    messageSource.getMessage( "violation.message.creationdate", null, null), errors);
        }
    }
    private void validateModificationDateParameter(Map<String, String> parameters, Errors errors) {
        if (parameters.containsKey(MODIFICATION_DATE) && !parameters.get(MODIFICATION_DATE).isBlank()) {
            validateDate(parameters.get(MODIFICATION_DATE),
                    messageSource.getMessage( "violation.message.modificationdate", null, null), errors);
        }
    }
    private void validateSortParameter(Map<String, String> parameters, Errors errors) {
        if (parameters.containsKey(SORT) && !parameters.get(SORT).isBlank()
                && !parameters.get(SORT).matches(SORT_REGEX_PATTERN)) {
            errors.reject(messageSource.getMessage( "violation.message.sort", null, null));
        }
    }
    private void validateLimitParameter(Map<String, String> parameters, Errors errors) {
        if (parameters.containsKey(LIMIT) && !parameters.get(LIMIT).isBlank()
                && !parameters.get(LIMIT).matches(LIMIT_REGEX_PATTERN)) {
            errors.reject(messageSource.getMessage( "violation.message.limit", null, null));
        }
    }
    private void validateOffsetParameter(Map<String, String> parameters, Errors errors) {
        if (parameters.containsKey(OFFSET) && !parameters.get(OFFSET).isBlank()
                && !parameters.get(OFFSET).matches(OFFSET_REGEX_PATTERN)) {
            errors.reject(messageSource.getMessage( "violation.message.offset", null, null));
        }
    }

    private void validateDate(String parameter, String violationMessage, Errors errors) {
        if (parameter.startsWith(NOT_IN)) {
            parameter = parameter.replaceFirst(NOT_IN, "");
        } else if (parameter.startsWith(BETWEEN)) {
            parameter = parameter.replaceFirst(BETWEEN, "");
        } else if (parameter.startsWith(NOT_BERWEEN)) {
            parameter = parameter.replaceFirst(NOT_BERWEEN, "");
        }
        for (String s: parameter.split(",")) {
            if (!isDateValid(s)) {
                errors.reject(violationMessage);
            }
        }
    }

    private boolean isDateValid(String date) {
        try {
            LocalDate.parse(date);
        } catch (DateTimeParseException e) {
            return false;
        }
        return true;
    }
}
