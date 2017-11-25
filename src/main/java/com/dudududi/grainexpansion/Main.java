package com.dudududi.grainexpansion;

import com.dudududi.grainexpansion.controller.*;
import com.dudududi.grainexpansion.model.CellAutomaton;
import com.dudududi.grainexpansion.model.cells.Board;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;

public class Main extends Application {
    private static final int WIDTH = 900;
    private static final int HEIGHT = 650;
    private Stage primaryStage;
    private CellAutomaton cellAutomaton;
    private RootController rootController;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.cellAutomaton = new CellAutomaton(new Board(SpaceGeneratorController.DEFAULT_WIDTH,
                SpaceGeneratorController.DEFAULT_HEIGHT, SpaceGeneratorController.DEFAULT_PERIODICITY));
        this.rootController = new RootController(cellAutomaton);
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
        if (param == AnimationController.class) {
            return new AnimationController(cellAutomaton);
        } else if (param == BoardController.class) {
            return new BoardController(cellAutomaton);
        } else if (param == CellularAutomatonController.class) {
            return new CellularAutomatonController(cellAutomaton);
        } else if (param == InclusionsController.class) {
            return new InclusionsController(cellAutomaton);
        } else if (param == NucleonsController.class) {
            return new NucleonsController(cellAutomaton);
        } else if (param == RootController.class) {
            return rootController;
        } else if (param == SpaceGeneratorController.class) {
            return new SpaceGeneratorController(cellAutomaton);
        } else if (param == StructuresController.class) {
            return new StructuresController(cellAutomaton);
        } else {
            // unsupported controller
            throw new UnsupportedOperationException("Cannot instantiate " + param.getCanonicalName());
        }
    }
}
