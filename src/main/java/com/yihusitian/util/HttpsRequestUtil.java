package com.yihusitian.util;

import cn.hutool.core.map.MapUtil;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.Closeable;
import java.util.Map;
import java.util.Objects;

/**
 * @Description TODO
 * @Author LeeHo
 * @Date 2022/7/8 17:12
 */
public class HttpsRequestUtil {

    /**
     * https请求
     *
     * @param url
     * @param headers
     * @return
     */
    public static String doGet(String url, Map<String, String> headers) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse httpResponse = null;
        String result = null;
        try {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, (TrustStrategy) (chain, authType) -> true).build();
            SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
            httpClient = HttpClients.custom().setSSLSocketFactory(sslConnectionSocketFactory)
                    .build();
            HttpGet httpGet = new HttpGet(url);
            if (MapUtil.isNotEmpty(headers)) {
                headers.forEach((key, value) -> httpGet.addHeader(key, value));
            }
            System.out.println("请求开始, url: " + url);
            httpResponse = httpClient.execute(httpGet);
            System.out.println("请求完成, url: " + url);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            System.out.println("请求完成, url: " + url + ", statusCode: " + statusCode);
            if (statusCode == HttpStatus.SC_OK) {
                result = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
                System.out.println("请求完成, result: " + result);
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("请求异常, url: " + url);
        } finally {
            closeResource(httpClient, httpResponse);
        }
        return result;
    }

    private static void closeResource(Closeable...closeables) {
        if (Objects.isNull(closeables)) {
            return;
        }
        try {
            for (Closeable closeable : closeables) {
                if (Objects.nonNull(closeable)) {
                    closeable.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}