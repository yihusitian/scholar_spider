package com.yihusitian.abstracts;

import cn.hutool.core.util.StrUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

public class FrontiersinAbstractAdaptor extends AbstractAdaptor {

    private static final String SITE_URL = "www.frontiersin.org";

    //去查header信息, https://www.frontiersin.org/articles/10.3389/fpsyt.2021.537411/full
    private static final Map<String, String> HEADERS = new HashMap<String, String>() {{
        this.put("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        this.put("Accept-Language","zh-CN,zh;q=0.9,en;q=0.8");
        this.put("Cache-Control","max-age=0");
        this.put("Connection","keep-alive");
        this.put("Cookie","CurrentSessionId=8e77dfda-f9fa-4b0c-aa69-c3149dc6e199; _gcl_au=1.1.1952127714.1657266468; _gid=GA1.2.627966407.1657266474; _fbp=fb.1.1657266473816.2005397550; OptanonAlertBoxClosed=2022-07-08T07:48:23.983Z; spses.ae49=*; spid.ae49=0ef4b7fb-fa71-416b-bf98-8c0e00fee066.1657266935.1.1657266935.1657266935.3e548e88-145e-4c23-97b8-b0df1395ec1c; __atuvc=2%7C27; __atuvs=62c7e124b82a6314001; _gat_UA-9164039-1=1; _ga_MF9DGCL3QL=GS1.1.1657266473.1.1.1657266941.0; _ga=GA1.1.1203846643.1657266474; OptanonConsent=isGpcEnabled=0&datestamp=Fri+Jul+08+2022+15%3A55%3A42+GMT%2B0800+(%E4%B8%AD%E5%9B%BD%E6%A0%87%E5%87%86%E6%97%B6%E9%97%B4)&version=6.19.0&isIABGlobal=false&hosts=&consentId=383825bc-78fa-4c7b-9c25-45f42f96390d&interactionCount=1&landingPath=NotLandingPage&groups=C0001%3A1%2CC0002%3A1%2CC0003%3A1%2CC0004%3A1&geolocation=CN%3BBJ&AwaitingReconsent=false");
        this.put("Sec-Fetch-Dest","document");
        this.put("Sec-Fetch-Mode","navigate");
        this.put("Sec-Fetch-Site","none");
        this.put("Sec-Fetch-User","?1");
        this.put("Upgrade-Insecure-Requests","1");
        this.put("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/103.0.0.0 Safari/537.36");
        this.put("sec-ch-ua","\".Not/A)Brand\";v=\"99\", \"Google Chrome\";v=\"103\", \"Chromium\";v=\"103\"");
        this.put("sec-ch-ua-mobile","?0");
        this.put("sec-ch-ua-platform","Windows");
    }};

    public FrontiersinAbstractAdaptor(String dirName) {
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
        Element abstractDivElement = document.select("div[class='JournalAbstract']").first();
        if (Objects.isNull(abstractDivElement)) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder();
        Elements pElements = abstractDivElement.select("p[class=mb15]");
        if (Objects.isNull(pElements) || pElements.size() == 0) {
            return null;
        }
        Iterator<Element> iterator = pElements.iterator();
        while (iterator.hasNext()) {
            stringBuilder.append(iterator.next().text());
        }
        return stringBuilder.toString();
    }
}