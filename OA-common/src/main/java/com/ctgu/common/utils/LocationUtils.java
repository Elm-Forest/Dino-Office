package com.ctgu.common.utils;


/**
 * @author Zhang Jinming
 * @create 25/6/2022 上午12:27
 */
public class LocationUtils {
    public static String getLocation(String location) {
        String requestUrl = "https://apis.map.qq.com/ws/place/v1/suggestion";
        // 申请的key
        String key = "xxx";
//        String place = HttpUtil.get(requestUrl, Dict.create()
//                .set("keyword", location)
//                .set("key", key));
        return "开发者请先配置地图api服务，见LocationUtils工具类";
    }
}
