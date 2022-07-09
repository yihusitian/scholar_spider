package com.yihusitian.spider;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yihusitian.Controller;
import com.yihusitian.abstracts.AbstractAdaptor;
import com.yihusitian.abstracts.AbstractAdaptorManager;
import com.yihusitian.bean.ArticleInfo;
import com.yihusitian.excel.ArticleInfoExcelDataHandler;
import com.yihusitian.excel.ArticleInfoExcelExportStyler;
import com.yihusitian.util.HttpRequestUtil;
import com.yihusitian.util.SleepUtil;
import org.apache.poi.ss.usermodel.Workbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Struct;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Description
 * @Author leeho
 * @Date 2022/7/7 下午10:06
 */
public class GoogleScholarSpider {

    private static final String SITE_URL = "https://scholar.google.com";

    private static final String SEARCH_URL = SITE_URL + "/scholar?hl=zh-CN&as_sdt=0,5&q=";

    private static final String QUOTE_URL_TEMPLATE = "https://scholar.google.com/scholar?q=info:%s:scholar.google.com/&output=cite&scirp=1&hl=zh-CN";

    //一个关键字最多抓取前10页，后面的参考价值也不大了
    private static final int LIMIT_PAGE = 10;

    //下载路径
    private String downloadDir;

    //关键字
    private String keyword;

    //控制器
    private Controller controller;

    public GoogleScholarSpider(Controller controller, String downloadDir, String keyword) {
        this.controller = controller;
        this.downloadDir = downloadDir;
        this.keyword = keyword.trim();
    }

    private static final Map<String, String> HEADERS = new HashMap<String, String >() {{
        this.put("authority", "scholar.google.com");
        this.put("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        this.put("accept-language", "zh-CN,zh;q=0.9");
        this.put("cookie", "CONSENT=YES+AE.zh-CN+20180429-14-0; ANID=AHWqTUmUca29_XCMSFI6Uov14lF7BMnyKVNpYPM3UqkGC01V8oOGOMoJl-tmTus7; SEARCH_SAMESITE=CgQI55UB; AEC=AakniGM37vbe0MRl9a-ZgcerUrSQUuCIMdFAn7HoANT5RsfK4XDf1oLx5x4; SID=MAgZYTxRjS7RiL1Vz4s0qwmhwa_4F7YYwERm_T9u76SDR76IsRmK1xO6C50bP9lF_bG5TA.; __Secure-1PSID=MAgZYTxRjS7RiL1Vz4s0qwmhwa_4F7YYwERm_T9u76SDR76Iw8BSGf5C1zwY_fjYALrEyQ.; __Secure-3PSID=MAgZYTxRjS7RiL1Vz4s0qwmhwa_4F7YYwERm_T9u76SDR76IfxQyn1oSRO5Gf6YD68k1AQ.; HSID=A9HO-cZp4HYVPFUbN; SSID=AWlDDtd-oRBMff_ZX; APISID=WRKMqM6Zs0lskhuV/AbzNT6JiLuzsbwYLj; SAPISID=7kHWHSwn9LyR8zJK/AeM09kM3Mpbi6QiK5; __Secure-1PAPISID=7kHWHSwn9LyR8zJK/AeM09kM3Mpbi6QiK5; __Secure-3PAPISID=7kHWHSwn9LyR8zJK/AeM09kM3Mpbi6QiK5; NID=511=ZIpsiZUfWuizgjCCPGyi8mB-h1syOq4YyMznKIfNpcx_8iv6ZmqEVWy_nJX9h2OpEolUxDnkyU20KPFxoByRGfgxV5hnzSsfeLVgVtATot-lgEwKqMNKYh5cBn2XfwVbpCoYx5lKz1AMuIytFGkJnd77uJSFDk18lBVX-w0xAhlfAFEzvpvNGnEBOtmqSGaGPc-NM40AC7skccIY0n3h1V9rGQ4FM0ssTnTgdlGcRKh8zehuM0HqH4HOCU8Ar_XLgU355o9DwoUbuGU54k5lJJykNAL31hFSZ9D5ZN8m7RvMG8UdqimvxXf4U4xJo_ZxXm3r8B6w0bM5KJQPDEu6; GSP=IN=8b9a455bd1c58d67:LD=zh-CN:LM=1657202142:S=glRFE3rjANsOGxkE; 1P_JAR=2022-07-07-14; SIDCC=AJi4QfGIuEHjUdXbtPCApen0U_rm-MtpRASBFvxJJWIAiJa2qLD39dEfTPkl8S8oqwYfckfjuA; __Secure-1PSIDCC=AJi4QfFHVH4onrXxhdxN-JN3hIkWQVn5kn0eHLXQAvu-qoAR0OiNK7IzI_X0wdubQMl8-S8LXA; __Secure-3PSIDCC=AJi4QfGVjKxtHZm1HM_uz9P8QMBdrVmPVx6YWjQjIAOBPHO-DASVDn3bgwq9WOZJvb8mRd7yQD4");
        this.put("referer", "https://scholar.google.com/scholar?hl=zh-CN&as_sdt=0%2C5&q=social+withdrawal+*anxiety+*depression&oq=");
        this.put("sec-ch-ua", "\".Not/A)Brand\";v=\"99\", \"Google Chrome\";v=\"103\", \"Chromium\";v=\"103\"");
        this.put("sec-ch-ua-mobile", "?0");
        this.put("sec-ch-ua-platform", "macOS");
        this.put("sec-fetch-dest", "document");
        this.put("sec-fetch-mode", "navigate");
        this.put("sec-fetch-site", "same-origin");
        this.put("sec-fetch-user", "?1");
        this.put("upgrade-insecure-requests", "1");
        this.put("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/103.0.0.0 Safari/537.36");
        this.put("x-client-data", "CLK1yQEIlbbJAQimtskBCMS2yQEIqZ3KAQjghcsBCJWhywEI2+/LAQjpucwBCLm6zAEI+brMAQiKu8wBGKupygE=");
    }};

    public Map<Integer, String> doSearch(String dirName) {
        keyword = keyword.trim();
        String[] parts = keyword.split("\\s+");
        if (parts.length > 1) {
            keyword = ArrayUtil.join(keyword.split("\\s+"), "+");
        }
        Map<Integer, String> pageContentMap = Maps.newLinkedHashMap();
        String searchUrl = SEARCH_URL + keyword;
        int firstPageNo = 1;
        String htmlContent = this.searchAndStoreResult(searchUrl, dirName, firstPageNo);
        pageContentMap.put(firstPageNo, htmlContent);
        Map<Integer, String> pageHrefMap = this.getPageHref(htmlContent);
        if (MapUtil.isNotEmpty(pageHrefMap)) {
            for (Map.Entry<Integer, String> entry : pageHrefMap.entrySet()) {
                Integer pageNo = entry.getKey();
                String href = entry.getValue();
                String pageContent = this.searchAndStoreResult(SITE_URL + href, dirName, pageNo);
                pageContentMap.put(pageNo, pageContent);
            }
        }
        return pageContentMap;
    }

    /**
     * 执行内容解析
     *
     * @param dirName
     * @param pageContentMap
     * @return
     */
    private List<ArticleInfo> parseContent(String dirName, Map<Integer, String> pageContentMap) {
        List<ArticleInfo> result = Lists.newArrayList();
        AbstractAdaptorManager abstractAdaptorManager = new AbstractAdaptorManager(dirName);
        for(Map.Entry<Integer, String> entry : pageContentMap.entrySet()) {
            List<ArticleInfo> pageArticleInfos = this.parseContent(abstractAdaptorManager, entry.getKey(), dirName, entry.getValue());
            result.addAll(pageArticleInfos);
        }
        return result;
    }

    /**
     * 内容解析
     *
     * @param abstractAdaptorManager
     * @param pageNo
     * @param dirName
     * @param pageContent
     * @return
     */
    private List<ArticleInfo> parseContent(AbstractAdaptorManager abstractAdaptorManager, int pageNo, String dirName, String pageContent) {
        List<ArticleInfo> result = Lists.newArrayList();
        Document document = Jsoup.parse(pageContent);
        Element mainElement = document.getElementById("gs_res_ccl_mid");
        Elements elements = mainElement.select("div[class='gs_r gs_or gs_scl']");
        Iterator<Element> iterator = elements.iterator();
        while (iterator.hasNext()) {
            Element item = iterator.next();
            String cid = item.attr("data-cid");
            Element aElement = item.getElementById(cid);
            String href = aElement.attr("href");
            AbstractAdaptor abstractAdaptor = abstractAdaptorManager.getAbstractAdaptor(href);
            ArticleInfo articleInfo = this.buildArtileInfo(abstractAdaptor, pageNo, dirName, cid, href);
            result.add(articleInfo);
        }
        return result;
    }



    /**
     * 构建文章信息
     *
     * @param abstractAdaptor
     * @param pageNo
     * @param dirName
     * @param cid
     * @param href
     * @return
     */
    private ArticleInfo buildArtileInfo(AbstractAdaptor abstractAdaptor, int pageNo, String dirName, String cid, String href) {
        ArticleInfo articleInfo = ArticleInfo.builder().cid(cid)
                .linkUrl(href)
                .build();
        //处理引用信息
        this.processQuoteInfo(articleInfo, pageNo, dirName, cid);
        //处理摘要信息
        this.processAbstractInfo(abstractAdaptor, articleInfo, href);
        return articleInfo;
    }

    /**
     * 处理摘要信息
     *
     * @param abstractAdaptor
     * @param articleInfo
     * @param href
     */
    private void processAbstractInfo(AbstractAdaptor abstractAdaptor, ArticleInfo articleInfo, String href) {
        if (Objects.isNull(abstractAdaptor)) {
            return;
        }
        controller.setProcessInfo(String.format(String.format("正在获取摘要信息, href: %s", href)));
        String abstractInfo = abstractAdaptor.getAbstractInfo(href);
        controller.setProcessInfo(String.format(String.format("获取摘要信息完成, href: %s", href)));
        if (StrUtil.isBlank(abstractInfo)) {
            return;
        }
        articleInfo.setAbstractInfo(abstractInfo);
    }

    /**
     * 执行搜索并存储搜索结果
     *
     * @param searchUrl
     * @param dirName
     * @param pageNo
     * @return
     */
    private String searchAndStoreResult(String searchUrl, String dirName, int pageNo) {
        String pageContent = this.getPageContent(dirName, pageNo);
        if (StrUtil.isNotBlank(pageContent)) {
            return pageContent;
        }
        SleepUtil.sleepRandomSeconds(3, 6);
        controller.setProcessInfo(String.format("正在爬取第%s页数据...", pageNo));
        String htmlContent = HttpRequestUtil.executGetHttpRequest(searchUrl, HEADERS);
        controller.setProcessInfo(String.format("爬取第%s页数据完成!", pageNo));
        if (StrUtil.isNotBlank(htmlContent)) {
            this.storeSearchHtmlContent(htmlContent, dirName, pageNo);
        }
        return htmlContent;
    }

    /**
     * 获取处理后的文件夹目录
     *
     * @return
     */
    private String getDirName() {
        return downloadDir + StrUtil.SLASH + keyword.trim().replaceAll("\\s+", "_")
                      .replaceAll("\\\\", "_")
                .replaceAll("\\/", "_")
                .replaceAll(":", "_")
                .replaceAll("\\*", "_")
                .replaceAll("\\?", "_")
                .replaceAll("\"", "_")
                .replaceAll("<", "_")
                .replaceAll(">", "_")
                .replaceAll("\\|", "_");
    }

    /**
     * 获取页面内容
     *
     * @param dirName
     * @param pageNo
     * @return
     */
    private String getPageContent(String dirName, int pageNo) {
        File file = new File(this.getPageHtmlName(dirName, pageNo));
        if (!file.exists()) {
            return null;
        }
        return FileUtil.readUtf8String(file);
    }

    /**
     * 存储
     * @param htmlContent
     * @param dirName
     * @param pageNo
     */
    private void storeSearchHtmlContent(String htmlContent, String dirName, int pageNo) {
        if (StrUtil.isBlank(htmlContent)) {
            return;
        }
        File file = new File(this.getPageHtmlName(dirName, pageNo));
        FileUtil.writeString(htmlContent, file, CharsetUtil.UTF_8);
    }

    /**
     * 获取html页面名称
     *
     * @param dirName
     * @param pageNo
     * @return
     */
    private String getPageHtmlName(String dirName, int pageNo) {
        return dirName + StrUtil.SLASH + String.format("page_%s.html", pageNo);
    }

    /**
     * 获取引用html文件
     *
     * @param dirName
     * @param pageNo
     * @param itemId
     * @return
     */
    private String getQuoteHtmlName(String dirName, int pageNo, String itemId) {
        return dirName + StrUtil.SLASH + String.format("quote_%s_%s.html", pageNo, itemId);
    }

    /**
     * 获取引用html文件内容
     *
     * @param dirName
     * @param pageNo
     * @param itemId
     * @return
     */
    private String getQuoteHtmlContent(String dirName, int pageNo, String itemId) {
        File file = new File(this.getQuoteHtmlName(dirName, pageNo, itemId));
        if (!file.exists()) {
            return null;
        }
        return FileUtil.readUtf8String(file);
    }

    /**
     * 存储引用html文件内容
     *
     * @param htmlContent
     * @param dirName
     * @param itemId
     * @param pageNo
     */
    private void storeQuoteHtmlContent(String htmlContent, String dirName, String itemId, int pageNo) {
        if (StrUtil.isBlank(htmlContent)) {
            return;
        }
        File file = new File(this.getQuoteHtmlName(dirName, pageNo, itemId));
        FileUtil.writeString(htmlContent, file, CharsetUtil.UTF_8);
    }

    /**
     * 获取分页链接信息
     *
     * @param pageContent
     * @return
     */
    private Map<Integer, String> getPageHref(String pageContent) {
        Map<Integer, String> result = Maps.newHashMap();
        Document document = Jsoup.parse(pageContent);
        Element pageElement = document.getElementById("gs_n");
        Elements aElements = pageElement.select("a[href^='/scholar']");
        Iterator<Element> aElementIterator = aElements.iterator();
        int pageNo = 2;
        while (aElementIterator.hasNext()) {
            Element aElement = aElementIterator.next();
            result.put(pageNo, aElement.attr("href"));
            if (pageNo == LIMIT_PAGE) {
                break;
            }
            pageNo ++;
        }
        return result;
    }

    /**
     * 处理引用相关信息
     *
     * @param articleInfo
     * @param pageNo
     * @param dirName
     * @param itemId
     */
    private void processQuoteInfo(ArticleInfo articleInfo, int pageNo, String dirName, String itemId) {
        String quoteHtmlContent = this.doQuoteSpide(pageNo, dirName, itemId);
        if (StrUtil.isBlank(quoteHtmlContent)) {
            return;
        }
        Document document = Jsoup.parse(quoteHtmlContent);
        Elements trElements = document.getElementsByTag("tr");
        Iterator<Element> iterator = trElements.iterator();
        while (iterator.hasNext()) {
            Element element = iterator.next();
            Elements apaElements = element.getElementsByTag("th").first().getElementsContainingText("APA");
            if (Objects.nonNull(apaElements) && apaElements.size() > 0) {
                Element contentElement = element.select("div[class=gs_citr]").first();
                String apaContent = contentElement.text();
                String[] parts = apaContent.split("\\(\\d+\\)\\.");
                if (parts.length == 2) {
                    String author = parts[0].trim();
                    String title = parts[1].trim();
                    String year = apaContent.replace(author, StrUtil.EMPTY)
                            .replace(title, StrUtil.EMPTY)
                            .replace("(", StrUtil.EMPTY)
                            .replace(").", StrUtil.EMPTY);
                    articleInfo.setAuthor(author).setTitle(title).setYear(year);
                }
                break;
            }
        }
    }

    /**
     * 爬取引用内容信息
     *
     * @param pageNo
     * @param dirName
     * @param itemId
     * @return
     */
    public String doQuoteSpide(int pageNo, String dirName, String itemId) {
        String quoteHtmlContent = this.getQuoteHtmlContent(dirName, pageNo, itemId);
        if (StrUtil.isNotBlank(quoteHtmlContent)) {
            return quoteHtmlContent;
        }
        SleepUtil.sleepRandomSeconds(2, 5);
        String quoteUrl = String.format(QUOTE_URL_TEMPLATE, itemId);
        String result = HttpRequestUtil.executGetHttpRequest(quoteUrl, HEADERS);
        this.storeQuoteHtmlContent(result, dirName, itemId, pageNo);
        return result;
    }
    
    //生成Excel
    private void doExcelGenerate(String dirName, List<ArticleInfo> articleInfos) {
        if (CollUtil.isEmpty(articleInfos)) {
            return;
        }
        try {
            articleInfos = articleInfos.stream().filter(item -> StrUtil.isNotEmpty(item.getTitle())).collect(Collectors.toList());
            File excelFile = new File(dirName + StrUtil.SLASH + "result.xlsx");
            ExportParams exportParams = new ExportParams();
            exportParams.setDataHandler(new ArticleInfoExcelDataHandler());
            exportParams.setStyle(ArticleInfoExcelExportStyler.class);
            exportParams.setHeight((short) 50);
            Workbook workbook = ExcelExportUtil.exportExcel(exportParams, ArticleInfo.class, articleInfos);
            FileOutputStream fos = new FileOutputStream(excelFile);
            workbook.write(fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 执行爬取任务
     *
     */
    public void doSpiderJob() {
        String dirName = this.getDirName();
        controller.setProcessInfo("开始执行关键字爬取任务, 请耐心等待...");
        Map<Integer, String> pageContentMap = this.doSearch(dirName);
        controller.setProcessInfo(String.format("关键字爬取任务完成, 共爬取到[%s]页信息", pageContentMap.size()));
        SleepUtil.sleepRandomSeconds(1,2);
        controller.setProcessInfo("开始执行页面解析以及摘要信息爬取任务...");
        List<ArticleInfo> articleInfos = this.parseContent(dirName, pageContentMap);
        controller.setProcessInfo(String.format("页面解析以及摘要信息爬取任务执行完毕, 涉及条目数量为[%s]个", articleInfos.size()));
        SleepUtil.sleepRandomSeconds(1,2);
        this.doExcelGenerate(dirName, articleInfos);
        controller.setProcessInfo("Excel文件整理完毕, 输出目录为: " + dirName);
    }

}
