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

public class TaylorfrancisAbstractAdaptor extends AbstractAdaptor {

    private static final String SITE_URL = "www.taylorfrancis.com";

    //去查header信息, https://www.taylorfrancis.com/chapters/edit/10.4324/9781315799544-14/socialization-factors-development-social-withdrawal-rosemary-mills-kenneth-rubin-117
    private static final Map<String, String> HEADERS = new HashMap<String, String>() {{
        this.put("authority","www.taylorfrancis.com");
        this.put("accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        this.put("accept-language","zh-CN,zh;q=0.9,en;q=0.8");
        this.put("cache-control","max-age=0");
        this.put("cookie","_token=eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJpcCI6IjYxLjUwLjEzMC4yNTIiLCJjb3VudHJ5X2NvZGUiOiJDTiIsInNjb3BlIjpbInByb2R1Y3RfYWNjZXNzIl0sInIiOlsiQlVTIl0sInRlcnJpdG9yeV90eXBlIjoiY291bnRyeSIsInVzZXIiOnsidXNlclR5cGUiOiJpcCIsIl9pZCI6IjYyYzdlOTM5OTRkOWU3MDAyMTQzZjE1ZCIsInVzZXJuYW1lIjoiNjEuNTAuMTMwLjI1MiIsImVtYWlsIjoiYW5ub255bW91cyIsImRpc3BsYXlOYW1lIjoiIiwiaGFzQWNjZXB0ZWRUZXJtcyI6ZmFsc2UsIm1GQUF1dGhlbnRpY2F0ZWQiOmZhbHNlLCJvcmdhbml6YXRpb25OYW1lIjpudWxsLCJvcmdhbml6YXRpb25JZCI6bnVsbH0sImlzcyI6Imh0dHBzOi8vYWNjb3VudHMudGF5bG9yZnJhbmNpcy5jb20vaWRlbnRpdHkvIiwiZXhwIjoxNjU3MjcyMTM3LCJpYXQiOjE2NTcyNjg1MzcsImF1ZCI6IjFlNGE3MTI3ZDc5ZTgzNzIxNGJhNjQzMTU2ZTM3ZjU5OWQwYzJjZDE1YzY5ZDFiMmQzMWNkY2Y5ZWUyMjc5ZDAiLCJzdWIiOiI2MmM3ZTkzOTk0ZDllNzAwMjE0M2YxNWQifQ.Qinw3T6FSK9YimM0LAzhjPsa4R6wznMiSogiZZH9_QRqWrIktEJVGYT8HcTvQjljIRhckCa0IQR9npdPf7z6RUhFHqtPi8Ww0VNw-cCnXoZqKtj3bFDfhuDvhHChUZ_q3nv0bTSAH4S8gGp-aQRrG2-x6a7G6ySWdH3UzjzScSiZa9JIZ-UCm0O4eqWpXp06SXN917KgmbX5SXkpTRz13W7tfjFvaoTYbq7FXXVAhAFsgSZvPNnbPVqlgs1TwChcts5qiE-LBDvgGqIuK2c_eZtEPwgtE3uZktA6-KJ1TQfh7d6Y-eIAdPpnb0XsKUYeicSJ5KQo2TK3RNkuZ6BYvQ; books_token=eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJpcCI6IjYxLjUwLjEzMC4yNTIiLCJjb3VudHJ5X2NvZGUiOiJDTiIsInNjb3BlIjpbInByb2R1Y3RfYWNjZXNzIl0sInIiOlsiQlVTIl0sInRlcnJpdG9yeV90eXBlIjoiY291bnRyeSIsInVzZXIiOnsidXNlclR5cGUiOiJpcCIsIl9pZCI6IjYyYzdlOTM5OTRkOWU3MDAyMTQzZjE1ZCIsInVzZXJuYW1lIjoiNjEuNTAuMTMwLjI1MiIsImVtYWlsIjoiYW5ub255bW91cyIsImRpc3BsYXlOYW1lIjoiIiwiaGFzQWNjZXB0ZWRUZXJtcyI6ZmFsc2UsIm1GQUF1dGhlbnRpY2F0ZWQiOmZhbHNlLCJvcmdhbml6YXRpb25OYW1lIjpudWxsLCJvcmdhbml6YXRpb25JZCI6bnVsbH0sImlzcyI6Imh0dHBzOi8vYWNjb3VudHMudGF5bG9yZnJhbmNpcy5jb20vaWRlbnRpdHkvIiwiZXhwIjoxNjU3MjcyMTM3LCJpYXQiOjE2NTcyNjg1MzcsImF1ZCI6IjFlNGE3MTI3ZDc5ZTgzNzIxNGJhNjQzMTU2ZTM3ZjU5OWQwYzJjZDE1YzY5ZDFiMmQzMWNkY2Y5ZWUyMjc5ZDAiLCJzdWIiOiI2MmM3ZTkzOTk0ZDllNzAwMjE0M2YxNWQifQ.Qinw3T6FSK9YimM0LAzhjPsa4R6wznMiSogiZZH9_QRqWrIktEJVGYT8HcTvQjljIRhckCa0IQR9npdPf7z6RUhFHqtPi8Ww0VNw-cCnXoZqKtj3bFDfhuDvhHChUZ_q3nv0bTSAH4S8gGp-aQRrG2-x6a7G6ySWdH3UzjzScSiZa9JIZ-UCm0O4eqWpXp06SXN917KgmbX5SXkpTRz13W7tfjFvaoTYbq7FXXVAhAFsgSZvPNnbPVqlgs1TwChcts5qiE-LBDvgGqIuK2c_eZtEPwgtE3uZktA6-KJ1TQfh7d6Y-eIAdPpnb0XsKUYeicSJ5KQo2TK3RNkuZ6BYvQ; books_refresh_token=25a9c9da2cf48053170d0a506c5d6ea7; refresh_token=25a9c9da2cf48053170d0a506c5d6ea7; __cf_bm=i43Jh.pB1dUQYe7PCY9uxTX4dbPlEAPbJ11Cgy8NCdg-1657268537-0-Ab6G/XM6LDTxDvdfYq0cdz4MXR2bYqJkKNlcRDOj3zKiYbv0By8OASowXcm40HImf5u9KX/p+bvxK3uh43YDnP4=; _ga=GA1.2.1096237174.1657268538; _gid=GA1.2.2020552097.1657268538; _gat_UA-79563113-8=1; _fbp=fb.1.1657268538629.1814351667; __atuvc=1%7C27; __atuvs=62c7e93b15faadf6000; _cs_c=1; _cs_id=0d47947d-33da-ad51-b3a4-8f9617d839bf.1657268542.1.1657268542.1657268542.1623235254.1691432542008; _cs_s=1.0.0.1657270342010; __asc=e67b16da181dce71d0a6286d0ec; __auc=e67b16da181dce71d0a6286d0ec; _hjSessionUser_1518123=eyJpZCI6IjIxOTE4MWNkLWY1ZWEtNTdjNS05YWI1LTVjZGY3ZDI4ZGMzNCIsImNyZWF0ZWQiOjE2NTcyNjg1Mzg5MzIsImV4aXN0aW5nIjpmYWxzZX0=; _hjFirstSeen=1; _hjIncludedInSessionSample=0; _hjSession_1518123=eyJpZCI6ImJmYTBmMDIwLWJlNjctNGEyZi04Yjk3LTkxNWMwZGU3OWIzMyIsImNyZWF0ZWQiOjE2NTcyNjg1NDM0OTksImluU2FtcGxlIjpmYWxzZX0=; _hjAbsoluteSessionInProgress=1");
        this.put("if-modified-since","Sun, 19 Jun 2022 05:44:01 GMT");
        this.put("sec-ch-ua","\".Not/A)Brand\";v=\"99\", \"Google Chrome\";v=\"103\", \"Chromium\";v=\"103\"");
        this.put("sec-ch-ua-mobile","?0");
        this.put("sec-ch-ua-platform","Windows");
        this.put("sec-fetch-dest","document");
        this.put("sec-fetch-mode","navigate");
        this.put("sec-fetch-site","same-origin");
        this.put("sec-fetch-user","?1");
        this.put("upgrade-insecure-requests","1");
        this.put("user-agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/103.0.0.0 Safari/537.36");
    }};

    public TaylorfrancisAbstractAdaptor(String dirName) {
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
        Element divElement = document.getElementById("collapseContent");
        if (Objects.isNull(divElement)) {
            return null;
        }
        Elements pElements = divElement.getElementsByTag("p");
        if (Objects.isNull(pElements)) {
            return null;
        }
        Iterator<Element> elementIterator = pElements.iterator();
        while (elementIterator.hasNext()) {
            Element element = elementIterator.next();
            String text = element.text().trim();
            if (StrUtil.isNotEmpty(text)) {
                return text;
            }
        }
        return null;
    }
}