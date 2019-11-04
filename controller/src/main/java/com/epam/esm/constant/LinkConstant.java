package com.epam.esm.constant;


public class LinkConstant {

    private LinkConstant() {
    }


    public static final String TEMPLATE = "{segment}{?page}{?size}";
    public static final String SEGMENT = "segment";
    public static final String PAGE_TEMPLATE = "page";
    public static final String SIZE_TEMPLATE = "size";
    public static final String PAGE_LAST = "last page";
    public static final String PAGE_FIRST = "first page";


    public static final String CREATE_REL = "create";
    public static final String PUT_REL = "update";
    public static final String PATCH_REL = "partial update";
    public static final String DELETE_REL = "delete";
    public static final String PAGINATION_REL = "pages";

    public static final String GET_METHOD = "GET";
    public static final String PUT_METHOD = "PUT";
    public static final String PATCH_METHOD = "PATCH";
    public static final String DELETE_METHOD = "DELETE";
    public static final String POST_METHOD = "POST";
}
