package com.epam.esm.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@SqlResultSetMapping(
        name="tagsCustomMapping",
        classes={@ConstructorResult(
                        targetClass=Tag.class,
                        columns={
                                @ColumnResult(name="id", type = Long.class),
                                @ColumnResult(name="title", type = String.class)})})

@NamedNativeQuery(name="Tag.saveAllTags", query="select * from insert_tag_list(:tags)", resultSetMapping="tagsCustomMapping")
@Entity
@Table(name = "tag")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
//@ToString(exclude = "giftCertificates")
@EqualsAndHashCode
public class Tag{

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;

    @Size(min = 1, max = 200, message = "{violation.tag.title.length}")
    @Pattern(regexp = "([\\w-]+(?: [\\w-]+)+)|([\\w-]+)", message = "{violation.tag.title.pattern}")
    @Column(unique = true, updatable = false)
    private String title;

//    @ManyToMany(fetch = FetchType.LAZY)
//    @JoinTable(name="certificate_tag",
//            joinColumns=@JoinColumn(name="tag_id"),
//            inverseJoinColumns=@JoinColumn(name="certificate_id"))
//    private Set<GiftCertificate> giftCertificates;

}
