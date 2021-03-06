package com.epam.esm.dto;

import com.epam.esm.validator.ValidTagSet;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.NoArgsConstructor;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

@NoArgsConstructor
@JsonIgnoreProperties(value={ "tags" }, allowSetters=true, allowGetters = true)
public class GiftCertificateDTO {

    @Null(message = "{violation.input.certificate.id}")
    private Long id;

    @NotBlank(message = "{violation.name.not.blank}")
    @Size(min = 1, max = 50, message = "{violation.name.size}")
    @Pattern(regexp = "([\\w-]+(?: [\\w-]+)+)|([\\w-]+)", message = "{violation.certificate.name.pattern}")
    private String name;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Size(min = 1, max = 1000, message = "{violation.description}")
    private String description;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotNull(message = "{violation.price.not.null}")
    @DecimalMin(value = "0", message = "{violation.price}")
    private BigDecimal price;

    @Null(message = "{violation.creation.date.input}")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate creationDate;


    @Null(message = "{violation.modification.date.input}")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate modificationDate;


    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate expirationDate;

    @Null(message = "{violation.string.active}")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String active;

    @ValidTagSet(message = "{violation.tag.set}")
    private Set<TagDTO> tags;





    public static class Builder {
        private GiftCertificateDTO giftCertificateDTO;

        public Builder() {
            giftCertificateDTO = new GiftCertificateDTO();
        }

        public Builder withId(Long id) {
            giftCertificateDTO.id = id;
            return this;
        }

        public Builder withName(String name) {
            giftCertificateDTO.name = name;
            return this;
        }

        public Builder withDescription(String description) {
            giftCertificateDTO.description = description;
            return this;
        }

        public Builder withPrice(BigDecimal price) {
            giftCertificateDTO.price = price;
            return this;
        }

        public Builder withCreationDate(LocalDate creationDate) {
            giftCertificateDTO.creationDate = creationDate;
            return this;
        }

        public Builder withModificationDate(LocalDate modificationDate) {
            giftCertificateDTO.modificationDate = modificationDate;
            return this;
        }

        public Builder withExpirationDate(LocalDate expirationDate) {
            giftCertificateDTO.expirationDate = expirationDate;
            return this;
        }


        public Builder withTags(Set<TagDTO> tags) {
            giftCertificateDTO.tags = tags;
            return this;
        }

        public Builder withIsActive(String active) {
            giftCertificateDTO.active = active;
            return this;
        }

        public GiftCertificateDTO build() {
            return giftCertificateDTO;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Set<TagDTO> getTags() {
        return tags;
    }

    public void setTags(Set<TagDTO> tags) {
        this.tags = tags;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    @AssertTrue(message = "{violation.date.creation.modification}")
    private boolean isCreationDateBeforeModificationDate() {
        if (creationDate != null && modificationDate != null) {
            return this.creationDate.isBefore(this.modificationDate);
        }
        return true;
    }

    @AssertTrue(message = "{violation.date.creation.expiration}")
    private boolean isCreationDateBeforeExpirationDate() {
        if (creationDate != null && expirationDate != null) {
            return this.creationDate.isBefore(this.expirationDate);
        }
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GiftCertificateDTO that = (GiftCertificateDTO) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(description, that.description) &&
                Objects.equals(price, that.price) &&
                Objects.equals(creationDate, that.creationDate) &&
                Objects.equals(modificationDate, that.modificationDate) &&
                Objects.equals(expirationDate, that.expirationDate) &&
                Objects.equals(tags, that.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, price, creationDate, modificationDate, expirationDate, tags);
    }

    @Override
    public String toString() {
        return "GiftCertificateDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", creationDate=" + creationDate +
                ", modificationDate=" + modificationDate +
                ", expirationDate=" + expirationDate +
                ", tags=" + tags +
                '}';
    }
}
