package com.epam.esm.entity.criteria;

import java.util.Objects;


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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SearchCriteria that = (SearchCriteria) o;
        return Objects.equals(idCriteria, that.idCriteria) &&
                Objects.equals(nameCriteria, that.nameCriteria) &&
                Objects.equals(descriptionCriteria, that.descriptionCriteria) &&
                Objects.equals(creationDateCriteria, that.creationDateCriteria) &&
                Objects.equals(modificationDateCriteria, that.modificationDateCriteria) &&
                Objects.equals(expirationDateCriteria, that.expirationDateCriteria) &&
                Objects.equals(priceCriteria, that.priceCriteria) &&
                Objects.equals(tagCriteria, that.tagCriteria) &&
                Objects.equals(tagNameCriteria, that.tagNameCriteria);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idCriteria, nameCriteria, descriptionCriteria, creationDateCriteria, modificationDateCriteria, expirationDateCriteria, priceCriteria, tagCriteria, tagNameCriteria);
    }
}
