package com.dino.common.constants;

/**
 * @author Zhang Jinming
 * @create 18/6/2022 下午5:25
 */
public class ShiroConst {
    /**
     * 非对称加密方法
     */
    public static final String RSA_TYPE = "md5";
    /**
     * 默认盐(已弃用)
     */
    public static final String DEFAULT_SALT_KEY = "#$fd$@DF*&^)@_{po@#fda";
    /**
     * 哈希散列次数
     */
    public static final int HASH_ITERATIONS = 1024;
    public static final String USER_ROLE = "user";

    public static final String ADMIN_ROLE = "admin";
}
