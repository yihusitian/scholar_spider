package com.yihusitian.util;

import cn.hutool.http.HttpInterceptor;
import cn.hutool.http.HttpRequest;

import java.util.Map;

/**
 * @Description
 * @Author leeho
 * @Date 2022/6/25 上午11:16
 */
public class HttpRequestUtil {

    /**
     * 构建GET请求信息
     *
     * @param url
     * @return
     */
    public static HttpRequest buildGetHttpRequest(String url) {
        return HttpRequest.get(url).timeout(10);
    }

    /**
     * 构建GET请求信息
     *
     * @param url
     * @param interceptor
     * @return
     */
    public static HttpRequest buildGetHttpRequest(String url, HttpInterceptor<HttpRequest> interceptor) {
        return buildGetHttpRequest(url).addRequestInterceptor(interceptor);
    }

    /**
     * 构建POST请求信息
     *
     * @param url
     * @return
     */
    public static HttpRequest buildPostHttpRequest(String url) {
        return HttpRequest.post(url).timeout(-1);
    }

    /**
     * 构建POST请求信息
     *
     * @param url
     * @param interceptor
     * @return
     */
    public static HttpRequest buildPostHttpRequest(String url, HttpInterceptor<HttpRequest> interceptor) {
        return buildPostHttpRequest(url).addRequestInterceptor(interceptor);
    }

    /**
     * 执行GET请求
     *
     * @param url
     * @param headers
     * @return
     */
    public static String executGetHttpRequest(String url, Map<String, String> headers) {
        String result = null;
        try {
            HttpRequest httpRequest = buildGetHttpRequest(url).addHeaders(headers);
            System.out.println("执行GET请求开始, url: " + url);
            result = httpRequest.execute().body();
            System.out.println("执行GET请求完成, url: " + url);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("执行GET请求异常, url: " + url);
        }
        return result;
    }
}
