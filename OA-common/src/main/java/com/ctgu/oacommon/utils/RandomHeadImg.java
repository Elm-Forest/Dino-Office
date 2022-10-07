package com.ctgu.oacommon.utils;

import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

/**
 * @author Zhang Jinming
 * @create 23/6/2022 下午7:45
 */
public class RandomHeadImg {
    public static String randomHeadImg() {
        try {
            String urlNameString = "http://api.btstu.cn/sjtx/api.php";
            URLConnection connection = new URL(urlNameString).openConnection();
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            connection.connect();
            Map<String, List<String>> map = connection.getHeaderFields();
            return map.get("Location").get(0);
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        return "https://pic2.zhimg.com/v2-c1abdf0c1a579e5c67a4732923b112a7_1440w.jpg?source=172ae18b";
    }
}
