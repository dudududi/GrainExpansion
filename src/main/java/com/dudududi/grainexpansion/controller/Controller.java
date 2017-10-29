package com.dudududi.grainexpansion.controller;

import javafx.scene.Node;

import java.util.List;

/**
 * Created by dudek on 10/24/17.
 */
public interface Controller<T> {
    List<Node> collectDisablingNodes();
    void reload(T model);
}
