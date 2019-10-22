package com.epam.esm.repository.predicate;

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

import static com.epam.esm.repository.constant.JpaConstant.*;

/**
 * helper class to build predicate parameters for criteria
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
                        predicates.add(criteriaBuilder.equal(rootQuery.get(EXPIRATION_DATE), date));
                    }
                    break;
                case NOT_IN:
                    for (LocalDate date : searchCriteria.getExpirationDateCriteria().getCriteriaList()) {
                        predicates.add(criteriaBuilder.notEqual(rootQuery.get(EXPIRATION_DATE), date));
                    }
                    break;
                case BETWEEN:
                    predicates.add(criteriaBuilder.between(rootQuery.get(EXPIRATION_DATE),
                            searchCriteria.getExpirationDateCriteria().getCriteriaList().get(0),
                            searchCriteria.getExpirationDateCriteria().getCriteriaList().get(1)));
                    break;
                case NOT_BETWEEN:
                    predicates.add(criteriaBuilder.lessThan(rootQuery.get(EXPIRATION_DATE),
                            searchCriteria.getExpirationDateCriteria().getCriteriaList().get(0)));
                    predicates.add(criteriaBuilder.greaterThan(rootQuery.get(EXPIRATION_DATE),
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
                        predicates.add(criteriaBuilder.equal(rootQuery.get(MODIFICATION_DATE), date));
                    }
                    break;
                case NOT_IN:
                    for (LocalDate date : searchCriteria.getModificationDateCriteria().getCriteriaList()) {
                        predicates.add(criteriaBuilder.notEqual(rootQuery.get(MODIFICATION_DATE), date));
                    }
                    break;
                case BETWEEN:
                    predicates.add(criteriaBuilder.between(rootQuery.get(MODIFICATION_DATE),
                            searchCriteria.getModificationDateCriteria().getCriteriaList().get(0),
                            searchCriteria.getModificationDateCriteria().getCriteriaList().get(1)));
                    break;
                case NOT_BETWEEN:
                    predicates.add(criteriaBuilder.lessThan(rootQuery.get(MODIFICATION_DATE),
                            searchCriteria.getModificationDateCriteria().getCriteriaList().get(0)));
                    predicates.add(criteriaBuilder.greaterThan(rootQuery.get(MODIFICATION_DATE),
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
                        predicates.add(criteriaBuilder.equal(rootQuery.get(CREATION_DATE), date));
                    }
                    break;
                case NOT_IN:
                    for (LocalDate date : searchCriteria.getCreationDateCriteria().getCriteriaList()) {
                        predicates.add(criteriaBuilder.notEqual(rootQuery.get(CREATION_DATE), date));
                    }
                    break;
                case BETWEEN:
                    predicates.add(criteriaBuilder.between(rootQuery.get(CREATION_DATE),
                            searchCriteria.getCreationDateCriteria().getCriteriaList().get(0),
                            searchCriteria.getCreationDateCriteria().getCriteriaList().get(1)));
                    break;
                case NOT_BETWEEN:
                    predicates.add(criteriaBuilder.lessThan(rootQuery.get(CREATION_DATE),
                            searchCriteria.getCreationDateCriteria().getCriteriaList().get(0)));
                    predicates.add(criteriaBuilder.greaterThan(rootQuery.get(CREATION_DATE),
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
                        predicates.add(criteriaBuilder.equal(rootQuery.get(PRICE), price));
                    }
                    break;
                case NOT_IN:
                    for (BigDecimal price : searchCriteria.getPriceCriteria().getCriteriaList()) {
                        predicates.add(criteriaBuilder.notEqual(rootQuery.get(PRICE), price));
                    }
                    break;
                case BETWEEN:
                    predicates.add(criteriaBuilder.between(rootQuery.get(PRICE),
                            searchCriteria.getPriceCriteria().getCriteriaList().get(0),
                            searchCriteria.getPriceCriteria().getCriteriaList().get(1)));
                    break;
                case NOT_BETWEEN:
                    predicates.add(criteriaBuilder.lessThan(rootQuery.get(PRICE),
                            searchCriteria.getPriceCriteria().getCriteriaList().get(0)));
                    predicates.add(criteriaBuilder.greaterThan(rootQuery.get(PRICE),
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
                        predicates.add(criteriaBuilder.equal(rootQuery.get(ID), id));
                    }
                    break;
                case NOT_IN:
                    for (long id : searchCriteria.getIdCriteria().getCriteriaList()) {
                        predicates.add(criteriaBuilder.notEqual(rootQuery.get(ID), id));
                    }
                    break;
                case BETWEEN:
                    predicates.add(criteriaBuilder.between(rootQuery.get(ID),
                            searchCriteria.getIdCriteria().getCriteriaList().get(0),
                            searchCriteria.getIdCriteria().getCriteriaList().get(1)));
                    break;
                case NOT_BETWEEN:
                    predicates.add(criteriaBuilder.lessThan(rootQuery.get(ID),
                            searchCriteria.getIdCriteria().getCriteriaList().get(0)));
                    predicates.add(criteriaBuilder.greaterThan(rootQuery.get(ID),
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
                        predicates.add(criteriaBuilder.equal(rootQuery.get(NAME), name));
                    }
                    break;
                case NOT_IN:
                    for (String name : searchCriteria.getNameCriteria().getCriteriaList()) {
                        predicates.add(criteriaBuilder.notEqual(rootQuery.get(NAME), name));
                    }
                    break;
                case LIKE:
                    for (String name : searchCriteria.getNameCriteria().getCriteriaList()) {
                        predicates.add(criteriaBuilder.like(rootQuery.get(NAME), "%" + name + "%"));
                    }
                    break;
                case NOT_LIKE:
                    for (String name : searchCriteria.getNameCriteria().getCriteriaList()) {
                        predicates.add(criteriaBuilder.notLike(rootQuery.get(NAME), "%" + name + "%"));
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
                        predicates.add(criteriaBuilder.equal(rootQuery.get(DESCRIPTION), name));
                    }
                    break;
                case NOT_IN:
                    for (String name : searchCriteria.getDescriptionCriteria().getCriteriaList()) {
                        predicates.add(criteriaBuilder.notEqual(rootQuery.get(DESCRIPTION), name));
                    }
                    break;
                case LIKE:
                    for (String name : searchCriteria.getDescriptionCriteria().getCriteriaList()) {
                        predicates.add(criteriaBuilder.like(rootQuery.get(DESCRIPTION), "%" + name + "%"));
                    }
                    break;
                case NOT_LIKE:
                    for (String name : searchCriteria.getDescriptionCriteria().getCriteriaList()) {
                        predicates.add(criteriaBuilder.notLike(rootQuery.get(DESCRIPTION), "%" + name + "%"));
                    }
                    break;
            }
        }
    }

    private void buildTagIdPredicates(List<Predicate> predicates, SearchCriteria searchCriteria,
                                      CriteriaBuilder criteriaBuilder, Root<GiftCertificate> rootQuery) {
        if (searchCriteria.getTagCriteria() != null) {
            for (long tagId : searchCriteria.getTagCriteria().getTagIds()) {
                Join<GiftCertificate, Tag> join = rootQuery.join(TAGS);
                predicates.add(criteriaBuilder.equal(join.get(ID), tagId));
            }
        }
    }

    private void buildTagNamePredicates(List<Predicate> predicates, SearchCriteria searchCriteria,
                                        CriteriaBuilder criteriaBuilder, Root<GiftCertificate> rootQuery) {
        if (searchCriteria.getTagNameCriteria() != null) {
            switch (searchCriteria.getTagNameCriteria().getTextSearchType()) {
                case IN:
                    for (String tagName : searchCriteria.getTagNameCriteria().getTagNames()) {
                        Join<GiftCertificate, Tag> join = rootQuery.join(TAGS);
                        predicates.add(criteriaBuilder.equal(join.get(TITLE), tagName));
                    }
                    break;
                case NOT_IN:
                    for (String tagName : searchCriteria.getTagNameCriteria().getTagNames()) {
                        Join<GiftCertificate, Tag> join = rootQuery.join(TAGS);
                        predicates.add(criteriaBuilder.notEqual(join.get(TITLE), tagName));
                    }
                    break;
                case LIKE:
                    for (String tagName : searchCriteria.getTagNameCriteria().getTagNames()) {
                        Join<GiftCertificate, Tag> join = rootQuery.join(TAGS);
                        predicates.add(criteriaBuilder.like(join.get(TITLE), "%" + tagName + "%"));
                    }
                    break;
                case NOT_LIKE:
                    for (String tagName : searchCriteria.getTagNameCriteria().getTagNames()) {
                        Join<GiftCertificate, Tag> join = rootQuery.join(TAGS);
                        predicates.add(criteriaBuilder.notLike(join.get(TITLE), "%" + tagName + "%"));
                    }
                    break;
            }
        }
    }
}
