package com.dudududi.grainexpansion.controller;

import com.dudududi.grainexpansion.model.CellAutomaton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by dudek on 10/22/17.
 */
public class RootController {
    private static final String CSV_FORMAT_EXTENSION = "*.csv";
    private static final String CSV_FORMAT_DESCRIPTION = "CSV file";
    private static final String DEFAULT_IMAGE_FORMAT = "png";
    private static final String PNG_FORMAT_EXTENSION = "*." + DEFAULT_IMAGE_FORMAT;
    private static final String PNG_FORMAT_DESCRIPTION = "PNG file";

    private FileChooser fileChooser;
    private Scene scene;

    @FXML
    private MenuItem importButton;

    @FXML
    private MenuItem exportButton;

    // Controllers list
    @FXML
    private AnimationController animationController;

    @FXML
    private BoardController boardController;

    @FXML
    private InclusionsController inclusionsController;

    @FXML
    private NucleonsController nucleonsController;

    @FXML
    private SpaceGeneratorController spaceGeneratorController;

    private CellAutomaton cellAutomaton;
    private List<Controller> controllerList;

    public RootController(CellAutomaton cellAutomaton) {
        this.cellAutomaton = cellAutomaton;
        this.fileChooser = new FileChooser();
        this.controllerList = new ArrayList<>();

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter(CSV_FORMAT_DESCRIPTION, CSV_FORMAT_EXTENSION),
                new FileChooser.ExtensionFilter(PNG_FORMAT_DESCRIPTION, PNG_FORMAT_EXTENSION));
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
    }

    @FXML
    private void initialize() {
        importButton.setOnAction(this::importButtonClicked);
        exportButton.setOnAction(this::exportButtonClicked);

        controllerList.addAll(Arrays.asList(
                animationController,
                boardController,
                inclusionsController,
                nucleonsController,
                spaceGeneratorController));
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    @SuppressWarnings("unused")
    private void exportButtonClicked(ActionEvent event) {
        fileChooser.setTitle("Select export destination");
        File file = fileChooser.showSaveDialog(scene.getWindow());
        FileChooser.ExtensionFilter selectedExtension = fileChooser.getSelectedExtensionFilter();
        try (FileWriter fileWriter = new FileWriter(file)) {
            switch (selectedExtension.getDescription()) {
                case CSV_FORMAT_DESCRIPTION:
                    cellAutomaton.printToCSVFile(fileWriter);
                    break;
                case PNG_FORMAT_DESCRIPTION:
                    ImageIO.write(boardController.getRenderedImage(), DEFAULT_IMAGE_FORMAT, file);
                    break;
                default:
                    throw new UnsupportedOperationException();
            }
        } catch (IOException e) {
            Logger.getGlobal().log(Level.ALL, "Unable to export file", e);
        }
    }

    @SuppressWarnings("unused")
    private void importButtonClicked(ActionEvent event) {
        fileChooser.setTitle("Select file to import");
        File file = fileChooser.showOpenDialog(scene.getWindow());
        FileChooser.ExtensionFilter selectedExtension = fileChooser.getSelectedExtensionFilter();
        try (FileReader fileReader = new FileReader(file)) {
            switch (selectedExtension.getDescription()) {
                case CSV_FORMAT_DESCRIPTION:
                    cellAutomaton = CellAutomaton.fromCSVFile(fileReader);
                    reloadAllControllers();
                    break;
                case PNG_FORMAT_DESCRIPTION:
                    Image importedImg = new Image(file.toURI().toString());
                    boardController.reloadBoardWithImage(importedImg);
                    break;
                default:
                    throw new UnsupportedOperationException();
            }
        } catch (IOException e) {
            Logger.getGlobal().log(Level.ALL, "Unable to import file", e);
        }
    }

    private void reloadAllControllers() {
        controllerList.forEach(controller -> controller.reload(cellAutomaton));
    }
}
