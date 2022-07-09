package com.yihusitian.abstracts;

import cn.hutool.core.util.StrUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @Description TODO
 * @Author LeeHo
 * @Date 2022/7/8 14:16
 */
public class LinkSpringerAbstractAdaptor extends AbstractAdaptor {

    private static final String SITE_URL = "link.springer.com";

    private static final Map<String, String> HEADERS = new HashMap<String, String>() {{
        this.put("authority", "link.springer.com");
        this.put("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        this.put("accept-language", "zh-CN,zh;q=0.9,en;q=0.8");
        this.put("cache-control", "max-age=0");
        this.put("cookie", "sim-inst-token=\"\"; trackid=\"2vssjtfklhtrcdypheglqc5ei\"; idp_session=sVERSION_157f4563b-ecb9-455b-8edd-c6f22adf802d; idp_session_http=hVERSION_1b3a75a23-e5db-40a5-9bad-94cca41693c8; idp_marker=e2d1e1a7-16d4-4506-a41b-5a3213a3b721; sncc=P=17:V=13.0.0&C=C01");
        this.put("if-none-match", "98a15a1d58beaf1f15342bab675fc65a");
        this.put("sec-ch-ua", "\".Not / A) Brand \";v=\" 99 \", \" Google Chrome \";v=\" 103 \", \" Chromium \";v=\" 103 \"");
        this.put("sec-ch-ua-mobile", "?0");
        this.put("sec-ch-ua-platform", "Windows");
        this.put("sec-fetch-dest", "document");
        this.put("sec-fetch-mode", "navigate");
        this.put("sec-fetch-site", "none");
        this.put("sec-fetch-user", "?1");
        this.put("upgrade-insecure-requests", "1");
        this.put("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/103.0.0.0 Safari/537.36");
    }};

    public LinkSpringerAbstractAdaptor(String dirName) {
        super(dirName);
    }

    @Override
    protected String getSiteUrl() {
        return SITE_URL;
    }

    @Override
    protected Map<String, String> getHeaders() {
        return HEADERS;
    }

    @Override
    protected String doParse(String htmlContent) {
        if (StrUtil.isEmpty(htmlContent)) {
            return null;
        }
        Document document = Jsoup.parse(htmlContent);
        Element abstractDivElement = document.getElementById("Abs1-content");
        if (Objects.isNull(abstractDivElement)) {
            return null;
        }
        return abstractDivElement.getElementsByTag("p").first().text();
    }
}