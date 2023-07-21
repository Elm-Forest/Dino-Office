package com.dino.common.constants;

import java.util.Calendar;

/**
 * @author Zhang Jinming
 * @create 17/6/2022 下午12:12
 */
public class JwtConst {
    /**
     * JWT私钥
     */
    public static final String PRIVATE_KEY = "gd#!@daf$%a&*(*(&";
    /**
     * 时间单位
     */
    public static final int CALENDAR_FIELD = Calendar.DATE;
    /**
     * 到期时间
     */
    public static final int CALENDAR_INTERVAL = 10;
    /**
     * K-V字段中的K
     */
    public static final String JWT_NAME = "token";

    public static final int ADMIN_ROLE = 8711534;

    public static final int USER_ROLE = 4264424;

}
