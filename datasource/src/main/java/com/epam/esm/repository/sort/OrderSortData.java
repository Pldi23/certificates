package com.epam.esm.repository.sort;

import com.epam.esm.entity.Order;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import static com.epam.esm.repository.constant.JpaConstant.FIXED_PRICE;
import static com.epam.esm.repository.constant.JpaConstant.ID;
import static com.epam.esm.repository.constant.JpaConstant.ORDER_CERTIFICATE;
import static com.epam.esm.repository.constant.JpaConstant.PRICE;

/**
 * implementation of {@link Sortable} for {@link Order}
 *
 * @author Dzmitry Platonov on 2019-10-24.
 * @version 0.0.1
 */
public class OrderSortData implements Sortable<Order> {

    private String sortParameter;

    public OrderSortData(String sortParameter) {
        this.sortParameter = sortParameter;
    }

    @Override
    public javax.persistence.criteria.Order setOrder(Root<Order> root, CriteriaQuery<Order> query, CriteriaBuilder cb) {
        javax.persistence.criteria.Order order = null;
        if (sortParameter != null && sortParameter.contains(PRICE)) {
            order = sortParameter.startsWith("-") ?
                    cb.asc(cb.sum(root.join(ORDER_CERTIFICATE).get(buildSortParameter(sortParameter.replaceFirst("-", ""))))) :
                    cb.desc(cb.sum(root.join(ORDER_CERTIFICATE).get(buildSortParameter(sortParameter))));
        }
        if (sortParameter != null && sortParameter.contains(ID)) {
            order = sortParameter.startsWith("-") ?
                    cb.asc(root.get(ID)) : cb.desc(root.get(ID));
        }
        return order;
    }

    private String buildSortParameter(String sortParameter) {
        return sortParameter.replaceFirst(PRICE, FIXED_PRICE);
    }
}
