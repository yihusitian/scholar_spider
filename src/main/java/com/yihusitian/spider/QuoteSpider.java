package com.yihusitian.spider;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import com.yihusitian.Controller;
import com.yihusitian.bean.ArticleInfo;
import com.yihusitian.util.HttpRequestUtil;
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
        this.put("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/103.0.0.0 Safari/537.36");
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
        SleepUtil.sleepRandomSeconds(1, 3);
        String quoteUrl = String.format(QUOTE_URL_TEMPLATE, itemId);
        print(String.format("开始爬取引用, qutoeUrl: %s", quoteUrl));
        String result = HttpRequestUtil.executGetHttpRequest(quoteUrl, HEADERS);
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
