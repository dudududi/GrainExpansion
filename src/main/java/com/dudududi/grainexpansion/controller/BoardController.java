package com.dudududi.grainexpansion.controller;

import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

/**
 * Created by dudek on 10/22/17.
 */
class BoardController {
    private static final int DEFAULT_WIDTH = 400;
    private static final int DEFAULT_HEIGHT = 400;

    private static final String BOARD_SELECTOR = "#board";
    private static final String BOARD_WIDTH_FIELD = "#boardWidth";
    private static final String BOARD_HEIGHT_FIELD = "#boardHeight";

    private ImageView board;
    private TextField boardWidth;
    private TextField boardHeight;

    BoardController(Scene scene) {
        this.board = (ImageView) scene.lookup(BOARD_SELECTOR);
        this.boardWidth = (TextField) scene.lookup(BOARD_WIDTH_FIELD);
        this.boardHeight = (TextField) scene.lookup(BOARD_HEIGHT_FIELD);

        boardWidth.setText(Integer.toString(DEFAULT_WIDTH));
        boardHeight.setText(Integer.toString(DEFAULT_HEIGHT));
    }

    private void bindAutomatonWithBoard() {

    }
}
