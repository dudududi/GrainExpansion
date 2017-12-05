package com.dudududi.grainexpansion;

import com.dudududi.grainexpansion.controller.*;
import com.dudududi.grainexpansion.model.SimulationModel;
import com.dudududi.grainexpansion.model.models.CellAutomaton;
import com.dudududi.grainexpansion.model.models.MonteCarlo;
import com.dudududi.grainexpansion.model.cells.Board;
import com.dudududi.grainexpansion.model.definables.neighbourhood.MooreNeighbourhood;
import com.dudududi.grainexpansion.model.definables.neighbourhood.NeighbourhoodType;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class Main extends Application {
    private static final int WIDTH = 900;
    private static final int HEIGHT = 650;
    private Stage primaryStage;
    private SimulationModel simulationModel;
    private RootController rootController;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Board board = new Board(SpaceGeneratorController.DEFAULT_WIDTH,
                SpaceGeneratorController.DEFAULT_HEIGHT, SpaceGeneratorController.DEFAULT_PERIODICITY);
        this.simulationModel = new SimulationModel(board);
        this.rootController = new RootController(simulationModel);
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Grain Expansion");

        initRoot();
    }

    private void initRoot() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setControllerFactory(this::dealControllers);

        loader.setLocation(Main.class.getClassLoader().getResource("layout/root_layout.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root, WIDTH, HEIGHT);
        this.primaryStage.setScene(scene);
        this.rootController.setScene(scene);
        this.primaryStage.show();
    }

    private Object dealControllers(Class<?> param) {
        if (param == RootController.class) {
            return rootController;
        } else {
            try {
                return param.getDeclaredConstructor(SimulationModel.class).newInstance(simulationModel);
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }
    }
}
