package com.epam.esm.dto;

import com.epam.esm.entity.criteria.*;


/**
 * gift certificates
 *
 * @author Dzmitry Platonov on 2019-09-26.
 * @version 0.0.1
 */
public class SearchCriteriaDTO {

    private IdCriteria idCriteria;
    private NameCriteria nameCriteria;
    private DescriptionCriteria descriptionCriteria;
    private CreationDateCriteria creationDateCriteria;
    private ModificationDateCriteria modificationDateCriteria;
    private ExpirationDateCriteria expirationDateCriteria;


    private SearchCriteriaDTO() {
    }

    public static class Builder {
        private SearchCriteriaDTO searchCriteriaDTO;

        public Builder() {
            searchCriteriaDTO = new SearchCriteriaDTO();
        }

        public Builder withIdCriteria(IdCriteria idCriteria) {
            searchCriteriaDTO.idCriteria = idCriteria;
            return this;
        }

        public Builder withNameCriteria(NameCriteria nameCriteria) {
            searchCriteriaDTO.nameCriteria = nameCriteria;
            return this;
        }
        public Builder withDescriptionCriteria(DescriptionCriteria descriptionCriteria) {
            searchCriteriaDTO.descriptionCriteria = descriptionCriteria;
            return this;
        }
        public Builder withCreationDateCriteria(CreationDateCriteria creationDateCriteria) {
            searchCriteriaDTO.creationDateCriteria = creationDateCriteria;
            return this;
        }
        public Builder withModificationDateCriteria(ModificationDateCriteria modificationDateCriteria) {
            searchCriteriaDTO.modificationDateCriteria = modificationDateCriteria;
            return this;
        }
        public Builder withExpirationDateCriteria(ExpirationDateCriteria expirationDateCriteria) {
            searchCriteriaDTO.expirationDateCriteria = expirationDateCriteria;
            return this;
        }

        public SearchCriteriaDTO build() {
            return searchCriteriaDTO;
        }
    }

    public IdCriteria getIdCriteria() {
        return idCriteria;
    }

    public NameCriteria getNameCriteria() {
        return nameCriteria;
    }

    public DescriptionCriteria getDescriptionCriteria() {
        return descriptionCriteria;
    }

    public CreationDateCriteria getCreationDateCriteria() {
        return creationDateCriteria;
    }

    public ModificationDateCriteria getModificationDateCriteria() {
        return modificationDateCriteria;
    }

    public ExpirationDateCriteria getExpirationDateCriteria() {
        return expirationDateCriteria;
    }
}
