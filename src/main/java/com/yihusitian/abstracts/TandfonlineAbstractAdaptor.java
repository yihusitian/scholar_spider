package com.yihusitian.abstracts;

import cn.hutool.core.util.StrUtil;
import com.yihusitian.util.HttpsRequestUtil;
import com.yihusitian.util.URLConnectionUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @Description TODO
 * @Author LeeHo
 * @Date 2022/7/8 14:36
 */
public class TandfonlineAbstractAdaptor extends AbstractAdaptor {

    private static final String SITE_URL = "www.tandfonline.com";

    private static final Map<String, String> HEADERS = new HashMap<String, String>() {{
        this.put("authority","www.tandfonline.com");
        this.put("accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        this.put("accept-language","zh-CN,zh;q=0.9,en;q=0.8");
        this.put("cache-control","max-age=0");
        this.put("user-agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/103.0.0.0 Safari/537.36");
    }};

    public TandfonlineAbstractAdaptor(String dirName) {
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

    /**
     * 爬取摘要信息
     *
     * @param href
     * @return
     */
    @Override
    protected String doAbstractSpide(String href) {
        String quoteHtmlContent = this.getAbstractHtmlContent(href);
        if (StrUtil.isNotBlank(quoteHtmlContent)) {
            return quoteHtmlContent;
        }
        try {
            String[] results = URLConnectionUtil.getRedictLocationAndCookie(href, HEADERS);
            HEADERS.put("cookie", results[1]);
            String content = HttpsRequestUtil.doGet(results[0], HEADERS);
            this.storeAbstractHtmlContent(content, href);
            return content;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("TandfonlineAbstractAdaptor请求异常了, href: " + href);
        }
        return null;
    }

    @Override
    protected String doParse(String htmlContent) {
        if (StrUtil.isEmpty(htmlContent)) {
            return null;
        }
        Document document = Jsoup.parse(htmlContent);
        Element element = document.select("div[class='abstractSection abstractInFull']").first();
        if (Objects.isNull(element)) {
            return null;
        }
        Element pElement = element.getElementsByTag("p").last();
        if (Objects.isNull(pElement)) {
            return null;
        }
        return pElement.text();
    }

}