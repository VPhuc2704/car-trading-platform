package org.cartradingplatform.config;

public class EndpointAPI {
    public static final String[] PUBLIC_API_ENDPOINTS = {
            "/api/auth/login",
            "/api/auth/register",
            "/api/auth/logout",
            "/api/vnpayment/return/**",
            "/api/public/posts/**",
            "/api/reviews/seller/{sellerId}",
            "/post/img/**"
    };

    public  static final String[] PRIVATE_ENDPOINTS = {
            "/api/users/*",
            "/api/account/me/*",
            "/api/reset/password",
            "/api/reviews/*",
            "/api/reports/**",
            "/api/favorites/**"
    };

    public  static final String[] MANAGEMENT_API_ENDPOINTS  = {
            "/api/admin/users/**",
            "/api/admin/posts/**",
            "/api/admin/account/**",
            "/api/admin/reports/**"
    };

    public static final String[] SELLER_API_ENDPOINTS = {
            "/api/seller/posts/**",
            "/api/reports/about-me"
    };

}
