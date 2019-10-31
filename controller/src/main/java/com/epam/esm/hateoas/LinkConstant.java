package com.epam.esm.hateoas;


class LinkConstant {

    private LinkConstant() {
    }

    static final String USER_LINK_REL = "user";
    static final String CERTIFICATE_LINK_REL = "gift certificates";
    static final String TAG_LINK_REL = "tag";
    static final String ORDER_LINK_REL = "orders";

    static final String TAG_STATS = "stats";

    static final String TEMPLATE = "{segment}{?page}{?size}";
    static final String SEGMENT = "segment";
    static final String PAGE_TEMPLATE = "page";
    static final String SIZE_TEMPLATE = "size";
    static final String PAGE_CURRENT = "current page";
    static final String PAGE_LAST = "last page";
    static final String PAGE_FIRST = "first page";

    static final String GET_METHOD = "GET";
    static final String PUT_METHOD = "PUT";
    static final String PATCH_METHOD = "PATCH";
    static final String DELETE_METHOD = "DELETE";
    static final String POST_METHOD = "POST";
    static final String MEDIA_APPLICATION_JSON = "application/json";
}
