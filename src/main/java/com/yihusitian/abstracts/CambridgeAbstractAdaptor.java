package com.yihusitian.abstracts;

import cn.hutool.core.util.StrUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CambridgeAbstractAdaptor extends AbstractAdaptor {

    private static final String SITE_URL = "www.cambridge.org";

    private static final String HREF_PREFIX = "https://" + SITE_URL;

    //去查header信息, https://www.cambridge.org/core/journals/development-and-psychopathology/article/roles-of-social-withdrawal-peer-rejection-and-victimization-by-peers-in-predicting-loneliness-and-depressed-mood-in-childhood/0537373B9B1F5FA18CFD6BC785122BAA
    private static final Map<String, String> HEADERS = new HashMap<String, String>() {{
        this.put("authority","www.cambridge.org");
        this.put("accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        this.put("accept-language","zh-CN,zh;q=0.9,en;q=0.8");
        this.put("cache-control","max-age=0");
        this.put("cookie","session=s%3AgpFfLicNCm6qz11UobrQF5lzvgGxkxrS.aoRhZxtig9HPpXX6jlt8Tgkw3tYPklaZ%2BZFYkFvpZIw; _ga=GA1.2.142793904.1657265905; _gid=GA1.2.615201609.1657265905; _gcl_au=1.1.2084290440.1657265905; _hjFirstSeen=1; _hjSession_2580298=eyJpZCI6IjUxMzA2MThkLWE4ZGUtNDZmMi05NjAwLWZlMDgyMzMzNjE3NiIsImNyZWF0ZWQiOjE2NTcyNjU5MDczOTksImluU2FtcGxlIjpmYWxzZX0=; _hjAbsoluteSessionInProgress=0; _hjSessionUser_2580298=eyJpZCI6IjM2NmVhODIxLWUwNTgtNTY1NS05NTY2LTVkZGY0NDlkZDFjOCIsImNyZWF0ZWQiOjE2NTcyNjU5MDY4NDQsImV4aXN0aW5nIjp0cnVlfQ==; aca-session=Fe26.2**7183f53c3b2fdc24e51bc0e18fa11f18ba58bee10a39401bf9164b72cf6c65a5*aQ_T8c4KToqg82Z8PQNSXg*5KSXUI1fsTt-EU8OILsjb1fOtnBW8ZOXQu1q0sSpB3rIQLRs9NiZSBh7esza9RjadyhDraRc8U3Ojkk6ywXnfZ9LW93Uzh0Rqgg2Zb1RKPU**f8e1e80072dc0d379d76eaf194695f9c55c7291aee288875aa9f9c1021493716*s_rQ2GyI_67PHShbUNFg-YVSgRvwXNT14u70DYSoQIo; _gat=1; _hjIncludedInSessionSample=0; __atuvc=3%7C27; __atuvs=62c7def435e9b38a002; site24x7rumID=3069167363296091.1657267006222.1657267006222");
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

    public CambridgeAbstractAdaptor(String dirName) {
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
     *
     * @param href
     * @return
     */
    protected String doAbstractSpide(String href) {
        String htmlContent = super.doAbstractSpide(href);
        if (StrUtil.isNotBlank(htmlContent)) {
            if (htmlContent.contains("Redirecting")) {
                Document document = Jsoup.parse(htmlContent);
                Element aElement = document.getElementsByTag("a").first();
                if (Objects.nonNull(aElement)) {
                    href = aElement.attr("href");
                    if (StrUtil.isNotEmpty(href)) {
                        return super.doAbstractSpide(HREF_PREFIX + href);
                    }
                }
            }

            return htmlContent;
        }
        return htmlContent;
    }



    @Override
    protected String doParse(String htmlContent) {
        if (StrUtil.isEmpty(htmlContent)) {
            return null;
        }
        Document document = Jsoup.parse(htmlContent);
        Element abstractDivElement = document.select("div[class=abstract-content]").first();
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