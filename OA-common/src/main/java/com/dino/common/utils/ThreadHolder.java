package com.dino.common.utils;


import com.dino.common.entity.User;


/**
 * @author Elm Forest
 */
public class ThreadHolder {

    /**
     * 保存用户对象的ThreadLocal
     */
    private static final ThreadLocal<User> THREAD_LOCAL = new ThreadLocal<>();

    private static final ThreadLocal<User> THREAD_LOCAL_TEMP = new ThreadLocal<>();

    /**
     * 添加当前登录用户方法
     */
    public static void addCurrentUser(User user) {
        THREAD_LOCAL.set(user);
    }

    public static User getCurrentUser() {
        return THREAD_LOCAL.get();
    }

    public static void addCurrentUserTmp(User user) {
        THREAD_LOCAL_TEMP.set(user);
    }

    public static User getCurrentUserTmp() {
        return THREAD_LOCAL_TEMP.get();
    }


    /**
     * 防止内存泄漏
     */
    public static void remove() {
        THREAD_LOCAL.remove();
    }

    public static void removeTmp() {
        THREAD_LOCAL_TEMP.remove();
    }

}
