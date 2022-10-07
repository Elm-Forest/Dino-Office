package com.ctgu.oacommon.utils;

import java.util.UUID;

/**
 * @author 李嘉超
 * @version 1.0
 * @date 2022/6/11 15:52
 */
public class UUIDUtils {
    /**
     * 回去uuid的值
     *
     * @return
     */
    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
