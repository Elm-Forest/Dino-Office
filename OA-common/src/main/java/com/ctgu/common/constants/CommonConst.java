package com.ctgu.common.constants;

/**
 * @author Zhang Jinming
 * @create 10/6/2022 上午9:05
 */
public class CommonConst {
    /**
     * 用户激活状态
     */
    public static final int ACTIVATED = 1;
    public static final int UNACTIVATED = -1;
    /**
     * 用户签到
     */
    public static final int CHECK_IN = 1;
    public static final int CHECK_OUT = 0;
    public static final int SUPER_ADMIN = 2;
    public static final int ADMIN = 1;
    /**
     * 员工状态
     * 0:未入职;
     * 1:在职;
     * 2:离职
     */
    public static final int NO_ENTRY = 0;
    public static final int STAFF_ONLINE = 1;
    public static final int STAFF_OFFLINE = 2;

}
