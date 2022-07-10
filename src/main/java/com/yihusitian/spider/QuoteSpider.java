package com.yihusitian.spider;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import com.yihusitian.Controller;
import com.yihusitian.bean.ArticleInfo;
import com.yihusitian.util.HttpsRequestUtil;
import com.yihusitian.util.SleepUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.util.*;
import java.util.concurrent.*;

/**
 * @Description 引用信息爬虫
 * @Author leeho
 * @Date 2022/7/10 上午11:12
 */
public class QuoteSpider implements Callable<Map<String, ArticleInfo>> {

    private static final int THREADS_SIZE = 10;

    private static final String QUOTE_URL_TEMPLATE = "https://scholar.google.com/scholar?q=info:%s:scholar.google.com/&output=cite&scirp=1&hl=zh-CN";

    private static final Map<String, String> HEADERS = new HashMap<String, String >() {{
        this.put("authority","scholar.google.com");
        this.put("accept","*/*");
        this.put("accept-language","zh-CN,zh;q=0.9");
        this.put("cookie","CONSENT=YES+AE.zh-CN+20180429-14-0; ANID=AHWqTUmUca29_XCMSFI6Uov14lF7BMnyKVNpYPM3UqkGC01V8oOGOMoJl-tmTus7; SEARCH_SAMESITE=CgQI55UB; SID=MAgZYTxRjS7RiL1Vz4s0qwmhwa_4F7YYwERm_T9u76SDR76IsRmK1xO6C50bP9lF_bG5TA.; __Secure-1PSID=MAgZYTxRjS7RiL1Vz4s0qwmhwa_4F7YYwERm_T9u76SDR76Iw8BSGf5C1zwY_fjYALrEyQ.; __Secure-3PSID=MAgZYTxRjS7RiL1Vz4s0qwmhwa_4F7YYwERm_T9u76SDR76IfxQyn1oSRO5Gf6YD68k1AQ.; HSID=A9HO-cZp4HYVPFUbN; SSID=AWlDDtd-oRBMff_ZX; APISID=WRKMqM6Zs0lskhuV/AbzNT6JiLuzsbwYLj; SAPISID=7kHWHSwn9LyR8zJK/AeM09kM3Mpbi6QiK5; __Secure-1PAPISID=7kHWHSwn9LyR8zJK/AeM09kM3Mpbi6QiK5; __Secure-3PAPISID=7kHWHSwn9LyR8zJK/AeM09kM3Mpbi6QiK5; 1P_JAR=2022-07-09-14; AEC=AakniGPRg-n32x56KEAQ8QNMYBz2AiXEp1bGhE40IyH78QABw1ysPNz2RhQ; NID=511=qWIG2ad653MlxwWIvQGOe3QG0nW7FYLT0fLFyzqYhO3-QN8vXtWPb8z3kLfA5LzSTPg4m9M-LkhL8Hvh2dOuyO9kToH8FzrM_7yRb9Y5yc9-cMS0ieC-unQt3S49_sP_7Wm8C8nYUl-bL7N_RrRc-Ahi1Wl_1KCxY-gUuUqpWcDffB0yxDiu0rcGQFYgIES_oxm6-fJtVSQW1Oc6tt0d8SeDgh_FauEzhQDAg2yeP3D2WrMlZjxZUl9lnmWLAk7kwhs9r1R7gukr43rQB6sD5X1NDo16IvuSq8xQ8BlwMlcfsnuWUwljolp4K-aYmRXLsHKvCRMIzMcHIiuYy0QzBD7494I; GSP=IN=8b9a455bd1c58d67:LD=zh-CN:A=SmWP-Q:CPTS=1657437234:LM=1657437234:S=HzVrx8heSPBHdDN3; SIDCC=AJi4QfHJDIJ_6CMEjx1uy0YRt4-jZCUDgKjqBDqk6M9p6EHRG3fEF1tcZ30DGs3WYPghGHDr-g; __Secure-1PSIDCC=AJi4QfEDnu-zLwBERL0zu6RIEKJBgmDyUwzxSq6w8iY0K4G6fYnBDXaaPR2IG13QS21W3eT_kQ; __Secure-3PSIDCC=AJi4QfEOdp1sHQw0bG6zP1pr0wdFjwBkUwOpNEJkYikr1HikegPZ-S7Onma7kRZXD7JFW4OJ_v0");
        this.put("referer","https://scholar.google.com/scholar?q=master+doctor+depression+anxiety&hl=zh-CN&as_sdt=0,5");
        this.put("sec-ch-ua","\".Not/A)Brand\";v=\"99\", \"Google Chrome\";v=\"103\", \"Chromium\";v=\"103\"");
        this.put("sec-ch-ua-mobile","?0");
        this.put("sec-ch-ua-platform","macOS");
        this.put("sec-fetch-dest","empty");
        this.put("sec-fetch-mode","cors");
        this.put("sec-fetch-site","same-origin");
        this.put("user-agent","Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/103.0.0.0 Safari/537.36");
        this.put("x-client-data","CLK1yQEIlbbJAQimtskBCMS2yQEIqZ3KAQjghcsBCJOhywEI2+/LAQjpucwBCLm6zAEI+brMAQiKu8wBGKupygE=");
        this.put("x-requested-with","XHR");
    }};

    private String dirName;

    private List<ArticleInfo> articleInfos;

    private Controller controller;

    public QuoteSpider(Controller controller, String dirName, List<ArticleInfo> articleInfos) {
        this.controller = controller;
        this.dirName = dirName;
        this.articleInfos = articleInfos;
    }

    @Override
    public Map<String, ArticleInfo> call() throws Exception {
        CountDownLatch countDownLatch = new CountDownLatch(THREADS_SIZE);
        try {
            //result中构建新的ArticleInfo对象
            Map<String, ArticleInfo> result = new ConcurrentHashMap<>();
            print("开始执行引用爬取任务...");
            //使用非阻塞队列
            ConcurrentLinkedQueue<ArticleInfo> articleInfoQueue = new ConcurrentLinkedQueue<>(articleInfos);
            ExecutorService executorService = Executors.newFixedThreadPool(THREADS_SIZE);
            for (int i = 0; i < THREADS_SIZE; i ++) {
                executorService.submit(new QuoteTask(countDownLatch, articleInfoQueue, result));
            }
            countDownLatch.await();
            executorService.shutdown();
            print(String.format("引用爬取任务执行完毕，一共爬取引用%s条", articleInfos.size()));
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    class QuoteTask implements Runnable {

        private CountDownLatch countDownLatch;

        private ConcurrentLinkedQueue<ArticleInfo> articleInfoQueue;

        private Map<String, ArticleInfo> map;

        public QuoteTask(CountDownLatch countDownLatch, ConcurrentLinkedQueue<ArticleInfo> articleInfoQueue, Map<String, ArticleInfo> map) {
            this.countDownLatch = countDownLatch;
            this.articleInfoQueue = articleInfoQueue;
            this.map = map;
        }

        @Override
        public void run() {
            ArticleInfo articleInfo;
            int count = 0;
            while ((articleInfo = articleInfoQueue.poll()) != null) {
                ArticleInfo newArticeInfo = processQuoteInfoForNewArticeInfo(articleInfo);
                count ++;
                if (Objects.nonNull(newArticeInfo)) {
                    map.put(articleInfo.getCid(), newArticeInfo);
                }
            }
            this.countDownLatch.countDown();
        }
    }

    /**
     * 处理引用相关信息
     *
     * @param articleInfo
     */
    private ArticleInfo processQuoteInfoForNewArticeInfo(ArticleInfo articleInfo) {
        String quoteHtmlContent = this.doQuoteSpide(articleInfo);
        if (StrUtil.isBlank(quoteHtmlContent)) {
            return null;
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
                    ArticleInfo result = ArticleInfo.builder().cid(articleInfo.getCid()).title(title).author(author).year(year).pageNo(articleInfo.getPageNo()).build();
                    return result;
                }
            }
        }
        return null;
    }

    /**
     * 爬取引用内容信息
     *
     * @param articleInfo
     * @return
     */
    public String doQuoteSpide(ArticleInfo articleInfo) {
        int pageNo = articleInfo.getPageNo();
        String itemId = articleInfo.getCid();
        String quoteHtmlContent = this.getQuoteHtmlContent(pageNo, itemId);
        if (StrUtil.isNotBlank(quoteHtmlContent)) {
            return quoteHtmlContent;
        }
        SleepUtil.sleepRandomSeconds(3, 6);
        String quoteUrl = String.format(QUOTE_URL_TEMPLATE, itemId);
        print(String.format("开始爬取引用, qutoeUrl: %s", quoteUrl));
        String result = HttpsRequestUtil.doGet(quoteUrl, HEADERS);
        print(String.format("爬取引用完成, qutoeUrl: %s", quoteUrl));
        this.storeQuoteHtmlContent(result, itemId, pageNo);
        return result;
    }

    /**
     * 存储引用html文件内容
     *
     * @param htmlContent
     * @param itemId
     * @param pageNo
     */
    private void storeQuoteHtmlContent(String htmlContent, String itemId, int pageNo) {
        if (StrUtil.isBlank(htmlContent)) {
            return;
        }
        File file = new File(this.getQuoteHtmlName(pageNo, itemId));
        FileUtil.writeString(htmlContent, file, CharsetUtil.UTF_8);
    }


    /**
     * 获取引用html文件内容
     *
     * @param pageNo
     * @param itemId
     * @return
     */
    private String getQuoteHtmlContent(int pageNo, String itemId) {
        File file = new File(this.getQuoteHtmlName(pageNo, itemId));
        if (!file.exists()) {
            return null;
        }
        return FileUtil.readUtf8String(file);
    }

    /**
     * 获取引用html文件
     *
     * @param pageNo
     * @param itemId
     * @return
     */
    private String getQuoteHtmlName(int pageNo, String itemId) {
        return dirName + StrUtil.SLASH + String.format("quote_%s_%s.html", pageNo, itemId);
    }

    private void print(String content) {
        controller.setProcessInfo(content);
    }

}
