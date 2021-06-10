package com.codewithbuff.cauliflower.secure.system;

import org.springframework.http.HttpHeaders;

/**
 * @author 十三月之夜
 * @time 2021/4/22 12:11 上午
 */
public class SystemConstant {

    public static final String BEARER = "Bearer";

    public static final String AUTHORIZATION = HttpHeaders.AUTHORIZATION;

    public static final String TOKEN_USERNAME = "USERNAME";

    public static final String TOKEN_USER_ID = "USER_ID";

    public static final String TOKEN_USER_ROLES = "USER_ROLES";

    public static final long ACCESS_TOKEN_EXPIRED_TIME = 2 * 60 * 60 * 1000L;

    public static final long REFRESH_TOKEN_EXPIRED_TIME = 7 * 24 * 60 * 60 * 1000L;

    public static final String ISSUER = "CodeWithBuff-Cauliflower";

    public static final String ACCESS_TOKEN = "ACCESS_TOKEN";

    public static final String REFRESH_TOKEN = "REFRESH_TOKEN";

    public static final String IDENTITY_TYPE_EMAIL = "IDENTITY_TYPE_EMAIL";

    public static final String VER_CODE_PREFIX = "VER_CODE_PREFIX_";
}
