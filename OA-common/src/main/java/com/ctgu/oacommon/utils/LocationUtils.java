package com.ctgu.oacommon.utils;


import static com.ctgu.oacommon.utils.HttpUtils.doGetHttp;

/**
 * @author Zhang Jinming
 * @create 25/6/2022 上午12:27
 */
public class LocationUtils {
    public static String getLoaction(String location) {
        return doGetHttp("https://apis.map.qq.com/ws/place/v1/suggestion","keyword="+
                location + "&key=KLMBZ-SH2WI-4WSGI-5KHVB-Y22Y5-RJFMF");
    }
}
