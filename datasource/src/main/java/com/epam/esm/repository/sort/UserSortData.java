package com.epam.esm.repository.sort;

import com.epam.esm.entity.User;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;

import static com.epam.esm.repository.constant.JpaConstant.ID;
import static com.epam.esm.repository.constant.JpaConstant.NUM_ORDERS_ASC_SORT_PARAMETER;
import static com.epam.esm.repository.constant.JpaConstant.NUM_ORDERS_DESC_SORT_PARAMETER;
import static com.epam.esm.repository.constant.JpaConstant.ORDERS;

/**
 * implementation of {@link Sortable} for User
 *
 * @author Dzmitry Platonov on 2019-10-24.
 * @version 0.0.1
 */
public class UserSortData implements Sortable<User> {

    private String sortParameter;

    public UserSortData(String sortParameter) {
        this.sortParameter = sortParameter;
    }

    @Override
    public Order setOrder(Root<User> root, CriteriaQuery<User> query, CriteriaBuilder cb) {
        Order order = null;
        if (sortParameter != null && (sortParameter.equals(NUM_ORDERS_DESC_SORT_PARAMETER) || sortParameter.equals(NUM_ORDERS_ASC_SORT_PARAMETER))) {
           order = sortParameter.startsWith("-") ?
                            cb.asc(cb.countDistinct(root.join(ORDERS).get(ID))) :
                            cb.desc(cb.countDistinct(root.join(ORDERS).get(ID)));
        } else if (sortParameter != null) {
            order = sortParameter.startsWith("-") ?
                    cb.asc(root.get(sortParameter.replaceFirst("-", ""))) :
                    cb.desc(root.get(sortParameter));
        }
        return order;
    }
}
