package com.dino.common.utils;

import com.dino.common.exception.BizException;

/**
 * 自定义断言
 *
 * @author Zhang Jinming
 * @date 2/8/2023 3:36 PM
 */
public class Assert {

    public static void isTrue(Boolean flag, Exception e) {
        if (!flag) {
            Assert.throwException(e);
        }
    }

    public static void greaterThanZero(int num, Exception e) {
        if (num <= 0) {
            Assert.throwException(e);
        }
    }

    public static void stringNotEmpty(String str, Exception e) {
        if (str == null || "".equals(str)) {
            Assert.throwException(e);
        }
    }

    public static void objNotNull(Object obj, Exception e) {
        try {
            cn.hutool.core.lang.Assert.notNull(obj);
        } catch (IllegalArgumentException ignored) {
            Assert.throwException(e);
        }
    }

    private static void throwException(Exception e) {
        if (e instanceof BizException) {
            throw (BizException) e;
        } else if (e instanceof RuntimeException) {
            throw (RuntimeException) e;
        } else {
            throw new RuntimeException(e.getMessage());
        }
    }

}
