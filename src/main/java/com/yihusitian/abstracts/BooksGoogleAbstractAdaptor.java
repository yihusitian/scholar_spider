package com.yihusitian.abstracts;

import cn.hutool.core.util.StrUtil;

import java.util.HashMap;
import java.util.Map;

public class BooksGoogleAbstractAdaptor extends AbstractAdaptor {

    private static final String SITE_URL = "books.google.com";

    //去查header信息, https://books.google.com/books?hl=zh-CN&lr=&id=FDu7Y9tTbpYC&oi=fnd&pg=PA3&dq=social+withdrawal+*anxiety+*depression&ots=_4RsmKOFQw&sig=RO75Q7sPnYXT3CyLZ8lebzmInY4
    private static final Map<String, String> HEADERS = new HashMap<String, String>() {{
    }};

    public BooksGoogleAbstractAdaptor(String dirName) {
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