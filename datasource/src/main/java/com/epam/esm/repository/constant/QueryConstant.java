package com.epam.esm.repository.constant;


public class QueryConstant {

    private QueryConstant() {
    }

    public static final String MOST_COST_EFFECTIVE_TAG_BY_USER =
            "select tag.id, tag.title "+
            "from tag "+
            "where tag.id in (select tag_id "+
            "from certificate_tag ct "+
            "where ct.certificate_id = (select certificate_id "+
            "from ((select id "+
            "from application_order "+
            "where application_order.user_id = ?) as app_or "+
            "left join order_certificate "+
            "on app_or.id = order_certificate.order_id) as s "+
            "group by certificate_id, order_id "+
            "order by sum(s.fixed_price) desc "+
            "limit 1))";

    public static final String DELETE_UNLINKED_TAGS =
            "delete from tag where tag.id not in (select tag_id from certificate_tag where certificate_id is not null)";


    public static final String CALCULATE_ORDER_FIXED_PRICE =
            "select sum(fixed_price) from order_certificate where order_id = ?";

    public static final String DELETE_ORDER_CERTIFICATE_BY_ORDER_ID =
            "delete from order_certificate where order_id = ?";

    public static final String SELECT_ALL_ORDER_CERTIFICATE_BY_CERTIFICATE_ID =
            "select id, certificate_id, order_id, fixed_price, expiration_date from order_certificate where certificate_id = ?";

    public static final String SELECT_ALL_ORDER_CERTIFICATE_BY_ORDER_ID =
            "select id, certificate_id, order_id, fixed_price, expiration_date from order_certificate where order_id = ?";

    public static final String SELECT_ALL_ORDER_CERTIFICATE =
            "select id, certificate_id, order_id, fixed_price, expiration_date from order_certificate";


}
