package com.epam.esm.repository.predicate;

import com.epam.esm.entity.Tag;
import com.epam.esm.repository.constant.JpaConstant;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * to specify {@link Tag} by user
 *
 * @author Dzmitry Platonov on 2019-10-25.
 * @version 0.0.1
 */
public class TagHasUserSpecification implements Specification<Tag> {

    private Long id;

    public TagHasUserSpecification(Long id) {
        this.id = id;
    }

    @Override
    public List<Predicate> toPredicates(Root<Tag> root, CriteriaQuery query, CriteriaBuilder cb) {
        return List.of(cb.equal(root
                .join(JpaConstant.GIFT_CERTIFICATES)
                .join(JpaConstant.ORDER_CERTIFICATE)
                .join(JpaConstant.ORDER)
                .get(JpaConstant.USER), id));
    }
}
