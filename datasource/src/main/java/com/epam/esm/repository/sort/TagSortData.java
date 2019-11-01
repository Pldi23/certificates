package com.epam.esm.repository.sort;

import com.epam.esm.entity.Tag;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;

import static com.epam.esm.repository.constant.JpaConstant.COST;
import static com.epam.esm.repository.constant.JpaConstant.COST_DESC;
import static com.epam.esm.repository.constant.JpaConstant.FIXED_PRICE;
import static com.epam.esm.repository.constant.JpaConstant.GIFT_CERTIFICATES;
import static com.epam.esm.repository.constant.JpaConstant.ORDERS_NUM;
import static com.epam.esm.repository.constant.JpaConstant.ORDERS_NUM_DESC;
import static com.epam.esm.repository.constant.JpaConstant.ORDER_CERTIFICATE;

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
            order = cb.desc(cb.sum(root.join(GIFT_CERTIFICATES).join(ORDER_CERTIFICATE, JoinType.LEFT).get(FIXED_PRICE)));
        } else if (sortParameter != null && sortParameter.equals(COST_DESC)) {
            order = cb.asc(cb.sum(root.join(GIFT_CERTIFICATES).join(ORDER_CERTIFICATE, JoinType.LEFT).get(FIXED_PRICE)));
        } else if (sortParameter != null && sortParameter.equals(ORDERS_NUM)) {
            order = cb.desc(cb.count(root.join(GIFT_CERTIFICATES).join(ORDER_CERTIFICATE, JoinType.LEFT).get(FIXED_PRICE)));
        } else if (sortParameter != null && sortParameter.equals(ORDERS_NUM_DESC)) {
            order = cb.asc(cb.count(root.join(GIFT_CERTIFICATES).join(ORDER_CERTIFICATE, JoinType.LEFT).get(FIXED_PRICE)));
        } else if (sortParameter != null) {
           order = sortParameter.startsWith("-") ? cb.desc(root.get(sortParameter.replaceFirst("-", ""))) :
                    cb.asc(root.get(sortParameter));
        }
        return order;
    }
}
