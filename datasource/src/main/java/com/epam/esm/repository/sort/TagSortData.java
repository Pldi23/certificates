package com.epam.esm.repository.sort;

import com.epam.esm.entity.Tag;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;

import static com.epam.esm.repository.constant.JpaConstant.*;

/**
 * implementation of {@link Sortable} for {@link Tag}
 *
 * @author Dzmitry Platonov on 2019-10-25.
 * @version 0.0.1
 */
public class TagSortData implements Sortable<Tag> {

    private String sortParameter;

    public TagSortData(String sortParameter) {
        this.sortParameter = sortParameter;
    }

    @Override
    public javax.persistence.criteria.Order setOrder(Root<Tag> root, CriteriaQuery<Tag> query, CriteriaBuilder cb) {
        Order order = null;
        if (sortParameter != null && sortParameter.equals(COST)) {
            order = cb.desc(cb.sum(root.join(GIFT_CERTIFICATES).join(ORDER_CERTIFICATE).get(FIXED_PRICE)));
        } else if (sortParameter != null && sortParameter.equals(COST_DESC)) {
            order = cb.asc(cb.sum(root.join(GIFT_CERTIFICATES).join(ORDER_CERTIFICATE).get(FIXED_PRICE)));
        } else if (sortParameter != null && sortParameter.equals(ORDERS_NUM)) {
            order = cb.desc(cb.count(root.join(GIFT_CERTIFICATES).join(ORDER_CERTIFICATE).get(FIXED_PRICE)));
        } else if (sortParameter != null && sortParameter.equals(ORDERS_NUM_DESC)) {
            order = cb.asc(cb.count(root.join(GIFT_CERTIFICATES).join(ORDER_CERTIFICATE).get(FIXED_PRICE)));
        } else if (sortParameter != null) {
           order = sortParameter.startsWith("-") ? cb.desc(root.get(sortParameter.replaceFirst("-", ""))) :
                    cb.asc(root.get(sortParameter));
        }
        return order;
    }
}
