package com.yihusitian.spider;

import com.yihusitian.Controller;
import com.yihusitian.abstracts.AbstractAdaptor;
import com.yihusitian.abstracts.AbstractAdaptorManager;
import com.yihusitian.bean.ArticleInfo;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.*;

/**
 * @Description 摘要信息爬虫
 * @Author leeho
 * @Date 2022/7/10 上午11:12
 */
public class AbstractInfoSpider implements Callable<Map<String, String>> {

    private String dirName;

    private List<ArticleInfo> articleInfos;

    private AbstractAdaptorManager abstractAdaptorManager;

    private static final int THREADS_SIZE = 10;

    private Controller controller;

    public AbstractInfoSpider(Controller controller, String dirName, List<ArticleInfo> articleInfos) {
        this.controller = controller;
        this.dirName = dirName;
        this.articleInfos = articleInfos;
        abstractAdaptorManager = new AbstractAdaptorManager(dirName);
    }

    /**
     * 处理摘要信息
     *
     * @param abstractAdaptor
     * @param href
     */
    private String getAbstractInfo(AbstractAdaptor abstractAdaptor, String href) {
        if (Objects.isNull(abstractAdaptor)) {
            return "暂未适配该网站数据抓取";
        }
        return abstractAdaptor.getAbstractInfo(href);
    }

    @Override
    public Map<String, String> call() throws Exception {
        CountDownLatch countDownLatch = new CountDownLatch(THREADS_SIZE);
        try {
            print("开始执行摘要信息爬取任务...");
            //result中构建新的ArticleInfo对象
            Map<String, String> result = new ConcurrentHashMap<>();
            //使用非阻塞队列
            ConcurrentLinkedQueue<ArticleInfo> articleInfoQueue = new ConcurrentLinkedQueue<>(articleInfos);
            ExecutorService executorService = Executors.newFixedThreadPool(THREADS_SIZE);
            for (int i = 0; i < THREADS_SIZE; i ++) {
                executorService.submit(new AbstractTask(countDownLatch, articleInfoQueue, result));
            }
            countDownLatch.await();
            executorService.shutdown();
            print("摘要信息爬取任务执行完毕...");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    class AbstractTask implements Runnable {

        private CountDownLatch countDownLatch;

        private ConcurrentLinkedQueue<ArticleInfo> articleInfoQueue;

        private Map<String, String> map;

        public AbstractTask(CountDownLatch countDownLatch, ConcurrentLinkedQueue<ArticleInfo> articleInfoQueue, Map<String, String> map) {
            this.countDownLatch = countDownLatch;
            this.articleInfoQueue = articleInfoQueue;
            this.map = map;
        }

        @Override
        public void run() {
            ArticleInfo articleInfo;
            int count = 0;
            while ((articleInfo = articleInfoQueue.poll()) != null) {
                String href = articleInfo.getLinkUrl();
                AbstractAdaptor abstractAdaptor = abstractAdaptorManager.getAbstractAdaptor(href);
                print("开始爬取摘要信息, href: " + href);
                String abstractInfo = getAbstractInfo(abstractAdaptor, href);
                print("爬取摘要信息完成, href: " + href);
                map.put(articleInfo.getCid(), abstractInfo);
                count ++;
            }
            this.countDownLatch.countDown();
        }
    }

    private void print(String content) {
        controller.setProcessInfo(content);
    }
}
