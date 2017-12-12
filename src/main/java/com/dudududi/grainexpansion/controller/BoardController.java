package com.dudududi.grainexpansion.controller;

import com.dudududi.grainexpansion.model.SimulationModel;
import com.dudududi.grainexpansion.model.cells.CoordinatePair;
import com.dudududi.grainexpansion.model.cells.Board;
import com.dudududi.grainexpansion.model.cells.Cell;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.awt.image.RenderedImage;

/**
 * Created by dudek on 10/22/17.
 */
public class BoardController extends Controller {

    @FXML
    private ImageView boardView;

    @FXML
    private ImageView energyView;

    public BoardController(SimulationModel simulationModel) {
        super(simulationModel);
    }

    @FXML
    private void initialize() {
        bindAutomatonWithBoard();
    }

    @Override
    public void onBoardChanged() {
        initialize();
    }

    RenderedImage getRenderedImage() {
        return SwingFXUtils.fromFXImage(boardView.getImage(), null);
    }

    void reloadBoardWithImage(Image image) {
        boardView.setImage(image);
    }

    private void bindAutomatonWithBoard() {
        Board board = simulationModel.getBoard();
        WritableImage boardImage = new WritableImage(board.getWidth(), board.getHeight());
        WritableImage energyImage = new WritableImage(board.getWidth(), board.getHeight());
        board.getCells().forEach(c -> {
            setupCellOnBoardView(c, boardImage);
            setupCellOnEnergyView(c, energyImage);
        });
        boardView.setImage(boardImage);
        boardView.setFitWidth(board.getWidth());
        boardView.setFitHeight(board.getHeight());

        energyView.setImage(energyImage);
        energyView.setFitWidth(board.getWidth());
        energyView.setFitHeight(board.getHeight());
        energyView.managedProperty().bind(energyView.visibleProperty());

        boardView.onMouseClickedProperty().setValue(event -> {
            int x = (int) event.getX();
            int y = (int) event.getY();
            Cell cell = board.getCell(new CoordinatePair(x, y));
            simulationModel.handleCellClick(cell);
        });

        setEnergyDistributionVisible(false);
    }

    void setEnergyDistributionVisible(boolean isVisible) {
        energyView.setVisible(isVisible);
    }

    private void setupCellOnBoardView(Cell cell, WritableImage boardImage) {
        cell.colorProperty().addListener((observable, oldColor, newColor) ->
                boardImage.getPixelWriter().setColor(cell.getPosition().x, cell.getPosition().y, newColor)
        );
        boardImage.getPixelWriter().setColor(cell.getPosition().x, cell.getPosition().y, cell.colorProperty().get());
    }

    private void setupCellOnEnergyView(Cell cell, WritableImage energyImage) {
        cell.energyProperty().addListener((observable, oldEnergy, newEnergy) -> {
            int value = newEnergy.intValue();
            energyImage.getPixelWriter().setColor(cell.getPosition().x, cell.getPosition().y, computeEnergyColor(value));
        });
        energyImage.getPixelWriter().setColor(cell.getPosition().x, cell.getPosition().y, computeEnergyColor(cell.getEnergy()));
    }

    private Color computeEnergyColor(int energy) {
        double hue = Color.BLUE.getHue() + (Color.RED.getHue() - Color.BLUE.getHue()) * (energy - Cell.MIN_ENERGY) / (Cell.MAX_ENERGY - Cell.MIN_ENERGY);
        return Color.hsb(hue, 1.0, 1.0);
    }
}
