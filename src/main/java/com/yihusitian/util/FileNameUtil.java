package com.yihusitian.util;

/**
 * @Description
 * @Author leeho
 * @Date 2022/7/10 下午3:25
 */
public class FileNameUtil {

    /**
     * 去除特殊字符以后的文件名称
     *
     * @param name
     * @return
     */
    public static String handle(String name) {
        return name.trim().replaceAll("\\s+", "_")
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
}
