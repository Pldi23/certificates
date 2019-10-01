package com.epam.esm.validator;

/**
 * giftcertificates
 *
 * @author Dzmitry Platonov on 2019-09-30.
 * @version 0.0.1
 */
class RegexPatternConstant {

    static final String ID_PARAMETER_REGEX_PATTERN = "(\\d)|(^bw:\\d,\\d)|(^nbw:\\d,\\d)";
    static final String TAG_ID_PARAMETER_REGEX_PATTERN = "(\\d)|(^bw:\\d,\\d)|(^nbw:\\d,\\d)";
    static final String PRICE_REGEX_PATTERN = "^(-)?[\\d]*(?:\\.[\\d]*)*|^n?bw:[\\d]*(?:\\.[\\d]*)*,[\\d]*(?:\\.[\\d]*)*";
    static final String NAME_REGEX_PATTERN = "^(-)?l:(\\w+)|^(-)?([\\w,]+)";
    static final String DESCRIPTION_REGEX_PATTERN = "^(-)?l:([\\w ]+)|^(-)?([\\w, ]+)";
    static final String LIMIT_REGEX_PATTERN = "\\d+";
    static final String OFFSET_REGEX_PATTERN = "\\d+";
    static final String SORT_REGEX_PATTERN =
            "^-?(name|description|price|modificationdate|expirationdate|creationdate|id)";

    private RegexPatternConstant() {
    }


}
