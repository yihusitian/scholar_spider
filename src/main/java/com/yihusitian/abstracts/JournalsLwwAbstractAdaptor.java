package com.yihusitian.abstracts;

import cn.hutool.core.util.StrUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class JournalsLwwAbstractAdaptor extends AbstractAdaptor {

    private static final String SITE_URL = "journals.lww.com";

    //去查header信息, https://journals.lww.com/jonmd/Fulltext/2006/03000/Recognizing_the_Anxious_Face_of_Depression.9.aspx
    private static final Map<String, String> HEADERS = new HashMap<String, String>() {{
        this.put("authority","journals.lww.com");
        this.put("accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        this.put("accept-language","zh-CN,zh;q=0.9,en;q=0.8");
        this.put("cache-control","max-age=0");
        this.put("cookie","EJP_AspState=0vab4ng1uooj1xko4i303joc; EJP_SessionIdCookie=CounterDataSessionId=1800d280-b63b-48ab-a386-3d1736ceab1f; EJP_JournalsLockCookie=id=f177b802-0104-4346-8d27-3ff370d2ced4&ip=10.232.62.87; ApplicationGatewayAffinity=2bb0863798bfcbb0b2a4a319190871fa89f7f74815e2bd3caf8bc85e3470b178; ApplicationGatewayAffinityCORS=2bb0863798bfcbb0b2a4a319190871fa89f7f74815e2bd3caf8bc85e3470b178; __cfruid=7cca306d43a6d6e1cc9566f4bb21604f7e1ef2b5-1657266741; AMCVS_A450776A5245ACC00A490D44%40AdobeOrg=1; s_ecid=MCMID%7C23177491438894801881709835299259311116; AMCV_A450776A5245ACC00A490D44%40AdobeOrg=-1124106680%7CMCIDTS%7C19182%7CMCMID%7C23177491438894801881709835299259311116%7CMCAAMLH-1657871544%7C11%7CMCAAMB-1657871544%7CRKhpRz8krg2tLO6pguXWp5olkAcUniQYPHaMWWgdJ3xzPWQmdj0y%7CMCOPTOUT-1657273945s%7CNONE%7CMCAID%7CNONE%7CvVersion%7C5.2.0; _ga=GA1.3.104719758.1657266746; _gid=GA1.3.925760274.1657266746; _gat_UA-145030560-1=1; liveagent_oref=; s_cc=true; liveagent_sid=ffcf4196-22ad-471b-ab33-3b8608ce9e1c; liveagent_vc=2; liveagent_ptid=ffcf4196-22ad-471b-ab33-3b8608ce9e1c; __cf_bm=b.vitvw_YjW8k3PcB.C_JhB.09sQMP_TC9TH4nTPAiE-1657266749-0-AUTdcXejPzQgppdgtJHLWIU4XzEaH6yWmmtzMMC0dfqXHtw6cVVIvlfdvHhcfzVx5yD4E0DvtYWc5+GSLoAwgnzP5s3zKZOgpZxnQYQxKUlLV/uhkjp67my50mJF1Grl1VBO6PS1JSBw0WETU1dEWZSwwyZ/YogkvZnL4OtTwmlaJLYsHgUFJ5d+vr8Usd7G3w==; aam_uuid=30463798727027762082149950879755710048; WSS_FullScreenMode=false; _mkto_trk=id:681-FHE-429&token:_mch-lww.com-1657266750267-12324; isHideCookieBanner=true; s_sess=%20s_e2%3DNot%2520Authenticated%3B%20SC_LINKS%3D%3B; s_pers=%20s_nr6%3D1657266750694-New%7C1688802750694%3B; s_sq=%5B%5BB%5D%5D");
        this.put("if-modified-since","Fri, 08 Jul 2022 07:52:22 GMT");
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

    public JournalsLwwAbstractAdaptor(String dirName) {
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
        Element abstractElement = document.getElementById("article-abstract-content1");
        if (Objects.isNull(abstractElement)) {
            return null;
        }
        Element pElement = abstractElement.getElementsByTag("p").first();
        if (Objects.isNull(pElement)) {
            return null;
        }
        return pElement.text();
    }
}