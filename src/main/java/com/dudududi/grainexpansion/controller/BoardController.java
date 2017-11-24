package com.dudududi.grainexpansion.controller;

import com.dudududi.grainexpansion.model.CellAutomaton;
import com.dudududi.grainexpansion.model.CoordinatePair;
import com.dudududi.grainexpansion.model.cells.Board;
import com.dudududi.grainexpansion.model.cells.Cell;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.awt.image.RenderedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by dudek on 10/22/17.
 */
public class BoardController implements Controller {

    @FXML
    private ImageView boardView;

    private CellAutomaton cellAutomaton;
    private Random random;

    public BoardController(CellAutomaton cellAutomaton) {
        this.cellAutomaton = cellAutomaton;
        this.random = new Random();
    }

    @FXML
    private void initialize() {
        bindAutomatonWithBoard();
        cellAutomaton.attach(this::bindAutomatonWithBoard);
    }

    private void bindAutomatonWithBoard() {
        Board board = cellAutomaton.getBoard();
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
            if (!cell.isAlive()) {
                cell.setState(new Cell.State(Cell.Phase.ALIVE, getRandomColor()));
            }
        });
    }

    private Color getRandomColor() {
        return new Color(random.nextDouble(), random.nextDouble(), random.nextDouble(), 1);
    }

    RenderedImage getRenderedImage() {
        return SwingFXUtils.fromFXImage(boardView.getImage(), null);
    }

    void reloadBoardWithImage(Image image) {
        boardView.setImage(image);
    }

    @Override
    public void reload(CellAutomaton cellAutomaton) {
        initialize();
    }
}
