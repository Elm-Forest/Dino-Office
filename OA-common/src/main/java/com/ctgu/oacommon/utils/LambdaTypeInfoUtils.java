package com.ctgu.oacommon.utils;


import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 原始使用方法： LambdaTypeInfoUtils.convertToFieldName(Test::getName)
 * 优化后的使用方法: getObjectKey(Test::getName)
 *
 * @author Zhang Jinming
 * @date 2022.6.21
 */
public class LambdaTypeInfoUtils {

    private static final Map<Class<?>, SerializedLambda> CLASS_LAMBDA_CACHE = new ConcurrentHashMap<>();

    /***
     * 转换方法引用为属性名
     */
    static <T, R> String convertToFieldName(SFunction<T, R> fn) {
        SerializedLambda lambda = getSerializedLambda(fn);
        // 获取方法名
        String methodName = lambda.getImplMethodName();
        String prefix = null;
        if (methodName.startsWith("get")) {
            prefix = "get";
        } else if (methodName.startsWith("is")) {
            prefix = "is";
        }
        if (prefix == null) {
            System.out.println("无效的getter方法: " + methodName);
        }
        // 截取get/is之后的字符串并转换首字母为小写
        assert prefix != null;
        return toLowerCaseFirstOne(methodName.replace(prefix, ""));
    }

    /**
     * 首字母转小写
     */
    static String toLowerCaseFirstOne(String s) {
        if (Character.isLowerCase(s.charAt(0))) {
            return s;
        } else {
            return Character.toLowerCase(s.charAt(0)) + s.substring(1);
        }
    }

    /**
     * 关键在于这个方法
     */
    static SerializedLambda getSerializedLambda(Serializable fn) {
        SerializedLambda lambda = CLASS_LAMBDA_CACHE.get(fn.getClass());
        // 先检查缓存中是否已存在
        if (lambda == null) {
            try {
                // 提取SerializedLambda并缓存
                Method method = fn.getClass().getDeclaredMethod("writeReplace");
                method.setAccessible(Boolean.TRUE);
                lambda = (SerializedLambda) method.invoke(fn);
                CLASS_LAMBDA_CACHE.put(fn.getClass(), lambda);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return lambda;
    }

    /**
     * 调用入口方法
     */
    public static <T, R> String getObjectKey(SFunction<T, R> fn) {
        return LambdaTypeInfoUtils.convertToFieldName(fn);
    }
}

