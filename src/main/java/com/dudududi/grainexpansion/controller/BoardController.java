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

import java.awt.image.RenderedImage;

/**
 * Created by dudek on 10/22/17.
 */
public class BoardController extends Controller {

    @FXML
    private ImageView boardView;

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
        WritableImage image = new WritableImage(board.getWidth(), board.getHeight());
        board.getCells()
                .forEach(c -> {
                    c.colorProperty().addListener((observable, oldColor, newColor) -> {
                        image.getPixelWriter().setColor(c.getPosition().x, c.getPosition().y, newColor);
                    });
                    image.getPixelWriter().setColor(c.getPosition().x, c.getPosition().y, c.colorProperty().get());
                });
        boardView.setImage(image);
        boardView.setFitWidth(board.getWidth());
        boardView.setFitHeight(board.getHeight());

        boardView.onMouseClickedProperty().setValue(event -> {
            int x = (int) event.getX();
            int y = (int) event.getY();
            Cell cell = board.getCell(new CoordinatePair(x, y));
            simulationModel.handleCellClick(cell);
        });
    }
}
