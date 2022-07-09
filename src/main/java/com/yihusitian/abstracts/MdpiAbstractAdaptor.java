package com.yihusitian.abstracts;

import cn.hutool.core.util.StrUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MdpiAbstractAdaptor extends AbstractAdaptor {

    private static final String SITE_URL = "www.mdpi.com";

    //去查header信息, https://www.mdpi.com/1007062
    private static final Map<String, String> HEADERS = new HashMap<String, String>() {{
        this.put("authority","www.mdpi.com");
        this.put("accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        this.put("accept-language","zh-CN,zh;q=0.9,en;q=0.8");
        this.put("cache-control","max-age=0");
        this.put("cookie","device_view=full; __cfruid=2cbc581080183eaffb458a2c9297005a4b69a4e6-1657267310; MDPIPHPSESSID=eec74b33bed9b421fc27abdc8413cfee; mdpi_cookies_enabled=1; __cf_bm=QjeDa5gUGK1EJUbkWfsjhab4g12zMKsW0hUKJ2x.on0-1657267313-0-ASLr0ndP8tt3Fpc2zuvukHsKbHiS57cKH1y6fjvLfXYcfUWZyf5Ya91tSRKOf0qNY4sI2x7EjAZyigiXtGEetDfA3glG9Peu2/t0XWPO8RWzrI1RfAvyUh4XWB/cHQX6lNPEZA11B8J6ygTDR23TnsbYf3hUSK8u/J2TWeh9XkkR; _ga=GA1.2.1660651209.1657267314; _gid=GA1.2.1299478954.1657267314; _gat=1; _fbp=fb.1.1657267316460.539484599; _hjSessionUser_1635407=eyJpZCI6IjBlM2U2ZGE5LTA1ODAtNTBlYi1hZjViLTdmNWI5Nzc5NTAzNSIsImNyZWF0ZWQiOjE2NTcyNjczMTYzNDIsImV4aXN0aW5nIjpmYWxzZX0=; _hjFirstSeen=1; _hjIncludedInSessionSample=0; _hjSession_1635407=eyJpZCI6Ijk4OGI1MGRhLTUyZmUtNDE1NC05MjA3LTRmYjMwMTQ5NDZiZCIsImNyZWF0ZWQiOjE2NTcyNjczMTgyNjQsImluU2FtcGxlIjpmYWxzZX0=; _hjAbsoluteSessionInProgress=0");
        this.put("sec-ch-ua","\".Not/A)Brand\";v=\"99\", \"Google Chrome\";v=\"103\", \"Chromium\";v=\"103\"");
        this.put("sec-ch-ua-mobile","?0");
        this.put("sec-ch-ua-platform","Windows");
        this.put("sec-fetch-dest","document");
        this.put("sec-fetch-mode","navigate");
        this.put("sec-fetch-site","none");
        this.put("sec-fetch-user","?1");
        this.put("upgrade-insecure-requests","1");
        this.put("user-agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/103.0.0.0 Safari/537.36");
    }};

    public MdpiAbstractAdaptor(String dirName) {
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
        Elements elements = document.select("div[class='art-abstract in-tab hypothesis_container']");
        if (Objects.isNull(elements)) {
            return null;
        }
        Element element = elements.first();
        if (Objects.isNull(element)) {
            return null;
        }
        return element.text();
    }
}