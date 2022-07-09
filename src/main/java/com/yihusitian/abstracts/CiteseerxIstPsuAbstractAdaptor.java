package com.yihusitian.abstracts;

import cn.hutool.core.util.StrUtil;

import java.util.HashMap;
import java.util.Map;

public class CiteseerxIstPsuAbstractAdaptor extends AbstractAdaptor {

    private static final String SITE_URL = "citeseerx.ist.psu.edu";

    //去查header信息, http://citeseerx.ist.psu.edu/viewdoc/download?doi=10.1.1.1074.7374&rep=rep1&type=pdf
    private static final Map<String, String> HEADERS = new HashMap<String, String>() {{
    }};

    public CiteseerxIstPsuAbstractAdaptor(String dirName) {
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