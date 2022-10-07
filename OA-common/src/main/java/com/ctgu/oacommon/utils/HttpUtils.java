package com.ctgu.oacommon.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

/**
 * @author Mr.zhou
 * @date 2021/3/27
 * &#064;description  http请求发送工具类
 */
public class HttpUtils {
    /**
     * 向指定URL发送GET方法的请求
     *
     * @param url   发送请求的URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL 所代表远程资源的响应结果
     */
    public static String doGetHttp(String url, String param) {
        StringBuilder result = new StringBuilder();
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
                result.append(line);
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result.toString();
    }

    /**
     * 发送json数据post请求
     * @param url 请求地址
     * @param jsonStr 请求json格式字符串数据
     * @return 响应结果
     */
    public static String doPostHttp(String url, String jsonStr) {
        System.out.println("发起的数据:" + jsonStr);
        byte[] xmlData = jsonStr.getBytes();
        BufferedReader in = null;
        StringBuilder responseString = new StringBuilder();
        try {
            URL doUrl = new URL(url);
            URLConnection urlCon = doUrl.openConnection();
            urlCon.setDoOutput(true);
            urlCon.setDoInput(true);
            urlCon.setUseCaches(false);
            urlCon.setRequestProperty("content-Type", "application/json");
            urlCon.setRequestProperty("charset", "utf-8");
            urlCon.setRequestProperty("Content-length",
                    String.valueOf(xmlData.length));
            System.out.println(xmlData.length);
            DataOutputStream printout = new DataOutputStream(
                    urlCon.getOutputStream());
            printout.write(xmlData);
            printout.flush();
            printout.close();
            in = new BufferedReader(new InputStreamReader(urlCon.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                responseString.append(line);
            }

            if ("".equals(responseString.toString().trim())) {
                System.out.println("返回空");
            }
            System.out.println("返回数据为:" + responseString);
            return responseString.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return "0";
        } finally {
            try {
                assert in != null;
                in.close();
            } catch (Exception ex) {
                return "0";
            }
        }

    }
}

