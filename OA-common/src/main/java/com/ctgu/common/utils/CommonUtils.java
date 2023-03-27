package com.ctgu.common.utils;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 公共工具类
 *
 * @author CTGU_LLZ(404name)
 * @date 2021/07/28
 */
public class CommonUtils {

    /**
     * 检测邮箱是否合法
     *
     * @param email 邮箱
     * @return 合法状态
     */
    public static boolean isUnValidEmail(String email) {
        String rule = "^\\w+((-\\w+)|(\\.\\w+))*@[A-Za-z0-9]+(([.\\-])[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$";
        //正则表达式的模式 编译正则表达式
        Pattern p = Pattern.compile(rule);
        //正则表达式的匹配器
        Matcher m = p.matcher(email);
        //进行正则匹配
        return !m.matches();
    }

    /**
     * 生成6位随机验证码
     *
     * @return 验证码
     */
    public static String getRandomCode() {
        StringBuilder codeNum = new StringBuilder();
        int codeLength = 6;
        int[] code = new int[3];
        Random random = new Random();
        //自动生成验证码
        for (int i = 0; i < codeLength; i++) {
            int num = random.nextInt(10) + 48;
            int uppercase = random.nextInt(26) + 65;
            int lowercase = random.nextInt(26) + 97;
            code[0] = num;
            code[1] = uppercase;
            code[2] = lowercase;
            codeNum.append((char) code[random.nextInt(3)]);
        }
        return codeNum.toString();
    }

    /**
     * 日期偏移至下一日零点
     *
     * @param endTime 查找结束时间
     * @return 新日期
     */
    public static String dateMoveOneDay(String endTime) {
        if (!StrUtil.hasEmpty(endTime)) {
            DateTime date = DateUtil.parse(endTime, "yyyy-MM-dd");
            DateTime newDate = DateUtil.offsetDay(date, 1);
            endTime = newDate.toString();
        }
        return endTime;
    }
}
