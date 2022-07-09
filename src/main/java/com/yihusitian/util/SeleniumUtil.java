package com.yihusitian.util;

import cn.hutool.core.io.FileUtil;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description
 * @Author leeho
 * @Date 2022/7/9 上午10:13
 */
public class SeleniumUtil {

    private static final String DRIVER_PATH = FileUtil.getAbsolutePath("chromedriver");

    private static ChromeDriver webDriver = null;

    static {
        System.setProperty("webdriver.chrome.driver", DRIVER_PATH);
        ChromeOptions options = new ChromeOptions();
//        options.addArguments("--start-maximized"); // 启动时自动最大化窗口
//        options.addArguments("--disable-popup-blocking"); // 禁用阻止弹出窗口
//        options.addArguments("no-sandbox"); // 启动无沙盒模式运行
//        options.addArguments("disable-extensions"); // 禁用扩展
//        options.addArguments("no-default-browser-check"); // 默认浏览器检查
//        options.setHeadless(Boolean.TRUE);//设置chrome 无头模式
//        options.addArguments("--headless");//不用打开图形界面。
//        options.addArguments("--disable-blink-features=AutomationControlled");
//        options.setExperimentalOption("excludeSwitches", Lists.newArrayList("enable-automation"));
        webDriver = new ChromeDriver();
//        String script = "object.defineProperty(navigator,'webdriver',{undefinedget: () => undefined})";
        Map<String,Object> command = new HashMap<>();
        command.put("source","Object.defineProperty(navigator, 'webdriver', {get: () => undefined})");
        webDriver.executeCdpCommand("Page.addScriptToEvaluateOnNewDocument",command);
    }

    /**
     * 获取web驱动
     *
     * @return
     */
    public static WebDriver getWebDriver() {
        return webDriver;
    }

    /**
     * 打开谷歌浏览器
     *
     * @param url
     * @return
     */
    public static WebDriver openChromeWithUrl(String url) {
        webDriver.get(url);
        return webDriver;
    }

}
