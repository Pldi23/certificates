package com.epam.esm.repository.constant;

/**
 * gift-certificates
 *
 * @author Dzmitry Platonov on 2019-10-20.
 * @version 0.0.1
 */
public class QueryConstant {

    private QueryConstant() {
    }

    public static final String MOST_COST_EFFECTIVE_TAG_BY_USER = "select tag.id, tag.title from tag " +
            "join certificate_tag on tag.id = tag_id "  +
            "join certificate c on certificate_tag.certificate_id = c.id " +
            "join order_certificate oc on c.id = oc.certificate_id " +
            "join application_order ao on oc.order_id = ao.id " +
            "where ao.user_id = ? " +
            "group by tag.id, tag.title " +
            "order by sum(c.price) desc limit 1";

    public static final String CALCULATE_ORDER_FIXED_PRICE =
            "select sum(fixed_price) from order_certificate where order_id = ?";

    public static final String DELETE_ORDER_CERTIFICATE_BY_ORDER_ID =
            "delete from order_certificate where order_id = ?";

    public static final String SELECT_ALL_ORDER_CERTIFICATE_BY_CERTIFICATE_ID =
            "select * from order_certificate where certificate_id = ?";

    public static final String SELECT_ALL_ORDER_CERTIFICATE_BY_ORDER_ID =
            "select * from order_certificate where order_id = ?";

    public static final String SELECT_ALL_ORDER_CERTIFICATE =
            "select * from order_certificate";


}
