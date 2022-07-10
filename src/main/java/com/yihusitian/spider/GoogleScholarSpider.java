package com.yihusitian.spider;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yihusitian.Controller;
import com.yihusitian.bean.ArticleInfo;
import com.yihusitian.excel.ArticleInfoExcelDataHandler;
import com.yihusitian.excel.ArticleInfoExcelExportStyler;
import com.yihusitian.util.HttpsRequestUtil;
import com.yihusitian.util.SleepUtil;
import org.apache.poi.ss.usermodel.Workbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileOutputStream;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * @Description
 * @Author leeho
 * @Date 2022/7/7 下午10:06
 */
public class GoogleScholarSpider {

    private Controller controller;

    private String keyword;

    private String downloadDir;

    public GoogleScholarSpider(Controller controller, String downloadDir, String keyword) {
        this.controller = controller;
        this.keyword = keyword;
        this.downloadDir = downloadDir;
    }


    private static final String SITE_URL = "https://scholar.google.com";

    private static final String SEARCH_URL = SITE_URL + "/scholar?hl=zh-CN&as_sdt=0,5&q=";

    //一个关键字最多抓取前10页，后面的参考价值也不大了
    private static final int LIMIT_PAGE = 15;

    private static final Map<String, String> HEADERS = new HashMap<String, String >() {{
        this.put("authority","scholar.google.com");
        this.put("accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        this.put("accept-language","zh-CN,zh;q=0.9");
        this.put("cookie","CONSENT=YES+AE.zh-CN+20180429-14-0; ANID=AHWqTUmUca29_XCMSFI6Uov14lF7BMnyKVNpYPM3UqkGC01V8oOGOMoJl-tmTus7; SEARCH_SAMESITE=CgQI55UB; SID=MAgZYTxRjS7RiL1Vz4s0qwmhwa_4F7YYwERm_T9u76SDR76IsRmK1xO6C50bP9lF_bG5TA.; __Secure-1PSID=MAgZYTxRjS7RiL1Vz4s0qwmhwa_4F7YYwERm_T9u76SDR76Iw8BSGf5C1zwY_fjYALrEyQ.; __Secure-3PSID=MAgZYTxRjS7RiL1Vz4s0qwmhwa_4F7YYwERm_T9u76SDR76IfxQyn1oSRO5Gf6YD68k1AQ.; HSID=A9HO-cZp4HYVPFUbN; SSID=AWlDDtd-oRBMff_ZX; APISID=WRKMqM6Zs0lskhuV/AbzNT6JiLuzsbwYLj; SAPISID=7kHWHSwn9LyR8zJK/AeM09kM3Mpbi6QiK5; __Secure-1PAPISID=7kHWHSwn9LyR8zJK/AeM09kM3Mpbi6QiK5; __Secure-3PAPISID=7kHWHSwn9LyR8zJK/AeM09kM3Mpbi6QiK5; GSP=IN=8b9a455bd1c58d67:LD=zh-CN:LM=1657202142:S=glRFE3rjANsOGxkE; 1P_JAR=2022-07-09-14; AEC=AakniGPRg-n32x56KEAQ8QNMYBz2AiXEp1bGhE40IyH78QABw1ysPNz2RhQ; NID=511=qWIG2ad653MlxwWIvQGOe3QG0nW7FYLT0fLFyzqYhO3-QN8vXtWPb8z3kLfA5LzSTPg4m9M-LkhL8Hvh2dOuyO9kToH8FzrM_7yRb9Y5yc9-cMS0ieC-unQt3S49_sP_7Wm8C8nYUl-bL7N_RrRc-Ahi1Wl_1KCxY-gUuUqpWcDffB0yxDiu0rcGQFYgIES_oxm6-fJtVSQW1Oc6tt0d8SeDgh_FauEzhQDAg2yeP3D2WrMlZjxZUl9lnmWLAk7kwhs9r1R7gukr43rQB6sD5X1NDo16IvuSq8xQ8BlwMlcfsnuWUwljolp4K-aYmRXLsHKvCRMIzMcHIiuYy0QzBD7494I; SIDCC=AJi4QfHECBTugz_v3_j-6McjEqfPDR108Ge0aoGLqWL0vyNlq62cj-bF49n3K2j4E5xiNjSWkA; __Secure-1PSIDCC=AJi4QfEWgH4Yql36-0SXz2iRTjiZdtSyBx6A6avVIg-hDueJ8j1l1ZlFb1wtaX4Th0MAucQhQw; __Secure-3PSIDCC=AJi4QfFFE_56VaRCobyscW_OacTG7R3YGdYEJ8Xvi4VNuK95_MMRVbn-Lic48mhd7WgaMby_BgI");
        this.put("referer","https://scholar.google.com/scholar?hl=zh-CN&as_sdt=0,5&q=master+doctor+depression+anxiety");
        this.put("sec-ch-ua","\".Not/A)Brand\";v=\"99\", \"Google Chrome\";v=\"103\", \"Chromium\";v=\"103\"");
        this.put("sec-ch-ua-mobile","?0");
        this.put("sec-ch-ua-platform","macOS");
        this.put("sec-fetch-dest","document");
        this.put("sec-fetch-mode","navigate");
        this.put("sec-fetch-site","same-origin");
        this.put("sec-fetch-user","?1");
        this.put("upgrade-insecure-requests","1");
        this.put("user-agent","Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/103.0.0.0 Safari/537.36");
        this.put("x-client-data","CLK1yQEIlbbJAQimtskBCMS2yQEIqZ3KAQjghcsBCJOhywEI2+/LAQjpucwBCLm6zAEI+brMAQiKu8wBGKupygE=");    }};

    public List<ArticleInfo> doSearch(String keyword) {
        keyword = keyword.trim();
        String[] parts = keyword.split("\\s+");
        if (parts.length > 1) {
            keyword = ArrayUtil.join(keyword.split("\\s+"), "+");
        }
        Map<Integer, String> pageContentMap = Maps.newLinkedHashMap();
        String pageHref = SEARCH_URL + keyword;
        int currentPageNo = 1;
        String htmlContent = null;
        while (currentPageNo <= LIMIT_PAGE) {
            htmlContent = this.searchAndStoreResult(pageHref, currentPageNo);
            if (StrUtil.isEmpty(htmlContent)) {
                System.out.println(String.format("第%s页未搜索到数据", currentPageNo));
                break;
            }
            pageContentMap.put(currentPageNo, htmlContent);
            pageHref = this.getNextPageHref(currentPageNo, htmlContent);
            if (StrUtil.isEmpty(pageHref)) {
                System.out.println("已经没有下一页可搜索数据了");
                break;
            }
            currentPageNo ++;
        }
        return this.parseCidAndHref(pageContentMap);
    }

    /**
     * 解析cid和href信息
     *
     * @param pageContentMap
     * @return
     */
    private List<ArticleInfo> parseCidAndHref(Map<Integer, String> pageContentMap) {
        List<ArticleInfo> result = Lists.newArrayList();
        for (Map.Entry<Integer, String> entry : pageContentMap.entrySet()) {
            List<ArticleInfo> pageArticleInfos = this.parseCidAndHref(entry.getKey(), entry.getValue());
            result.addAll(pageArticleInfos);
        }
        return result;
    }

    /**
     * 解析cid和href信息
     *
     * @param pageNo
     * @param pageContent
     * @return
     */
    private List<ArticleInfo> parseCidAndHref(int pageNo, String pageContent) {
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
            ArticleInfo articleInfo = ArticleInfo.builder().cid(cid).pageNo(pageNo).linkUrl(href).build();
            result.add(articleInfo);
        }
        return result;
    }

    /**
     * 执行搜索并存储搜索结果
     *
     * @param searchUrl
     * @param pageNo
     * @return
     */
    private String searchAndStoreResult(String searchUrl, int pageNo) {
        String pageContent = this.getPageContent(pageNo);
        if (StrUtil.isNotBlank(pageContent)) {
            return pageContent;
        }
        SleepUtil.sleepRandomSeconds(3, 6);
//        String htmlContent = HttpRequestUtil.executGetHttpRequest(searchUrl, HEADERS);
        controller.setProcessInfo(String.format("正在爬取第%s页, searchUrl: %s", pageNo, searchUrl));
        String htmlContent = HttpsRequestUtil.doGet(searchUrl, HEADERS);
        controller.setProcessInfo(String.format("爬取第%s页完成, searchUrl: %s", pageNo, searchUrl));
        if (StrUtil.isNotBlank(htmlContent)) {
            this.storeSearchHtmlContent(htmlContent, pageNo);
        }
        return htmlContent;
    }

    /**
     * 获取页面内容
     *
     * @param pageNo
     * @return
     */
    private String getPageContent(int pageNo) {
        File file = new File(this.getPageHtmlName(pageNo));
        if (!file.exists()) {
            return null;
        }
        return FileUtil.readUtf8String(file);
    }

    /**
     * 存储
     * @param htmlContent
     * @param pageNo
     */
    private void storeSearchHtmlContent(String htmlContent, int pageNo) {
        if (StrUtil.isBlank(htmlContent)) {
            return;
        }
        File file = new File(this.getPageHtmlName(pageNo));
        FileUtil.writeString(htmlContent, file, CharsetUtil.UTF_8);
    }

    /**
     * 获取html页面名称
     *
     * @param pageNo
     * @return
     */
    private String getPageHtmlName(int pageNo) {
        return downloadDir + StrUtil.SLASH + String.format("page_%s.html", pageNo);
    }

    private String getNextPageHref(int currentPageNo, String htmlContent) {
        int nextPageNO = currentPageNo + 1;
        Document document = Jsoup.parse(htmlContent);
        Element pageElement = document.getElementById("gs_n");
        Elements aElements = pageElement.select("a[href^='/scholar']");
        Iterator<Element> aElementIterator = aElements.iterator();
        while (aElementIterator.hasNext()) {
            Element aElement = aElementIterator.next();
            String pageNoStr = aElement.text();
            if (pageNoStr.equals(String.valueOf(nextPageNO))) {
                return SITE_URL + aElement.attr("href");
            }
        }
        return null;
    }

    //生成Excel
    private void doExcelGenerate(List<ArticleInfo> articleInfos) {
        if (CollUtil.isEmpty(articleInfos)) {
            return;
        }
        try {
            articleInfos = articleInfos.stream().filter(item -> StrUtil.isNotEmpty(item.getTitle())).collect(Collectors.toList());
            File excelFile = new File(downloadDir + StrUtil.SLASH + "result.xlsx");
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

    public void doSpider(String keyword) throws Exception{
        controller.setProcessInfo("开始执行关键字爬取任务, 请耐心等待...");
        List<ArticleInfo> articleInfos = this.doSearch(keyword);
        controller.setProcessInfo(String.format("关键字爬取任务完成, 共爬取到[%s]条信息", articleInfos.size()));
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        //获取引用信息
        Future<Map<String, ArticleInfo>> quoteFuture = executorService.submit(new QuoteSpider(controller, downloadDir, articleInfos));
        //获取摘要信息
        Future<Map<String, String>> abstractFuture = executorService.submit(new AbstractInfoSpider(controller, downloadDir, articleInfos));
        Map<String, ArticleInfo> quoteArticleInfoMap = quoteFuture.get();
        controller.setProcessInfo(String.format("引用信息爬取任务完成, 共爬取到[%s]条信息", quoteArticleInfoMap.size()));
        Map<String, String> abstractInfoMap = abstractFuture.get();
        controller.setProcessInfo(String.format("摘要信息爬取任务完成, 共爬取到[%s]条信息", abstractInfoMap.size()));
        executorService.shutdown();
        articleInfos.forEach(articleInfo -> {
            String cid = articleInfo.getCid();
            ArticleInfo quoteArticleInfo = quoteArticleInfoMap.get(cid);
            if (Objects.nonNull(quoteArticleInfo)) {
                articleInfo.setAuthor(quoteArticleInfo.getAuthor())
                        .setTitle(quoteArticleInfo.getTitle())
                        .setYear(quoteArticleInfo.getYear());
            }
            articleInfo.setAbstractInfo(abstractInfoMap.get(cid));
        });
        this.doExcelGenerate(articleInfos);
        controller.setProcessInfo("Excel文件整理完毕, 输出目录为: " + downloadDir);
    }

}
