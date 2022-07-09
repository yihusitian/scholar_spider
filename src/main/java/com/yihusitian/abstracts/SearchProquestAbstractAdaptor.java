package com.yihusitian.abstracts;

import cn.hutool.core.util.StrUtil;

import java.util.HashMap;
import java.util.Map;

public class SearchProquestAbstractAdaptor extends AbstractAdaptor {

    private static final String SITE_URL = "search.proquest.com";

    //去查header信息, https://search.proquest.com/openview/29f867e1f8bbb773cd3628d9832047af/1?pq-origsite=gscholar&cbl=18750&diss=y
    private static final Map<String, String> HEADERS = new HashMap<String, String>() {{
    }};

    public SearchProquestAbstractAdaptor(String dirName) {
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
        return null;
    }
}