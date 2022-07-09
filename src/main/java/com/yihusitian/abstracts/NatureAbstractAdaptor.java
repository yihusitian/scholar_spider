package com.yihusitian.abstracts;

import cn.hutool.core.util.StrUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class NatureAbstractAdaptor extends AbstractAdaptor {

    private static final String SITE_URL = "www.nature.com";

    //去查header信息, https://www.nature.com/articles/s41380-020-0788-3
    private static final Map<String, String> HEADERS = new HashMap<String, String>() {{
        this.put("authority","www.nature.com");
        this.put("accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        this.put("accept-language","zh-CN,zh;q=0.9,en;q=0.8");
        this.put("cache-control","max-age=0");
        this.put("cookie","idp_session=sVERSION_1cfb3f657-ba21-4dfb-a53d-484cf35f0602; idp_session_http=hVERSION_1cd26f1d4-c0d7-40ce-b022-755f2258bf8f; idp_marker=fbbc7d11-0b1a-47be-8677-24f1e8f586fa; user.uuid=\"b6f39954-6447-4b38-8337-e19d35ed4d2d\"; CONTENT_USAGE_SESSIONID=\"utterlyidle:v1:ZTBhMWJiYmUtOGRiYy00M2M1LTlhMDItNmI3Y2UzZDhhOTVk\"; sncc=P=8:V=9.0.0&C=C01");
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

    public NatureAbstractAdaptor(String dirName) {
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
        Element abstractElement = document.getElementById("Abs1-content");
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