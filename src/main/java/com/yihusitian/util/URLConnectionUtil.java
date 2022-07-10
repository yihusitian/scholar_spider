package com.yihusitian.util;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Description TODO
 * @Author LeeHo
 * @Date 2022/7/8 19:10
 */
public class URLConnectionUtil {

    public static String getCookie(String url) {
        HttpURLConnection conn = null;
        try {
            URL realUrl = new URL(url);
            conn = (HttpURLConnection) realUrl.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
        } catch (Exception e) {
            e.printStackTrace();
        }
        String sessionId = "";
        String cookieVal = "";
        String key = null;
        //取cookie
        for (int i = 1; (key = conn.getHeaderFieldKey(i)) != null; i++) {
            if (key.equalsIgnoreCase("set-cookie")) {
                cookieVal = conn.getHeaderField(i);
                cookieVal = cookieVal.substring(0, cookieVal.indexOf(";"));
                sessionId = sessionId + cookieVal + ";";
            }
        }
        return sessionId;
    }

    /**
     * 获取 cookie
     *
     * @param url    发送请求的URL
     * @param cookie cookie
     */
    public static List<String> getCookie3(String url, String cookie) {
        HttpURLConnection conn = null;
        try {
            URL realUrl = new URL(url);
            conn = (HttpURLConnection) realUrl.openConnection();
            conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
            conn.setRequestProperty("Accept-Encoding", "gzip, deflate, sdch");
            conn.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
            conn.setRequestProperty("Cache-Control", "max-age=0");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("Cookie", cookie);

            conn.setInstanceFollowRedirects(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("GET");
        } catch (Exception e) {
            e.printStackTrace();
        }
        String sessionId = "";
        String cookieVal = "";
        String key = null;
        for (int i = 1; (key = conn.getHeaderFieldKey(i)) != null; i++) {
            if (key.equalsIgnoreCase("set-cookie")) {
                cookieVal = conn.getHeaderField(i);
                cookieVal = cookieVal.substring(0, cookieVal.indexOf(";"));
                sessionId = sessionId + cookieVal + ";";
            }
        }
        List<String> list = new ArrayList<String>();
        list.add(sessionId);//存放cookie
        return list;
    }

    /**
     * 获取重定向location和cookie信息
     *
     * @param url
     * @param headers
     * @return
     * @throws Exception
     */
    public static String[] getRedictLocationAndCookie(String url, Map<String, String> headers) throws Exception {
        URL realUrl = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
        HttpURLConnection finalConn = conn;
        if (MapUtil.isNotEmpty(headers)) {
            headers.forEach((key, value) -> finalConn.setRequestProperty(key, value));
        }
        conn.setInstanceFollowRedirects(false);
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setRequestMethod("GET");
        String cookieVal = "";
        String sessionId = "";
        String key;
        Map<String, List<String>> map = conn.getHeaderFields();
        for (String key1 : map.keySet()) {
            System.out.println(key1 + "--->" + map.get(key1));
        }
        //取cookie
        for (int i = 1; (key = conn.getHeaderFieldKey(i)) != null; i++) {
            if (key.equalsIgnoreCase("set-cookie")) {
                cookieVal = conn.getHeaderField(i);
                cookieVal = cookieVal.substring(0, cookieVal.indexOf(";"));
                sessionId = sessionId + cookieVal + ";";
            }
        }
        String location = conn.getHeaderField("Location");
        if (StrUtil.isEmpty(location)) {
            location = url;
        }
        return new String[] {location, sessionId};
    }

    /**
     * 获取cookie
     *
     * @param url 发送请求的URL
     * @return key=value;key=value;...
     */
    public static String getCookie(String url, Map<String, String> headers) throws Exception {
        HttpURLConnection conn = null;
        try {
            URL realUrl = new URL(url);
            conn = (HttpURLConnection) realUrl.openConnection();
            HttpURLConnection finalConn = conn;
            headers.forEach((key, value) -> finalConn.setRequestProperty(key, value));
            //是否自动执行 http 重定向，默认为true
            //如果实际操作中，不存在重定向问题，不需要设置此行。
            conn.setInstanceFollowRedirects(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("GET");
        } catch (Exception e) {
            e.printStackTrace();
        }
        String sessionId = "";
        String cookieVal = "";
        String key = null;

        Map<String, List<String>> map = conn.getHeaderFields();
        for (String key1 : map.keySet()) {
            System.out.println(key1 + "--->" + map.get(key1));
        }
        //取cookie
        for (int i = 1; (key = conn.getHeaderFieldKey(i)) != null; i++) {
            if (key.equalsIgnoreCase("set-cookie")) {
                cookieVal = conn.getHeaderField(i);
                cookieVal = cookieVal.substring(0, cookieVal.indexOf(";"));
                sessionId = sessionId + cookieVal + ";";
            }
        }
        //如果实际操作中，不存在重定向问题，不需要以下四行
        String location = conn.getHeaderField("Location");//获取 重定向地址
        List<String> list = getCookie3(location, sessionId);
        List<String> list2 = getCookie3(list.get(1), sessionId + list.get(0));
        sessionId = sessionId + list2.get(0);
        return sessionId;
    }

}