package com.yihusitian.abstracts;

import cn.hutool.core.util.StrUtil;

import java.util.HashMap;
import java.util.Map;

public class JpsychopatholAbstractAdaptor extends AbstractAdaptor {

    private static final String SITE_URL = "www.jpsychopathol.it";

    //去查header信息, https://www.jpsychopathol.it/wp-content/uploads/2015/07/01b-Suwa1.pdf
    private static final Map<String, String> HEADERS = new HashMap<String, String>() {{
    }};

    public JpsychopatholAbstractAdaptor(String dirName) {
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