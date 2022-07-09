package com.yihusitian.util;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * @Description
 * @Author LeeHo
 * @Date 2022/6/27 16:43
 */
public class SleepUtil {

    /**
     * 当前线程随机停几秒
     *
     */
    public static void sleepRandomSeconds(int origin, int bound) {
        try {
            int sleepSeconds = ThreadLocalRandom.current().nextInt(origin, bound);
            System.out.println(String.format("线程停止%s秒", sleepSeconds));
            TimeUnit.SECONDS.sleep(sleepSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}