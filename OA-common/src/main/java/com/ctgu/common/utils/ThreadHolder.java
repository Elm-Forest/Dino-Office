package com.ctgu.common.utils;


import com.ctgu.common.entity.User;


/**
 * @author Elm Forest
 */
public class ThreadHolder {

    /**
     * 保存用户对象的ThreadLocal
     */
    private static final ThreadLocal<User> THREAD_LOCAL = new ThreadLocal<>();

    /**
     * 添加当前登录用户方法
     */
    public static void addCurrentUser(User user) {
        THREAD_LOCAL.set(user);
    }

    public static User getCurrentUser() {
        return THREAD_LOCAL.get();
    }


    /**
     * 防止内存泄漏
     */
    public static void remove() {
        THREAD_LOCAL.remove();
    }

}
