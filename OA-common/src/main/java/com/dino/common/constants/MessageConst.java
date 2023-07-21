package com.dino.common.constants;

import cn.hutool.core.date.DateUtil;

import java.util.Date;

/**
 * @author 李嘉超
 * @version 1.0
 * @date 2022/8/16 17:39
 */
public class MessageConst {
    //消息是否发送
    public static final int NOT_RELEASE = 0;
    public static final int HAVE_RELEASE = 1;

    //消息是否过期
    public static final int NOT_OVERDUE = 0;
    public static final int HAVE_OVERDUE = 1;

    //最大时间和最小时间
    public static final Date MAX_DATE = DateUtil.parse("2100-00-01");
    public static final Date MIN_DATE = DateUtil.parse("2000-00-01");
}
