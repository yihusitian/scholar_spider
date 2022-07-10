package com.yihusitian;

import cn.hutool.core.util.StrUtil;
import com.sun.org.apache.bcel.internal.generic.NEW;
import com.yihusitian.spider.GoogleScholarSpider;
import com.yihusitian.util.SleepUtil;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.Optional;

/**
 * @Description
 * @Author leeho
 * @Date 2022/7/9 下午9:30
 */
public class Controller {

    @FXML
    TextField keywordText;

    @FXML
    Button searchButton;

    @FXML
    Button configButton;

    @FXML
    Label searchLabel;

    @FXML
    Label configLabel;

    private DirectoryChooser directoryChooser;

    private static final String USER_DIR = System.getProperty("user.dir");

    private String downloadDir;

    /**
     * 初始化
     */
    public void initialize() {
        directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("选择下载路径");
        this.setDownloadDir(USER_DIR);
    }

    @FXML
    public void searchClick(MouseEvent event) {
        if (StrUtil.isBlank(keywordText.getText().trim())) {
            Alert warnAlert = new Alert(Alert.AlertType.WARNING);
            warnAlert.setContentText("请先输入关键字信息!");
            warnAlert.show();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("搜索任务确认");
        alert.setHeaderText(null);
        alert.setContentText("搜索任务需要花费一段时间，您确定要继续吗？");
        Optional<ButtonType> button = alert.showAndWait();
        if (button.get() == ButtonType.OK) {
            alert.close();
        } else {
            return;
        }
        new Thread(() -> {
            doSearch();
        }).start();
    }

    @FXML
    public void configClick(MouseEvent event) {
        File newFolder = directoryChooser.showDialog(configButton.getScene().getWindow());
        this.setDownloadDir(newFolder.getAbsolutePath());
    }

    /**
     * 执行搜索
     *
     **/
    private void doSearch() {
        searchButton.setDisable(true);
        String keyword = keywordText.getText().trim();
        try {
            String keywordDownloadDir = this.getKeywordDownloadDir(keyword);
            this.setProcessInfo(String.format("开始执行检索任务, 搜索关键词为: %s , 请您耐心等待", keyword));
            SleepUtil.sleepRandomSeconds(1, 2);
            GoogleScholarSpider googleScholarSpider = new GoogleScholarSpider(this, keywordDownloadDir, keyword);
            googleScholarSpider.doSpider(keyword);
            this.setProcessInfo(String.format("检索任务完成, 请查看你的excel文件, 路径为: %s", keywordDownloadDir));
        } catch (Exception e) {
            this.setProcessInfo("搜索过程中出错了，快来找人看看吧！");
        }
        searchButton.setDisable(false);
    }

    /**
     * 获取处理后的文件夹目录
     *
     * @param keyword
     * @return
     */
    private String getKeywordDownloadDir(String keyword) {
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
     * 输出进度信息
     *
     * @param content
     */
    public void setProcessInfo(String content) {
        Platform.runLater(() -> {
            searchLabel.setText(content);
        });
    }

    /**
     * 设置下载路径
     *
     * @param dir
     */
    private void setDownloadDir(String dir) {
        this.downloadDir = dir;
        configLabel.setText(dir);
    }

}
