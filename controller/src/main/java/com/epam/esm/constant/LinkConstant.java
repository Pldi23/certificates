package com.epam.esm.constant;


public class LinkConstant {

    private LinkConstant() {
    }

    public static final String USER_LINK_REL = "user";
    public static final String CERTIFICATE_LINK_REL = "gift certificates";
    public static final String TAG_LINK_REL = "tag";
    public static final String ORDER_LINK_REL = "orders";

    public static final String TAG_STATS = "stats";
    public static final String CERTIFICATES = "certificates";

    public static final String TEMPLATE = "{segment}{?page}{?size}";
    public static final String SEGMENT = "segment";
    public static final String PAGE_TEMPLATE = "page";
    public static final String SIZE_TEMPLATE = "size";
    public static final String PAGE_CURRENT = "current";
    public static final String PAGE_LAST = "last page";
    public static final String PAGE_FIRST = "first page";
    public static final String PAGE_NEXT = "next";
    public static final String PAGE_PREVIOUS = "previous";

    public static final String GET_METHOD = "GET";
    public static final String PUT_METHOD = "PUT";
    public static final String PATCH_METHOD = "PATCH";
    public static final String DELETE_METHOD = "DELETE";
    public static final String POST_METHOD = "POST";
    public static final String MEDIA_APPLICATION_JSON = "application/json";
}
