package com.yihusitian;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Hello world!
 *
 */
public class App extends Application {

    @Override
    public void init() throws Exception {
        super.init();
    }

    public static void main(String[] args ) {
        App.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(App.class.getClassLoader().getResourceAsStream("google_spider.fxml"));
        Scene scene = new Scene(root);

        primaryStage.setTitle("谷歌学术检索工具");
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
