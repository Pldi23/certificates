package com.epam.esm.repository.hibernate.predicate;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.criteria.SearchCriteria;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * gift-certificates
 *
 * @author Dzmitry Platonov on 2019-10-15.
 * @version 0.0.1
 */
@Component
public class CriteriaBuilderHelper {

    public void buildSearchCriteriaPredicates(List<Predicate> predicates, SearchCriteria searchCriteria,
                                              CriteriaBuilder criteriaBuilder, Root<GiftCertificate> rootQuery) {
        if (searchCriteria != null) {
            buildSearchNamePredicates(predicates, searchCriteria, criteriaBuilder, rootQuery);
            buildTagIdPredicates(predicates, searchCriteria, criteriaBuilder, rootQuery);
            buildTagNamePredicates(predicates, searchCriteria, criteriaBuilder, rootQuery);
            buildSearchDescriptionPredicates(predicates, searchCriteria, criteriaBuilder, rootQuery);
            buildSearchIdPredicates(predicates, searchCriteria, criteriaBuilder, rootQuery);
            buildPricePredicates(predicates, searchCriteria, criteriaBuilder, rootQuery);
            buildCreationDatePredicates(predicates, searchCriteria, criteriaBuilder, rootQuery);
            buildModificationDatePredicates(predicates, searchCriteria, criteriaBuilder, rootQuery);
            buildExpirationDatePredicates(predicates, searchCriteria, criteriaBuilder, rootQuery);
        }
    }

    private void buildExpirationDatePredicates(List<Predicate> predicates, SearchCriteria searchCriteria,
                                                 CriteriaBuilder criteriaBuilder, Root<GiftCertificate> rootQuery) {
        if (searchCriteria.getExpirationDateCriteria() != null) {
            switch (searchCriteria.getExpirationDateCriteria().getSearchType()) {
                case IN:
                    for (LocalDate date : searchCriteria.getExpirationDateCriteria().getCriteriaList()) {
                        predicates.add(criteriaBuilder.equal(rootQuery.get("expirationDate"), date));
                    }
                    break;
                case NOT_IN:
                    for (LocalDate date : searchCriteria.getExpirationDateCriteria().getCriteriaList()) {
                        predicates.add(criteriaBuilder.notEqual(rootQuery.get("expirationDate"), date));
                    }
                    break;
                case BETWEEN:
                    predicates.add(criteriaBuilder.between(rootQuery.get("expirationDate"),
                            searchCriteria.getExpirationDateCriteria().getCriteriaList().get(0),
                            searchCriteria.getExpirationDateCriteria().getCriteriaList().get(1)));
                    break;
                case NOT_BETWEEN:
                    predicates.add(criteriaBuilder.lessThan(rootQuery.get("expirationDate"),
                            searchCriteria.getExpirationDateCriteria().getCriteriaList().get(0)));
                    predicates.add(criteriaBuilder.greaterThan(rootQuery.get("expirationDate"),
                            searchCriteria.getExpirationDateCriteria().getCriteriaList().get(1)));
                    break;
            }
        }
    }

    private void buildModificationDatePredicates(List<Predicate> predicates, SearchCriteria searchCriteria,
                                             CriteriaBuilder criteriaBuilder, Root<GiftCertificate> rootQuery) {
        if (searchCriteria.getModificationDateCriteria() != null) {
            switch (searchCriteria.getModificationDateCriteria().getParameterSearchType()) {
                case IN:
                    for (LocalDate date : searchCriteria.getModificationDateCriteria().getCriteriaList()) {
                        predicates.add(criteriaBuilder.equal(rootQuery.get("modificationDate"), date));
                    }
                    break;
                case NOT_IN:
                    for (LocalDate date : searchCriteria.getModificationDateCriteria().getCriteriaList()) {
                        predicates.add(criteriaBuilder.notEqual(rootQuery.get("modificationDate"), date));
                    }
                    break;
                case BETWEEN:
                    predicates.add(criteriaBuilder.between(rootQuery.get("modificationDate"),
                            searchCriteria.getModificationDateCriteria().getCriteriaList().get(0),
                            searchCriteria.getModificationDateCriteria().getCriteriaList().get(1)));
                    break;
                case NOT_BETWEEN:
                    predicates.add(criteriaBuilder.lessThan(rootQuery.get("modificationDate"),
                            searchCriteria.getModificationDateCriteria().getCriteriaList().get(0)));
                    predicates.add(criteriaBuilder.greaterThan(rootQuery.get("modificationDate"),
                            searchCriteria.getModificationDateCriteria().getCriteriaList().get(1)));
                    break;
            }
        }
    }

    private void buildCreationDatePredicates(List<Predicate> predicates, SearchCriteria searchCriteria,
                                             CriteriaBuilder criteriaBuilder, Root<GiftCertificate> rootQuery) {
        if (searchCriteria.getCreationDateCriteria() != null) {
            switch (searchCriteria.getCreationDateCriteria().getParameterSearchType()) {
                case IN:
                    for (LocalDate date : searchCriteria.getCreationDateCriteria().getCriteriaList()) {
                        predicates.add(criteriaBuilder.equal(rootQuery.get("creationDate"), date));
                    }
                    break;
                case NOT_IN:
                    for (LocalDate date : searchCriteria.getCreationDateCriteria().getCriteriaList()) {
                        predicates.add(criteriaBuilder.notEqual(rootQuery.get("creationDate"), date));
                    }
                    break;
                case BETWEEN:
                    predicates.add(criteriaBuilder.between(rootQuery.get("creationDate"),
                            searchCriteria.getCreationDateCriteria().getCriteriaList().get(0),
                            searchCriteria.getCreationDateCriteria().getCriteriaList().get(1)));
                    break;
                case NOT_BETWEEN:
                    predicates.add(criteriaBuilder.lessThan(rootQuery.get("creationDate"),
                            searchCriteria.getCreationDateCriteria().getCriteriaList().get(0)));
                    predicates.add(criteriaBuilder.greaterThan(rootQuery.get("creationDate"),
                            searchCriteria.getCreationDateCriteria().getCriteriaList().get(1)));
                    break;
            }
        }
    }

    private void buildPricePredicates(List<Predicate> predicates, SearchCriteria searchCriteria,
                                      CriteriaBuilder criteriaBuilder, Root<GiftCertificate> rootQuery) {
        if (searchCriteria.getPriceCriteria() != null) {
            switch (searchCriteria.getPriceCriteria().getParameterSearchType()) {
                case IN:
                    for (BigDecimal price : searchCriteria.getPriceCriteria().getCriteriaList()) {
                        predicates.add(criteriaBuilder.equal(rootQuery.get("price"), price));
                    }
                    break;
                case NOT_IN:
                    for (BigDecimal price : searchCriteria.getPriceCriteria().getCriteriaList()) {
                        predicates.add(criteriaBuilder.notEqual(rootQuery.get("price"), price));
                    }
                    break;
                case BETWEEN:
                    predicates.add(criteriaBuilder.between(rootQuery.get("price"),
                            searchCriteria.getPriceCriteria().getCriteriaList().get(0),
                            searchCriteria.getPriceCriteria().getCriteriaList().get(1)));
                    break;
                case NOT_BETWEEN:
                    predicates.add(criteriaBuilder.lessThan(rootQuery.get("price"),
                            searchCriteria.getPriceCriteria().getCriteriaList().get(0)));
                    predicates.add(criteriaBuilder.greaterThan(rootQuery.get("price"),
                            searchCriteria.getPriceCriteria().getCriteriaList().get(1)));
                    break;
            }
        }
    }

    private void buildSearchIdPredicates(List<Predicate> predicates, SearchCriteria searchCriteria,
                                         CriteriaBuilder criteriaBuilder, Root<GiftCertificate> rootQuery) {
        if (searchCriteria.getIdCriteria() != null) {
            switch (searchCriteria.getIdCriteria().getParameterSearchType()) {
                case IN:
                    for (long id : searchCriteria.getIdCriteria().getCriteriaList()) {
                        predicates.add(criteriaBuilder.equal(rootQuery.get("id"), id));
                    }
                    break;
                case NOT_IN:
                    for (long id : searchCriteria.getIdCriteria().getCriteriaList()) {
                        predicates.add(criteriaBuilder.notEqual(rootQuery.get("id"), id));
                    }
                    break;
                case BETWEEN:
                    predicates.add(criteriaBuilder.between(rootQuery.get("id"),
                            searchCriteria.getIdCriteria().getCriteriaList().get(0),
                            searchCriteria.getIdCriteria().getCriteriaList().get(1)));
                    break;
                case NOT_BETWEEN:
                    predicates.add(criteriaBuilder.lessThan(rootQuery.get("id"),
                            searchCriteria.getIdCriteria().getCriteriaList().get(0)));
                    predicates.add(criteriaBuilder.greaterThan(rootQuery.get("id"),
                            searchCriteria.getIdCriteria().getCriteriaList().get(1)));

                    break;
            }
        }
    }

    private void buildSearchNamePredicates(List<Predicate> predicates, SearchCriteria searchCriteria,
                                           CriteriaBuilder criteriaBuilder, Root<GiftCertificate> rootQuery) {
        if (searchCriteria.getNameCriteria() != null) {
            switch (searchCriteria.getNameCriteria().getSearchType()) {
                case IN:
                    for (String name : searchCriteria.getNameCriteria().getCriteriaList()) {
                        predicates.add(criteriaBuilder.equal(rootQuery.get("name"), name));
                    }
                    break;
                case NOT_IN:
                    for (String name : searchCriteria.getNameCriteria().getCriteriaList()) {
                        predicates.add(criteriaBuilder.notEqual(rootQuery.get("name"), name));
                    }
                    break;
                case LIKE:
                    for (String name : searchCriteria.getNameCriteria().getCriteriaList()) {
                        predicates.add(criteriaBuilder.like(rootQuery.get("name"), "%" + name + "%"));
                    }
                    break;
                case NOT_LIKE:
                    for (String name : searchCriteria.getNameCriteria().getCriteriaList()) {
                        predicates.add(criteriaBuilder.notLike(rootQuery.get("name"), "%" + name + "%"));
                    }
                    break;
            }
        }
    }

    private void buildSearchDescriptionPredicates(List<Predicate> predicates, SearchCriteria searchCriteria,
                                                  CriteriaBuilder criteriaBuilder, Root<GiftCertificate> rootQuery) {
        if (searchCriteria.getDescriptionCriteria() != null) {
            switch (searchCriteria.getDescriptionCriteria().getSearchType()) {
                case IN:
                    for (String name : searchCriteria.getDescriptionCriteria().getCriteriaList()) {
                        predicates.add(criteriaBuilder.equal(rootQuery.get("description"), name));
                    }
                    break;
                case NOT_IN:
                    for (String name : searchCriteria.getDescriptionCriteria().getCriteriaList()) {
                        predicates.add(criteriaBuilder.notEqual(rootQuery.get("description"), name));
                    }
                    break;
                case LIKE:
                    for (String name : searchCriteria.getDescriptionCriteria().getCriteriaList()) {
                        predicates.add(criteriaBuilder.like(rootQuery.get("description"), "%" + name + "%"));
                    }
                    break;
                case NOT_LIKE:
                    for (String name : searchCriteria.getDescriptionCriteria().getCriteriaList()) {
                        predicates.add(criteriaBuilder.notLike(rootQuery.get("description"), "%" + name + "%"));
                    }
                    break;
            }
        }
    }

    private void buildTagIdPredicates(List<Predicate> predicates, SearchCriteria searchCriteria,
                                      CriteriaBuilder criteriaBuilder, Root<GiftCertificate> rootQuery) {
        if (searchCriteria.getTagCriteria() != null) {
            for (long tagId : searchCriteria.getTagCriteria().getTagIds()) {
                Join<GiftCertificate, Tag> join = rootQuery.join("tags");
                predicates.add(criteriaBuilder.equal(join.get("id"), tagId));
            }
        }
    }

    private void buildTagNamePredicates(List<Predicate> predicates, SearchCriteria searchCriteria,
                                        CriteriaBuilder criteriaBuilder, Root<GiftCertificate> rootQuery) {
        if (searchCriteria.getTagNameCriteria() != null) {
            for (String tagName : searchCriteria.getTagNameCriteria().getTagNames()) {
                Join<GiftCertificate, Tag> join = rootQuery.join("tags");
                predicates.add(criteriaBuilder.equal(join.get("title"), tagName));
            }
        }
    }


}
