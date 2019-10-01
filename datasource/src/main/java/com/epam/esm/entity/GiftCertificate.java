package com.epam.esm.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

/**
 * gift certificates
 *
 * @author Dzmitry Platonov on 2019-09-23.
 * @version 0.0.1
 */
public class GiftCertificate extends Entity {

    private String name;
    private String description;
    private BigDecimal price;
    private LocalDate creationDate;
    private LocalDate modificationDate;
    private LocalDate expirationDate;

    private Set<Tag> tags;

    private GiftCertificate() {
    }

    public static class Builder {
        private GiftCertificate giftCertificate;

        public Builder() {
            giftCertificate = new GiftCertificate();
        }

        public Builder withId(Long id) {
            giftCertificate.setId(id);
            return this;
        }

        public Builder withName(String name) {
            giftCertificate.name = name;
            return this;
        }
        public Builder withDescription(String description) {
            giftCertificate.description = description;
            return this;
        }
        public Builder withPrice(BigDecimal price) {
            giftCertificate.price = price;
            return this;
        }
        public Builder withCreationDate(LocalDate creationDate) {
            giftCertificate.creationDate = creationDate;
            return this;
        }
        public Builder withModificationDate(LocalDate modificationDate) {
            giftCertificate.modificationDate = modificationDate;
            return this;
        }
        public Builder withExpirationDate(LocalDate expirationDate) {
            giftCertificate.expirationDate = expirationDate;
            return this;
        }


        public Builder withTags(Set<Tag> tags) {
            giftCertificate.tags = tags;
            return this;
        }

        public GiftCertificate build() {
            return giftCertificate;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDate getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(LocalDate modificationDate) {
        this.modificationDate = modificationDate;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GiftCertificate that = (GiftCertificate) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(description, that.description) &&
                Objects.equals(price, that.price) &&
                Objects.equals(creationDate, that.creationDate) &&
                Objects.equals(modificationDate, that.modificationDate) &&
                Objects.equals(expirationDate, that.expirationDate) &&
                Objects.equals(tags, that.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, price, creationDate, modificationDate, expirationDate, tags);
    }

    @Override
    public String toString() {
        return "GiftCertificate{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", creationDate=" + creationDate +
                ", modificationDate=" + modificationDate +
                ", expirationDate=" + expirationDate +
                ", tags=" + tags +
                '}';
    }
}
