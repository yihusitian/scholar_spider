package com.yihusitian.abstracts;

import cn.hutool.core.util.StrUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

//不支持抓取 页面数据不是简单的html
public class PsycnetApaAbstractAdaptor extends AbstractAdaptor {

    private static final String SITE_URL = "psycnet.apa.org";

    private static final Map<String, String> HEADERS = new HashMap<String, String>(){{
        this.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        this.put("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8");
        this.put("Cache-Control", "max-age=0");
        this.put("Connection", "keep-alive");
        this.put("Cookie", "PN_ACCESSTIME=1657257998962; csId=bf176567-b66a-41fb-b9de-0356b2281ae6; PN_HOST=https%3A%2F%2Fpsycnet.apa.org; PN_RC=false; visid_incap_2377601=kuC19+SaQt2Yq/9lW/y0zN+hx2IAAAAAQUIPAAAAAAA4fTRSNcR13lNvYeplTOta; nlbi_2377601=ebVGRTdb9Q323Wr7RZi9EAAAAADaMnCS9p8QIdpdVQLvNLdi; defaultLocale=en-US; currency=USD; _gid=GA1.2.1947720095.1657250278; cart_my_apa_org=1d3fa779-0dda-4c01-adc0-691d2e8e4954; visid_incap_2624409=78JMth7QRP6tQloC7TffbOShx2IAAAAAQUIPAAAAAAB8/q9PS+VQ9z7GauZwuXqs; nlbi_2624409=3jc/e53bMXxUCJoIzJKtcwAAAAAVNBY3hX7KuOTsSXgoxiMO; incap_ses_1439_2624409=Bd7MGr9cOEqvxYQ+g1v4E+Whx2IAAAAAQOXhtRAanR/ipkGh4sDE+g==; visid_incap_2624412=9n9CS75aS1ugdp9HS508Vuqwx2IAAAAAQUIPAAAAAACVL65iVI6TSYZpBe5qL/FO; nlbi_2624412=FHa6UG7cDVBpJwai1iJ/EQAAAABxk/iB57e2Eok0Pno3SEYO; incap_ses_200_2624412=KBqKEyPZvSpV1QiJS4vGAuuwx2IAAAAAIdtIjRFIocQzzV3US2yfpg==; REFRESH_SESSION=true; _gcl_au=1.1.1157458632.1657254126; _ga_SZXLGDJGNB=GS1.1.1657254126.1.0.1657254126.60; connect.sid=s%3AQbLaYU-abvoP_T72DPpA-qmrzEDra9Vz.2BKTxkWPmLXZ9l0GP2WNFKqITaJLueHeG7iH%2Bzg2p%2B4; incap_ses_200_2377601=j7qlIpgaRHlKVhKJS4vGAgzAx2IAAAAAev1S02PUptyWJAGIXvgG1w==; _ga_H2KGDH2XNS=GS1.1.1657257998.3.0.1657257998.60; _ga=GA1.1.2104743308.1657250278; _dc_gtm_UA-10493335-18=1; nlbi_2377601_2147483392=P7BUfID+DxhLk9fVRZi9EAAAAACZCmgBm6K+tpoDsMsYFHBy; reese84=3:ESuWPKNFtwAmdFUjCACh1A==:vpq9t2nTleCoaB+Qf9y+wDxr0BMrIE1nZa/owbNlRaPQo47dATihJlNbEpDsNrTLMk1QvdaxFxVUr2TfYskgO8pgCyzYkV7K18VGWxmAEX7ul1JrkqCXaaStmij545rW0b/YaSHN0OO+dJC1T1d6UZrvAtua+9VlCtuFPsv6/CbptRpVjwWySB51QD4prXILg/6GuXIYoE2XFTT0owIavRGhwQ06n21U2MrrPp7SQmLHDSaAI2eQXb1c254fJnlzVV4RXApK2N5bJoUGLZBx2+Jr1T0+YqaGJydkV/3OvSuOK9SoxzA0aAHJpX8KpXvrM0QxHq/O3k7MXck6LLvBWFl5YyNhNBsOVvN8gcKCb85EKfUSaVTiPaPKlr5Hqoeb+bnFvQu873Gf3AQ6OhfB9klbwDqst+ish6u4Ydw+vMlZC2byXS8NEZWg7x909Ldb:+I9esedfdgv1qDVlepb85ToCYcyoWpdDs5brIyqy5lc=; incap_ses_517_2624409=9b7iKA3JllQKgL1MFMEsBxDAx2IAAAAAeT+IVx/LcYwkNMmya+haaQ==");
        this.put("If-Modified-Since", "Tue, 28 Jun 2022 18:53:10 GMT");
        this.put("If-None-Match", "W/\"2a47-181aba905f0\"");
        this.put("Sec-Fetch-Dest", "document");
        this.put("Sec-Fetch-Mode", "navigate");
        this.put("Sec-Fetch-Site", "same-origin");
        this.put("Sec-Fetch-User", "?1");
        this.put("Upgrade-Insecure-Requests", "1");
        this.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/103.0.0.0 Safari/537.36");
        this.put("sec-ch-ua", "\".Not/A)Brand\";v=\"99\", \"Google Chrome\";v=\"103\", \"Chromium\";v=\"103\"");
        this.put("sec-ch-ua-mobile", "?0");
        this.put("sec-ch-ua-platform", "Windows");
    }};

    public PsycnetApaAbstractAdaptor(String dirName) {
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
        Element abstractElement = document.getElementsByTag("abstract").first();
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