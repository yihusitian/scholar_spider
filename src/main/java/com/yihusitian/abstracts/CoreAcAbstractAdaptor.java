package com.yihusitian.abstracts;

import cn.hutool.core.util.StrUtil;

import java.util.HashMap;
import java.util.Map;

public class CoreAcAbstractAdaptor extends AbstractAdaptor {

    private static final String SITE_URL = "core.ac.uk";

    //去查header信息, https://core.ac.uk/download/pdf/149232717.pdf
    private static final Map<String, String> HEADERS = new HashMap<String, String>() {{
    }};

    public CoreAcAbstractAdaptor(String dirName) {
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