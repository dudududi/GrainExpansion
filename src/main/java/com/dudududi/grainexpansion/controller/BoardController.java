package com.dudududi.grainexpansion.controller;

import com.dudududi.grainexpansion.model.SimulationModel;
import com.dudududi.grainexpansion.model.cells.CoordinatePair;
import com.dudududi.grainexpansion.model.cells.Board;
import com.dudududi.grainexpansion.model.cells.Cell;
import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.awt.image.RenderedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by dudek on 10/22/17.
 */
public class BoardController extends Controller {

    private AnimationTimer updatesTimer;

    @FXML
    private ImageView boardView;

    @FXML
    private ImageView energyView;

    public BoardController(SimulationModel simulationModel) {
        super(simulationModel);
    }

    @FXML
    private void initialize() {
        if (updatesTimer != null) {
            updatesTimer.stop();
        }
        bindAutomatonWithBoard();
        updatesTimer.start();
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

        board.getCells().forEach(cell -> {
            boardImage.getPixelWriter().setColor(cell.getPosition().x, cell.getPosition().y, cell.getState().getColor());
            energyImage.getPixelWriter().setColor(cell.getPosition().x, cell.getPosition().y, computeEnergyColor(cell.getEnergy()));
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

        updatesTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (simulationModel.getUpdatesQueue().isEmpty()) {
                    return;
                }
                List<Cell.Snapshot> snapshots = new ArrayList<>();
                int transferred = simulationModel.getUpdatesQueue().drainTo(snapshots);
                Logger.getGlobal().log(Level.ALL, "Transferred {0} snapshot items to UI.", transferred);
                for (Cell.Snapshot snapshot: snapshots) {
                    int x = snapshot.getPosition().x;
                    int y = snapshot.getPosition().y;
                    boardImage.getPixelWriter().setColor(x, y, snapshot.getColor());
                    energyImage.getPixelWriter().setColor(x, y, computeEnergyColor(snapshot.getEnergy()));
                }
            }
        };
    }

    void setEnergyDistributionVisible(boolean isVisible) {
        energyView.setVisible(isVisible);
    }

    private Color computeEnergyColor(int energy) {
        double hue = Color.BLUE.getHue() + (Color.RED.getHue() - Color.BLUE.getHue()) * (energy - Cell.MIN_ENERGY) / (Cell.MAX_ENERGY - Cell.MIN_ENERGY);
        return Color.hsb(hue, 1.0, 1.0);
    }
}
