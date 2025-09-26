package org.cartradingplatform.config;

public class EndpointAPI {
    public static final String[] PUBLIC_API_ENDPOINTS = {
            "/api/auth/login",
            "/api/auth/register",
            "/api/auth/logout",
    };

    public  static final String[] PRIVATE_ENDPOINTS = {
            "/api/users/*",
            "/api/account/me/*",
    };

    public  static final String[] MANAGEMENT_API_ENDPOINTS  = {
            "/api/admin/users/**",
    };

    public static final String[] SELLER_API_ENDPOINTS = {
            "/api/seller/posts/*",
    };

}
