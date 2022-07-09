package com.yihusitian.abstracts;

import cn.hutool.core.util.StrUtil;

import java.util.HashMap;
import java.util.Map;

public class PsychiatristAbstractAdaptor extends AbstractAdaptor {

    private static final String SITE_URL = "www.psychiatrist.com";

    //去查header信息, https://www.psychiatrist.com/wp-content/uploads/2021/02/13687_gender-differences-presentation-management-social.pdf
    private static final Map<String, String> HEADERS = new HashMap<String, String>() {{
    }};

    public PsychiatristAbstractAdaptor(String dirName) {
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