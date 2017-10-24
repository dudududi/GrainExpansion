package com.dudududi.grainexpansion.controller;

import com.dudududi.grainexpansion.controller.randomizable.PseudoRandomize;
import com.dudududi.grainexpansion.model.cells.Cell;
import com.dudududi.grainexpansion.model.CellAutomaton;
import com.dudududi.grainexpansion.model.cells.CellState;
import com.dudududi.grainexpansion.model.definables.neighbourhood.MooreNeighbourhood;
import com.dudududi.grainexpansion.model.rules.StaticRecrystallizationRule;
import com.dudududi.grainexpansion.model.definables.neighbourhood.NeighbourhoodType;
import com.dudududi.grainexpansion.model.rules.Rule;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RootController {
    private static final int DEFAULT_WIDTH = 400;
    private static final int DEFAULT_HEIGHT = 400;
    private static final int DEFAULT_NUCLEONS_NUMBER = 25;

    private List<Node> disableNodes;
    private WritableImage image;

    @FXML
    private ImageView board;

    @FXML
    private ToggleButton startButton;

    @FXML
    private TextField widthField;

    @FXML
    private TextField heightField;

    @FXML
    private TextField nucleonsField;

    @FXML
    private TextField inclusionsField;

    @FXML
    private TextField inclusionsSize;

    @FXML
    private Button clearButton;

    @FXML
    private Button generateButton;

    @FXML
    private Button randomizeButton;

    @FXML
    private Button inclusionsButton;

    @FXML
    private CheckBox periodicBC;

    @FXML
    private MenuItem importButton;

    @FXML
    private MenuItem exportButton;

    @FXML
    private ComboBox<String> selectInclusionsType;


    private NeighbourhoodType neighbourhood;
    private CellAutomaton cellAutomaton;
    private FileChooser fileChooser;
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void initialize() {
        collectNodes();
        neighbourhood = new MooreNeighbourhood();
        cellAutomaton = new CellAutomaton(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        generateAutomaton();

        widthField.setText(Integer.toString(DEFAULT_WIDTH));
        heightField.setText(Integer.toString(DEFAULT_HEIGHT));
        nucleonsField.setText(Integer.toString(DEFAULT_NUCLEONS_NUMBER));

        generateButton.setOnMouseClicked(event -> {
            cellAutomaton.clear();
            int width = Integer.valueOf(widthField.getText());
            int height = Integer.valueOf(heightField.getText());
            cellAutomaton = new CellAutomaton(width, height);
            generateAutomaton();
        });

        periodicBC.setSelected(true);
        periodicBC.selectedProperty().addListener((observable, oldValue, newValue) -> {
            neighbourhood.setPeriodic(newValue);
            cellAutomaton.init(neighbourhood);
        });

        randomizeButton.setOnMouseClicked(event ->
                new PseudoRandomize()
                        .randomize(cellAutomaton, Integer.parseInt(nucleonsField.getText()), 0));

        clearButton.setOnMouseClicked(event -> cellAutomaton.clear());

        configureAnimation();
        configureFileChooser();
        configureImportExportOptions();
        configureInclusions();
    }

    private void generateAutomaton() {
        image = new WritableImage(cellAutomaton.getWidth(), cellAutomaton.getHeight());
        Cell[][] cells = cellAutomaton.getCells();
        for (int i = 0; i < cellAutomaton.getWidth(); i++) {
            for (int j = 0; j < cellAutomaton.getHeight(); j++) {
                Cell cell = cells[i][j];
                int m = i, n = j;
                cell.getAliveProperty().addListener(((observable, wasAlive, willBeAlive) -> {
                    if (willBeAlive) {
                        image.getPixelWriter().setColor(m, n, cell.getColor());
                    } else {
                        cell.setState(new CellState(Color.WHITE, CellState.Type.DEAD));
                        image.getPixelWriter().setColor(m, n, Color.WHITE);
                    }
                }));
                image.getPixelWriter().setColor(i, j, cell.getColor());
            }
        }
        board.setImage(image);
        board.setFitWidth(cellAutomaton.getWidth());
        board.setFitHeight(cellAutomaton.getHeight());
        board.onMouseClickedProperty().setValue(event -> {
            int x = (int)event.getX(), y = (int)event.getY();
            Cell cell = cells[x][y];
            if (cell.isAlive()){
                cell.setAlive(false);
            } else {
                cell.setAliveWithRandomColor();
            }
        });
        cellAutomaton.init(neighbourhood);
    }

    private void collectNodes(){
        disableNodes = new ArrayList<>();
        disableNodes.add(widthField);
        disableNodes.add(heightField);
        disableNodes.add(generateButton);
        disableNodes.add(periodicBC);
        disableNodes.add(nucleonsField);
        disableNodes.add(randomizeButton);
        disableNodes.add(clearButton);
    }

    private void configureAnimation(){
        Rule automatonRule = new StaticRecrystallizationRule();
        Timeline animation = new Timeline(new KeyFrame(Duration.millis(90), event -> cellAutomaton.next(automatonRule)));
        animation.setCycleCount(Timeline.INDEFINITE);
        startButton.setToggleGroup(new ToggleGroup());
        startButton.getToggleGroup().selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                animation.stop();
                disableNodes.forEach(node -> node.setDisable(false));
                startButton.setText("START");
            } else {
                animation.play();
                disableNodes.forEach(node -> node.setDisable(true));
                startButton.setText("STOP");
            }
        });
    }

    private void configureImportExportOptions() {
        exportButton.setOnAction(event -> {
            fileChooser.setTitle("Select export destination");
            File file = fileChooser.showSaveDialog(stage);
            FileChooser.ExtensionFilter selectedExtension = fileChooser.getSelectedExtensionFilter();
            try (FileWriter fileWriter = new FileWriter(file)) {
                if ("CSV file".equals(selectedExtension.getDescription())) {
                    cellAutomaton.printToCSVFile(fileWriter);
                } else {
                    ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
                }
            } catch (IOException e){
                Logger.getGlobal().log(Level.ALL, "Unable to export file", e);
            }
        });

        importButton.setOnAction(event -> {
            fileChooser.setTitle("Select file to import");
            File file = fileChooser.showOpenDialog(stage);
            FileChooser.ExtensionFilter selectedExtension = fileChooser.getSelectedExtensionFilter();
            try(FileReader fileReader = new FileReader(file)) {
                if ("CSV file".equals(selectedExtension.getDescription())) {
                    cellAutomaton.clear();
                    cellAutomaton = CellAutomaton.fromCSVFile(fileReader);
                    generateAutomaton();
                    widthField.setText(Integer.toString(cellAutomaton.getWidth()));
                    heightField.setText(Integer.toString(cellAutomaton.getHeight()));
                    configureAnimation();
                } else {
                    Image importedImg = new Image(file.toURI().toString());
                    board.setImage(importedImg);
                }
            } catch (IOException e) {
                Logger.getGlobal().log(Level.ALL, "Unable to import file", e);
            }
        });
    }

    private void configureFileChooser() {
        fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("CSV file", "*.csv"),
                new FileChooser.ExtensionFilter("PNG file", "*.png"));
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
    }

    private void configureInclusions(){
        ObservableList<String> inclusionsTypes = FXCollections.observableArrayList("Square", "Circular");
        selectInclusionsType.getItems().addAll(inclusionsTypes);
        selectInclusionsType.getSelectionModel().selectFirst();
        inclusionsButton.setOnMouseClicked(event -> {
            int inclusionsAmount = Integer.valueOf(inclusionsField.getText());
            int size = Integer.valueOf(inclusionsSize.getText());
            boolean isCircular = selectInclusionsType.getSelectionModel().getSelectedItem().equals("Circular");
            cellAutomaton.addInclusions(inclusionsAmount, size, isCircular);
        });
    }
}
