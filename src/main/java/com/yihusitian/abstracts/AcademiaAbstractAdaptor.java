package com.yihusitian.abstracts;

import cn.hutool.core.util.StrUtil;

import java.util.HashMap;
import java.util.Map;

public class AcademiaAbstractAdaptor extends AbstractAdaptor {

    private static final String SITE_URL = "www.academia.edu";

    //去查header信息, 404不处理 https://www.academia.edu/download/65906951/Social_withdrawal_in_childhood_Conceptua20210305-10115-seut4y.pdf
    private static final Map<String, String> HEADERS = new HashMap<String, String>() {{
    }};

    public AcademiaAbstractAdaptor(String dirName) {
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