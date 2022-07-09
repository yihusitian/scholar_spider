package com.yihusitian.abstracts;

import cn.hutool.core.util.StrUtil;

import java.util.HashMap;
import java.util.Map;

public class AcamhOnlinelibraryWileyAbstractAdaptor extends AbstractAdaptor {

    private static final String SITE_URL = "acamh.onlinelibrary.wiley.com";

    //去查header信息, https://acamh.onlinelibrary.wiley.com/doi/abs/10.1111/jcpp.13459
    private static final Map<String, String> HEADERS = new HashMap<String, String>() {{
    }};

    public AcamhOnlinelibraryWileyAbstractAdaptor(String dirName) {
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