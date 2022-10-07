package com.ctgu.oacommon.utils;


import com.ctgu.oacommon.vo.Result;

/**
 * @author Zhang Jinming
 * @create 10/6/2022 下午5:28
 */
public class MessageUtils {
    public static Result<?> updateMessage(int insert, String failureMsg) {
        return insert > 0 ? Result.ok() : Result.fail(failureMsg);
    }
}
