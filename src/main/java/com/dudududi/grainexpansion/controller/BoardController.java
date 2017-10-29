package com.dudududi.grainexpansion.controller;

import com.dudududi.grainexpansion.model.CoordinatePair;
import com.dudududi.grainexpansion.model.cells.Board;
import com.dudududi.grainexpansion.model.cells.Cell;
import javafx.embed.swing.SwingFXUtils;
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
class BoardController implements Controller<Board>{
    private static final int DEFAULT_WIDTH = 400;
    private static final int DEFAULT_HEIGHT = 400;
    private static final int DEFAULT_NUCLEONS_AMOUNT = 25;
    private static final boolean DEFAULT_PERIODICITY = true;

    private static final String BOARD_SELECTOR = "#board";
    private static final String BOARD_WIDTH_FIELD_SELECTOR = "#boardWidth";
    private static final String BOARD_HEIGHT_FIELD_SELECTOR = "#boardHeight";
    private static final String NUCLEONS_AMOUNT_FIELD_SELECTOR = "#nucleonsAmountField";
    private static final String PERIODIC_BC_CHECKBOX_SELECTOR = "#periodicBC";
    private static final String RANDOMIZE_BUTTON_SELECTOR = "#randomizeButton";
    private static final String CLEAR_BUTTON_SELECTOR = "#clearButton";


    private final ImageView boardView;
    private final TextField boardWidthField;
    private final TextField boardHeightField;
    private final TextField nucleonsAmountField;
    private final CheckBox periodicBC;
    private final Button randomizeButton;
    private final Button clearButton;

    private Random random;
    private Board board;

    BoardController(Scene scene) {
        this.boardView = (ImageView) scene.lookup(BOARD_SELECTOR);
        this.boardWidthField = (TextField) scene.lookup(BOARD_WIDTH_FIELD_SELECTOR);
        this.boardHeightField = (TextField) scene.lookup(BOARD_HEIGHT_FIELD_SELECTOR);
        this.periodicBC = (CheckBox) scene.lookup(PERIODIC_BC_CHECKBOX_SELECTOR);
        this.randomizeButton = (Button) scene.lookup(RANDOMIZE_BUTTON_SELECTOR);
        this.clearButton = (Button) scene.lookup(CLEAR_BUTTON_SELECTOR);
        this.nucleonsAmountField = (TextField) scene.lookup(NUCLEONS_AMOUNT_FIELD_SELECTOR);

        boardWidthField.setText(Integer.toString(DEFAULT_WIDTH));
        boardHeightField.setText(Integer.toString(DEFAULT_HEIGHT));
        periodicBC.setSelected(DEFAULT_PERIODICITY);
        nucleonsAmountField.setText(String.valueOf(DEFAULT_NUCLEONS_AMOUNT));

        this.board = new Board(DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_PERIODICITY);
        random = new Random();

        randomizeButton.setOnMouseClicked(event -> {
            int nucleonsAmount = Integer.valueOf(nucleonsAmountField.getText());
            for (int i = 0; i < nucleonsAmount; i++) {
                int x = random.nextInt(board.getWidth());
                int y = random.nextInt(board.getHeight());
                Cell cell = board.getCell(new CoordinatePair(x, y));
                if (!cell.isAlive()) {
                    cell.setState(new Cell.State(Cell.Phase.ALIVE, getRandomColor()));
                }
            }
        });

        clearButton.setOnMouseClicked(event -> board.resetAll());

        bindAutomatonWithBoard();
    }

    private void bindAutomatonWithBoard() {
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
            int x = (int)event.getX();
            int y = (int)event.getY();
            Cell cell = board.getCell(new CoordinatePair(x, y));
            if (!cell.isAlive()) {
                cell.setState(new Cell.State(Cell.Phase.ALIVE, getRandomColor()));
            }
        });
    }

    private Color getRandomColor() {
        return new Color(random.nextDouble(), random.nextDouble(), random.nextDouble(), 1);
    }

    Board getBoard() {
        return board;
    }

    RenderedImage getRenderedImage() {
        return SwingFXUtils.fromFXImage(boardView.getImage(), null);
    }

    void reloadBoardWithImage(Image image) {
        boardView.setImage(image);
    }

    @Override
    @SuppressWarnings("Duplicates")
    public List<Node> collectDisablingNodes() {
        List<Node> nodes = new ArrayList<>();
        nodes.add(boardWidthField);
        nodes.add(boardHeightField);
        nodes.add(periodicBC);
        nodes.add(randomizeButton);
        nodes.add(clearButton);
        nodes.add(nucleonsAmountField);
        return nodes;
    }

    @Override
    public void reload(Board newBoard) {
        board.resetAll();
        this.board = newBoard;
        boardWidthField.setText(Integer.toString(board.getWidth()));
        boardHeightField.setText(Integer.toString(board.getHeight()));
        bindAutomatonWithBoard();
    }
}
