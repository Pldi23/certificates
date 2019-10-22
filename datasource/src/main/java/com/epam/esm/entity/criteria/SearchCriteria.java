package com.epam.esm.entity.criteria;

/**
 * gift certificates
 *
 * @author Dzmitry Platonov on 2019-09-26.
 * @version 0.0.1
 */
public class SearchCriteria {

    private IdCriteria idCriteria;
    private NameCriteria nameCriteria;
    private DescriptionCriteria descriptionCriteria;
    private CreationDateCriteria creationDateCriteria;
    private ModificationDateCriteria modificationDateCriteria;
    private ExpirationDateCriteria expirationDateCriteria;
    private PriceCriteria priceCriteria;
    private TagCriteria tagCriteria;
    private TagNameCriteria tagNameCriteria;


    private SearchCriteria() {
    }

    public static class Builder {
        private SearchCriteria searchCriteria;

        public Builder() {
            searchCriteria = new SearchCriteria();
        }

        public Builder withIdCriteria(IdCriteria idCriteria) {
            searchCriteria.idCriteria = idCriteria;
            return this;
        }

        public Builder withNameCriteria(NameCriteria nameCriteria) {
            searchCriteria.nameCriteria = nameCriteria;
            return this;
        }
        public Builder withDescriptionCriteria(DescriptionCriteria descriptionCriteria) {
            searchCriteria.descriptionCriteria = descriptionCriteria;
            return this;
        }
        public Builder withCreationDateCriteria(CreationDateCriteria creationDateCriteria) {
            searchCriteria.creationDateCriteria = creationDateCriteria;
            return this;
        }
        public Builder withModificationDateCriteria(ModificationDateCriteria modificationDateCriteria) {
            searchCriteria.modificationDateCriteria = modificationDateCriteria;
            return this;
        }
        public Builder withExpirationDateCriteria(ExpirationDateCriteria expirationDateCriteria) {
            searchCriteria.expirationDateCriteria = expirationDateCriteria;
            return this;
        }

        public Builder withPriceCriteria(PriceCriteria priceCriteria) {
            searchCriteria.priceCriteria = priceCriteria;
            return this;
        }

        public Builder withTagCriteria(TagCriteria tagCriteria) {
            searchCriteria.tagCriteria = tagCriteria;
            return this;
        }

        public Builder withTagNameCriteria(TagNameCriteria tagNameCriteria) {
            searchCriteria.tagNameCriteria = tagNameCriteria;
            return this;
        }

        public SearchCriteria build() {
            return searchCriteria;
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

    public PriceCriteria getPriceCriteria() {
        return priceCriteria;
    }

    public TagCriteria getTagCriteria() {
        return tagCriteria;
    }

    public TagNameCriteria getTagNameCriteria() {
        return tagNameCriteria;
    }

    @Override
    public String toString() {
        return "SearchCriteria{" +
                "idCriteria=" + idCriteria +
                ", nameCriteria=" + nameCriteria +
                ", descriptionCriteria=" + descriptionCriteria +
                ", creationDateCriteria=" + creationDateCriteria +
                ", modificationDateCriteria=" + modificationDateCriteria +
                ", expirationDateCriteria=" + expirationDateCriteria +
                ", priceCriteria=" + priceCriteria +
                ", tagCriteria=" + tagCriteria +
                ", tagNameCriteria=" + tagNameCriteria +
                '}';
    }
}
