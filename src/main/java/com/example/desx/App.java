package com.example.desx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        BorderPane root = new BorderPane();
        View view = new View();
        Scene scene = new Scene(root, 800, 600);
        VBox aggregation = new VBox();
        aggregation.getChildren().addAll(view.setKeyArea(), view.setMessageArea(), view.setResultArea(), view.setFileArea());


        root.setCenter(aggregation);


        stage.setTitle("DESX");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}