package com.dudududi.grainexpansion.controller;

import com.dudududi.grainexpansion.model.CellAutomaton;
import javafx.event.ActionEvent;
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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by dudek on 10/22/17.
 */
public class RootController{

    private static final String MENU_BAR_SELECTOR = "#menuBar";
    private static final String IMPORT_BUTTON_SELECTOR = "importButton";
    private static final String EXPORT_BUTTON_SELECTOR = "exportButton";
    private static final String CSV_FORMAT_EXTENSION = "*.csv";
    private static final String CSV_FORMAT_DESCRIPTION = "CSV file";
    private static final String DEFAULT_IMAGE_FORMAT = "png";
    private static final String PNG_FORMAT_EXTENSION = "*." + DEFAULT_IMAGE_FORMAT;
    private static final String PNG_FORMAT_DESCRIPTION = "PNG file";

    private FileChooser fileChooser;
    private Scene scene;
    private AutomatonController automatonController;
    private BoardController boardController;


    public void init(Scene scene) {
        this.scene = scene;
        this.boardController = new BoardController(scene);
        this.automatonController = new AutomatonController(scene, boardController);
        this.fileChooser = new FileChooser();
        configureFileChooser();
        MenuBar menuBar = (MenuBar) scene.lookup(MENU_BAR_SELECTOR);
        menuBar.getMenus()
                .get(0)
                .getItems()
                .forEach(this::configureMenuItem);
    }


    private void configureMenuItem(MenuItem menuItem) {
        switch (menuItem.getId()) {
            case IMPORT_BUTTON_SELECTOR:
                menuItem.setOnAction(this::importButtonClicked);
                break;
            case EXPORT_BUTTON_SELECTOR:
                menuItem.setOnAction(this::exportButtonClicked);
                break;
            default:
                throw new UnsupportedOperationException();
        }
    }

    private void configureFileChooser() {
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter(CSV_FORMAT_DESCRIPTION, CSV_FORMAT_EXTENSION),
                new FileChooser.ExtensionFilter(PNG_FORMAT_DESCRIPTION, PNG_FORMAT_EXTENSION));
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
    }
    @SuppressWarnings("unused")
    private void exportButtonClicked(ActionEvent event) {
        fileChooser.setTitle("Select export destination");
        File file = fileChooser.showSaveDialog(scene.getWindow());
        FileChooser.ExtensionFilter selectedExtension = fileChooser.getSelectedExtensionFilter();
        try (FileWriter fileWriter = new FileWriter(file)) {
            switch (selectedExtension.getDescription()) {
                case CSV_FORMAT_DESCRIPTION:
                    automatonController.getCellAutomaton().printToCSVFile(fileWriter);
                    break;
                case PNG_FORMAT_DESCRIPTION:
                    ImageIO.write(boardController.getRenderedImage(), DEFAULT_IMAGE_FORMAT, file);
                    break;
                default:
                    throw new UnsupportedOperationException();
            }
        } catch (IOException e){
            Logger.getGlobal().log(Level.ALL, "Unable to export file", e);
        }
    }

    @SuppressWarnings("unused")
    private void importButtonClicked(ActionEvent event) {
        fileChooser.setTitle("Select file to import");
        File file = fileChooser.showOpenDialog(scene.getWindow());
        FileChooser.ExtensionFilter selectedExtension = fileChooser.getSelectedExtensionFilter();
        try(FileReader fileReader = new FileReader(file)) {
            switch (selectedExtension.getDescription()) {
                case CSV_FORMAT_DESCRIPTION:
                    CellAutomaton cellAutomaton = CellAutomaton.fromCSVFile(fileReader);
                    boardController.reload(cellAutomaton.getBoard());
                    automatonController.reload(cellAutomaton);
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
}
