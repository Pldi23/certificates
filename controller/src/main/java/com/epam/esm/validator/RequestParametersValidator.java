package com.epam.esm.controller.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * gift certificates
 *
 * @author Dzmitry Platonov on 2019-09-28.
 * @version 0.0.1
 */
@Component
public class RequestParametersValidator implements Validator {

    private static final String ID_PARAMETER_REGEX_PATTERN = "(\\d)|(^bw:\\d,\\d)|(^nbw:\\d,\\d)";
    private static final String TAG_ID_PARAMETER_REGEX_PATTERN = "(\\d)|(^bw:\\d,\\d)|(^nbw:\\d,\\d)";
    private static final String PRICE_REGEX_PATTERN = "^(-)?[\\d]*(?:\\.[\\d]*)*|^n?bw:[\\d]*(?:\\.[\\d]*)*,[\\d]*(?:\\.[\\d]*)*";
    private static final String NAME_REGEX_PATTERN = "^(-)?l:(\\w+)|^(-)?([\\w,]+)";
    private static final String DESCRIPTION_REGEX_PATTERN = "^(-)?l:([\\w ]+)|^(-)?([\\w, ]+)";
    private static final String LIMIT_REGEX_PATTERN = "\\d+";
    private static final String OFFSET_REGEX_PATTERN = "\\d+";
    private static final String SORT_REGEX_PATTERN =
            "^-?(name|description|price|modificationdate|expirationdate|creationdate|id)";

    private static final String ID = "id";
    private static final String TAG_ID = "tagid";
    private static final String PRICE = "price";
    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";
    private static final String EXPIRATION_DATE = "expirationdate";
    private static final String MODIFICATION_DATE = "modificationdate";
    private static final String CREATION_DATE = "creationdate";
    private static final String SORT = "sort";
    private static final String LIMIT = "limit";
    private static final String OFFSET = "offset";
    private static final String BETWEEN = "bw:";
    private static final String NOT_BERWEEN = "nbw:";
    private static final String NOT_IN = "-";


    private static final String ID_VIOLATION_MESSAGE = "id parameter incorrect. ex: id=1; id=bw:1,10; id=nbw:1,10";
    private static final String TAG_VIOLATION_MESSAGE = "tagId parameter incorrect. ex: tagId=1; tagId=bw:1,10; tagId=nbw:1,10";
    private static final String NAME_VIOLATION_MESSAGE =
            "name parameter incorrect. ex: name=dima,ar,as;name=-dima,ar; name=l:param; name=-l:dima,ar";
    private static final String PRICE_VIOLATION_MESSAGE =
            "price parameter incorrect. ex: price=1; price=bw:1.9,10.2; price=nbw:1.2,10.3; price=-25(not 25)";
    private static final String DESCRIPTION_VIOLATION_MESSAGE =
            "description parameter incorrect. ex: description=a lot of words; description=-words; " +
                    "description=l:word; description=-l:word";
    private static final String EXPIRATION_DATE_VIOLATION_MESSAGE =
            "expiration date violation ex: expirationdate=2000-12-31; date=-2020-01-01; date=nbw:2000-01-01,2010-01-01";
    private static final String MODIFICATION_DATE_VIOLATION_MESSAGE =
            "modification date violation ex: modificationdate=2000-12-31; modificationdate=-2020-01-01; " +
                    "modificationdate=nbw:2000-01-01,2010-01-01";
    private static final String CREATION_DATE_VIOLATION_MESSAGE =
            "creation date violation ex: creationdate=2000-12-31; creationdate=-2020-01-01; creationdate=nbw:2000-01-01,2010-01-01";

    private static final String SORT_VIOLATION_MESSAGE =
            "sort parameter incorrect. ex: sort=-name,expirationdate,price";
    private static final String LIMIT_VIOLATION_MESSAGE =
            "limit parameter incorrect. ex: limit=20";
    private static final String OFFSET_VIOLATION_MESSAGE =
            "offset parameter incorrect. ex: offset=0";

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
            errors.reject(ID_VIOLATION_MESSAGE);
        }
    }

    private void validateTagIdParameter(Map<String, String> parameters, Errors errors) {
        if (parameters.containsKey(TAG_ID) && !parameters.get(TAG_ID).isBlank() && !parameters.get(TAG_ID).matches(TAG_ID_PARAMETER_REGEX_PATTERN)) {
            errors.reject(TAG_VIOLATION_MESSAGE);
        }
    }

    private void validatePriceParameter(Map<String, String> parameters, Errors errors) {
        if (parameters.containsKey(PRICE) && !parameters.get(PRICE).isBlank() && !parameters.get(PRICE).matches(PRICE_REGEX_PATTERN)) {
            errors.reject(PRICE_VIOLATION_MESSAGE);
        }
    }

    private void validateNameParameter(Map<String, String> parameters, Errors errors) {
        if (parameters.containsKey(NAME) && !parameters.get(NAME).isBlank() && !parameters.get(NAME).matches(NAME_REGEX_PATTERN)) {
            errors.reject(NAME_VIOLATION_MESSAGE);
        }
    }

    private void validateDescriptionParameter(Map<String, String> parameters, Errors errors) {
        if (parameters.containsKey(DESCRIPTION) && !parameters.get(DESCRIPTION).isBlank()
                && !parameters.get(DESCRIPTION).matches(DESCRIPTION_REGEX_PATTERN)) {
            errors.reject(DESCRIPTION_VIOLATION_MESSAGE);
        }
    }
    private void validateExpirationDateParameter(Map<String, String> parameters, Errors errors) {
        if (parameters.containsKey(EXPIRATION_DATE) && !parameters.get(EXPIRATION_DATE).isBlank()) {
            validateDate(parameters.get(EXPIRATION_DATE), EXPIRATION_DATE_VIOLATION_MESSAGE, errors);
        }

    }
    private void validateCreationDateParameter(Map<String, String> parameters, Errors errors) {
        if (parameters.containsKey(CREATION_DATE) && !parameters.get(CREATION_DATE).isBlank()) {
            validateDate(parameters.get(CREATION_DATE), CREATION_DATE_VIOLATION_MESSAGE, errors);
        }
    }
    private void validateModificationDateParameter(Map<String, String> parameters, Errors errors) {
        if (parameters.containsKey(MODIFICATION_DATE) && !parameters.get(MODIFICATION_DATE).isBlank()) {
            validateDate(parameters.get(MODIFICATION_DATE), MODIFICATION_DATE_VIOLATION_MESSAGE, errors);
        }
    }
    private void validateSortParameter(Map<String, String> parameters, Errors errors) {
        if (parameters.containsKey(SORT) && !parameters.get(SORT).isBlank()
                && !parameters.get(SORT).matches(SORT_REGEX_PATTERN)) {
            errors.reject(SORT_VIOLATION_MESSAGE);
        }
    }
    private void validateLimitParameter(Map<String, String> parameters, Errors errors) {
        if (parameters.containsKey(LIMIT) && !parameters.get(LIMIT).isBlank()
                && !parameters.get(LIMIT).matches(LIMIT_REGEX_PATTERN)) {
            errors.reject(LIMIT_VIOLATION_MESSAGE);
        }
    }
    private void validateOffsetParameter(Map<String, String> parameters, Errors errors) {
        if (parameters.containsKey(OFFSET) && !parameters.get(OFFSET).isBlank()
                && !parameters.get(OFFSET).matches(OFFSET_REGEX_PATTERN)) {
            errors.reject(OFFSET_VIOLATION_MESSAGE);
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
