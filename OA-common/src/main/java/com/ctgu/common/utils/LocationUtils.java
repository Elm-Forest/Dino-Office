package com.ctgu.common.utils;


import cn.hutool.core.lang.Dict;
import cn.hutool.http.HttpUtil;

/**
 * @author Zhang Jinming
 * @create 25/6/2022 上午12:27
 */
public class LocationUtils {
    public static String getLocation(String location) {
        String requestUrl = "https://apis.map.qq.com/ws/place/v1/suggestion";
        String key = "KLMBZ-SH2WI-4WSGI-5KHVB-Y22Y5-RJFMF";
        return HttpUtil.get(requestUrl, Dict.create()
                .set("keyword", location)
                .set("key", key));
    }
}
