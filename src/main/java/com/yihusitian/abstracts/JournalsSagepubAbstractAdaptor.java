package com.yihusitian.abstracts;

import cn.hutool.core.util.StrUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class JournalsSagepubAbstractAdaptor extends AbstractAdaptor {

    private static final String SITE_URL = "journals.sagepub.com";

    //去查header信息, https://journals.sagepub.com/doi/abs/10.1177/1362361306062013

    private static final Map<String, String> HEADERS = new HashMap<String, String>() {{
        this.put("authority","journals.sagepub.com");
        this.put("accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        this.put("accept-language","zh-CN,zh;q=0.9,en;q=0.8");
        this.put("cache-control","max-age=0");
        this.put("upgrade-insecure-requests","1");
        this.put("user-agent","user-agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/103.0.0.0 Safari/537.36");
    }};

    public JournalsSagepubAbstractAdaptor(String dirName) {
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
    public String getAbstractInfo(String href) {
        return "此网站暂不支持抓取";
    }


    @Override
    protected String doParse(String htmlContent) {
        if (StrUtil.isEmpty(htmlContent)) {
            return null;
        }
        Document document = Jsoup.parse(htmlContent);
        Element abstractDivElement = document.select("div[class='abstractSection abstractInFull']").first();
        if (Objects.isNull(abstractDivElement)) {
            return null;
        }
        Element pElement = abstractDivElement.getElementsByTag("p").first();
        if (Objects.isNull(pElement)) {
            return null;
        }
        return pElement.text();
    }
}