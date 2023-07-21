package com.dino.common.constants;

/**
 * redis常量
 *
 * @author CTGU_LLZ(404name)
 * @date 2021/07/27
 */
public class RedisPrefixConst {

    /**
     * 验证码过期时间
     */
    public static final long CODE_EXPIRE_TIME = 15 * 60;

    /**
     * 验证码
     */
    public static final String USER_CODE_KEY = "code:";

    public static final String USER_AUTH_KEY = "user_auth:";
    public static final String USER_INFO_KEY = "user_info:";
    public static final String TOKEN_KEY = "token:";

    public static final String NOTIFICATION = "notification:";
}
