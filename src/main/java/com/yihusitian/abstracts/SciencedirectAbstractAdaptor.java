package com.yihusitian.abstracts;

import cn.hutool.core.util.StrUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SciencedirectAbstractAdaptor extends AbstractAdaptor {

    private static final String SITE_URL = "www.sciencedirect.com";

    //去查header信息, https://www.sciencedirect.com/science/article/pii/S088915911730154X
    private static final Map<String, String> HEADERS = new HashMap<String, String>() {{

        this.put("authority","www.sciencedirect.com");
        this.put("accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        this.put("accept-language","zh-CN,zh;q=0.9,en;q=0.8");
        this.put("cache-control","max-age=0");
        this.put("cookie","__cf_bm=hkxQYzNgfPBq2fbOtXcgamXiyfCnLskh7Cc_Qp7m2CE-1657268186-0-AVQiBCDLEblfVOXZdfV/AOs6cQOWUNF4H2e2cY1kqCA0nCZb1YZLSxkw3rsCnmJjt94UbDHhJwq8i7qR7M7a8K1UWeLpk779NxNtSf2/eo5z; EUID=3b5b9209-d4ce-4ad6-ab80-bff2918d57ef; sd_session_id=fe9d63c546cef94b7218b760e89516bfc5dcgxrqa; acw=fe9d63c546cef94b7218b760e89516bfc5dcgxrqa%7C%24%7CD9B95CA3AE852400F64EE359A015778E3173594498AE44D68AA02CB58C551D014C27DF8EFBB9D1C408B83BD374B228FD4281CFA173CA6A553FBA44D1BD4E4F2EB0469A67597464825D387A21AFA2E514; has_multiple_organizations=false; MIAMISESSION=62b26c78-cada-4fae-aeab-7306c5ee6d7f:3834720986; id_ab=AEG; mbox=session%2342571bcb70ed412eaa90fb7bcf01000c%231657270047%7CPC%2342571bcb70ed412eaa90fb7bcf01000c.34_0%231720512987; mboxes=%7B%22corporate-sign-in%22%3A%7B%22variation%22%3A%22%231%22%2C%22enabled%22%3Afalse%7D%7D; utt=f304-20e05dcd1811a285dd5dc391dd65e00569a; AMCVS_4D6368F454EC41940A4C98A6%40AdobeOrg=1; AMCV_4D6368F454EC41940A4C98A6%40AdobeOrg=-2121179033%7CMCIDTS%7C19182%7CMCMID%7C30473544703260739892151209942286210933%7CMCAAMLH-1657872991%7C11%7CMCAAMB-1657872991%7CRKhpRz8krg2tLO6pguXWp5olkAcUniQYPHaMWWgdJ3xzPWQmdj0y%7CMCOPTOUT-1657275391s%7CNONE%7CvVersion%7C5.3.0");
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

    public SciencedirectAbstractAdaptor(String dirName) {
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
        Element divElement = document.getElementById("abstracts");
        if (Objects.isNull(divElement)) {
            return null;
        }
        Element abstractDivElement = divElement.select("div[class='abstract author']").first();
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