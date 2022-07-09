package com.yihusitian.abstracts;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.yihusitian.util.HttpsRequestUtil;
import com.yihusitian.util.SleepUtil;

import java.io.File;
import java.util.Map;

/**
 * @Description 摘要适配器
 * @Author LeeHo
 * @Date 2022/7/8 13:28
 */
public abstract class AbstractAdaptor {

    private String dirName;

    public AbstractAdaptor(String dirName) {
        this.dirName = dirName;
    }

    /**
     * 获取摘要html文件名称
     *
     * @param href
     * @return
     */
    protected String getAbstractHtmlName(String href) {
        return dirName + StrUtil.SLASH + String.format("abstract_%s.html", SecureUtil.md5(href));
    }

    /**
     * 获取摘要html文件内容
     *
     * @param href
     * @return
     */
    protected String getAbstractHtmlContent(String href) {
        File file = new File(this.getAbstractHtmlName(href));
        if (!file.exists()) {
            return null;
        }
        return FileUtil.readUtf8String(file);
    }

    /**
     * 存储摘要html文件内容
     *
     * @param htmlContent
     * @param href
     */
    protected void storeAbstractHtmlContent(String htmlContent, String href) {
        if (StrUtil.isBlank(htmlContent)) {
            return;
        }
        File file = new File(this.getAbstractHtmlName(href));
        if (file.exists()) {
            return;
        }
        FileUtil.writeUtf8String(htmlContent, file);
    }

    /**
     * 爬取摘要信息
     *
     * @param href
     * @return
     */
    protected String doAbstractSpide(String href) {
        String quoteHtmlContent = this.getAbstractHtmlContent(href);
        if (StrUtil.isNotBlank(quoteHtmlContent)) {
            return quoteHtmlContent;
        }
        SleepUtil.sleepRandomSeconds(2, 5);
        String result = HttpsRequestUtil.doGet(href, getHeaders());
        this.storeAbstractHtmlContent(result, href);
        return result;
    }

    /**
     * 获取摘要信息
     *
     * @param href
     * @return
     */
    public String getAbstractInfo(String href) {
        String htmlContent = this.doAbstractSpide(href);
        if (StrUtil.isBlank(htmlContent)) {
            System.out.println("抓取摘要HTML为空, href: " + href);
            return "摘要抓取信息为空";
        }
        String parseContent = this.doParse(htmlContent);
        if (StrUtil.isBlank(parseContent)) {
            System.out.println("解析摘要信息为空, href: " + href);
            return "解析摘要信息为空";
        }
        return parseContent;
    }

    /**
     * 网站链接
     *
     * @return
     */
    protected abstract String getSiteUrl();

    /**
     * 获取头信息
     *
     * @return
     */
    protected abstract Map<String, String> getHeaders();

    /**
     * 解析摘要信息
     *
     * @param htmlContent
     * @return
     */
    protected abstract String doParse(String htmlContent);

    /**
     * 是否匹配
     *
     * @param href
     * @return
     */
    public boolean match(String href) {
        return href.contains(this.getSiteUrl());
    }
}
