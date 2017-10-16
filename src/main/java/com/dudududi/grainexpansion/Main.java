package com.dudududi.grainexpansion;

import com.dudududi.grainexpansion.controller.RootController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    private Stage primaryStage;
    private Parent root;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Static recrystallization");

        initRoot();
    }

    private void initRoot() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getClassLoader().getResource("layout/root.fxml"));
        this.root = loader.load();
        RootController controller = loader.getController();
        controller.setStage(primaryStage);
        this.primaryStage.setScene(new Scene(root, 800, 600));
        this.primaryStage.show();
    }
}
