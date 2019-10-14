package com.epam.esm.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

/**
 * gift certificates
 *
 * @author Dzmitry Platonov on 2019-09-23.
 * @version 0.0.1
 */
@Entity
@Table(name = "certificate")
//@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GiftCertificate {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private LocalDate creationDate;
    private LocalDate modificationDate;
    private LocalDate expirationDate;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinTable(name="certificate_tag",
            joinColumns=@JoinColumn(name="certificate_id"),
            inverseJoinColumns=@JoinColumn(name="tag_id"))
    private Set<Tag> tags;

//    public void addTag(Tag tag) {
//        tags.add(tag);
//        tag.getGiftCertificates().add(this);
//    }
//
//    public void removeTag(Tag tag) {
//        tags.deleteById(tag);
//        tag.getGiftCertificates().deleteById(this);
//    }

}
