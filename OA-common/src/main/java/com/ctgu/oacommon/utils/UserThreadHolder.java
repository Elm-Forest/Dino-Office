package com.ctgu.oacommon.utils;


import com.ctgu.oacommon.entity.User;

/**
 * @author zjm
 */
public class UserThreadHolder {

    /**
     * 保存用户对象的ThreadLocal
     */
    private static final ThreadLocal<User> USER_THREAD_LOCAL = new ThreadLocal<>();

    /**
     * 添加当前登录用户方法
     */
    public static void addCurrentUser(User user) {
        USER_THREAD_LOCAL.set(user);
    }

    public static User getCurrentUser() {
        return USER_THREAD_LOCAL.get();
    }


    /**
     * 防止内存泄漏
     */
    public static void remove() {
        USER_THREAD_LOCAL.remove();
    }

}
