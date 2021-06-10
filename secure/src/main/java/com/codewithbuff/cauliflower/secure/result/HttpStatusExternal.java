package com.codewithbuff.cauliflower.secure.result;

/**
 * @author 十三月之夜
 */
public enum HttpStatusExternal {

    // 令牌异常系列

    /**
     * 签发日期异常
     */
    TOKEN_SIGN_DATE_ERROR(461, Series.CLIENT_ERROR, "Token Sign Date Error"),

    /**
     * 已过期
     */
    TOKEN_EXPIRED(462, Series.CLIENT_ERROR, "Token Expired"),

    /**
     * 签名失配
     */
    TOKEN_SIGNATURE_FAILED(463, Series.CLIENT_ERROR, "Token Signature Failed"),

    /**
     * 声明失配
     */
    TOKEN_CLAIM_MATCH_FAILED(464, Series.CLIENT_ERROR, "Token Claim Match Failed"),

    /**
     * 算法失配
     */
    TOKEN_ALGORITHM_FAILED(465, Series.CLIENT_ERROR, "Token Algorithm Failed"),

    /**
     * 签发者失配
     */
    TOKEN_ISSUER_FAILED(466, Series.CLIENT_ERROR, "Token Issuer Failed"),

    /**
     * 受者失配
     */
    TOKEN_AUDIENCE_FAILED(467, Series.CLIENT_ERROR, "Token Audience Failed"),

    /**
     * 用户不存在
     */
    TOKEN_USER_NOT_EXIST(468, Series.CLIENT_ERROR, "Token User Not Exist"),

    /**
     * 强制下线
     */
    TOKEN_EXCLUDED(469, Series.CLIENT_ERROR, "User Forced Log Out"),

    // 用户异常系列

    /**
     * 用户未注册
     */
    USER_NOT_FOUND(470, Series.CLIENT_ERROR, "User Not Found"),

    /**
     * 用户认证失败
     */
    USER_CREDENTIAL_FAILED(471, Series.CLIENT_ERROR, "User Credential Failed"),

    /**
     * 请求参数缺失
     */
    CLIENT_PARAMETER_MISSING(472, Series.CLIENT_ERROR, "Client Parameter Missing");


    private final int value;

    private final Series series;

    private final String reasonPhrase;

    HttpStatusExternal(int value, Series series, String reasonPhrase) {
        this.value = value;
        this.series = series;
        this.reasonPhrase = reasonPhrase;
    }


    /**
     * Return the integer value of this status code.
     */
    public int value() {
        return this.value;
    }

    /**
     * Return the HTTP status series of this status code.
     * @see org.springframework.http.HttpStatus.Series
     */
    public Series series() {
        return this.series;
    }

    /**
     * Return the reason phrase of this status code.
     */
    public String getReasonPhrase() {
        return this.reasonPhrase;
    }

    /**
     * Whether this status code is in the HTTP series
     */
    public boolean is1xxInformational() {
        return (series() == Series.INFORMATIONAL);
    }

    /**
     * Whether this status code is in the HTTP series
     */
    public boolean is2xxSuccessful() {
        return (series() == Series.SUCCESSFUL);
    }

    /**
     * Whether this status code is in the HTTP series
     */
    public boolean is3xxRedirection() {
        return (series() == Series.REDIRECTION);
    }

    /**
     * Whether this status code is in the HTTP series
     */
    public boolean is4xxClientError() {
        return (series() == Series.CLIENT_ERROR);
    }

    /**
     * Whether this status code is in the HTTP series
     */
    public boolean is5xxServerError() {
        return (series() == Series.SERVER_ERROR);
    }

    /**
     * Whether this status code is in the HTTP series
     */
    public boolean isError() {
        return (is4xxClientError() || is5xxServerError());
    }

    /**
     * Return a string representation of this status code.
     */
    @Override
    public String toString() {
        return this.value + " " + name();
    }


    /**
     * Return the {@code HttpStatus} enum constant with the specified numeric value.
     */
    public static HttpStatusExternal valueOf(int statusCode) {
        HttpStatusExternal status = resolve(statusCode);
        if (status == null) {
            throw new IllegalArgumentException("No matching constant for [" + statusCode + "]");
        }
        return status;
    }

    /**
     * Resolve the given status code to an {@code HttpStatus}, if possible.
     */
    public static HttpStatusExternal resolve(int statusCode) {
        for (HttpStatusExternal status : values()) {
            if (status.value == statusCode) {
                return status;
            }
        }
        return null;
    }


    /**
     * Enumeration of HTTP status series.
     */
    public enum Series {

        INFORMATIONAL(1),
        SUCCESSFUL(2),
        REDIRECTION(3),
        CLIENT_ERROR(4),
        SERVER_ERROR(5);

        private final int value;

        Series(int value) {
            this.value = value;
        }

        /**
         * Return the integer value of this status series. Ranges from 1 to 5.
         */
        public int value() {
            return this.value;
        }

        /**
         * Return the {@code Series} enum constant for the supplied status code.
         * @param statusCode the HTTP status code (potentially non-standard)
         * @return the {@code Series} enum constant for the supplied status code
         * @throws IllegalArgumentException if this enum has no corresponding constant
         */
        public static Series valueOf(int statusCode) {
            Series series = resolve(statusCode);
            if (series == null) {
                throw new IllegalArgumentException("No matching constant for [" + statusCode + "]");
            }
            return series;
        }

        /**
         * Resolve the given status code to an {@code HttpStatus.Series}, if possible.
         */
        public static Series resolve(int statusCode) {
            int seriesCode = statusCode / 100;
            for (Series series : values()) {
                if (series.value == seriesCode) {
                    return series;
                }
            }
            return null;
        }
    }
}
