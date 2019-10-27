package com.epam.esm.validator;

import com.epam.esm.dto.TagDTO;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Set;

/**
 * gift-certificates
 *
 * @author Dzmitry Platonov on 2019-10-26.
 * @version 0.0.1
 */
public class TagSetValidator implements ConstraintValidator<ValidTagSet, Set<TagDTO>> {

    private static final int TAG_TITLE_MIN_LENGTH = 1;
    private static final int TAG_TITLE_MAX_LENGTH = 200;

    @Override
    public boolean isValid(Set<TagDTO> tagDTOS, ConstraintValidatorContext constraintValidatorContext) {
        return tagDTOS.stream().allMatch(tagDTO -> checkIdentifier(tagDTO) && checkId(tagDTO) && checkTitle(tagDTO));
    }

    private boolean checkId(TagDTO tagDTO) {
        if (tagDTO.getId() != null) {
            return tagDTO.getId() > 0;
        }
        return true;
    }

    private boolean checkTitle(TagDTO tagDTO) {
        if (tagDTO.getTitle() != null) {
            return tagDTO.getTitle().matches("([\\w-]+(?: [\\w-]+)+)|([\\w-]+)")
                    && tagDTO.getTitle().length() >= TAG_TITLE_MIN_LENGTH
                    && tagDTO.getTitle().length() < TAG_TITLE_MAX_LENGTH;
        }
        return true;
    }

    private boolean checkIdentifier(TagDTO tagDTO) {
        return tagDTO.getId() != null || tagDTO.getTitle() != null;
    }
}
