package com.dudududi.grainexpansion.controller;

import com.dudududi.grainexpansion.model.SimulationModel;
import com.dudududi.grainexpansion.model.nucleation.ConstantNucleationType;
import com.dudududi.grainexpansion.model.nucleation.IncreasingNucleationType;
import com.dudududi.grainexpansion.model.nucleation.InitialNucleationType;
import com.dudududi.grainexpansion.model.nucleation.NucleationType;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.logging.Level;
import java.util.logging.Logger;

public class StaticRecrystallizationController extends Controller {
    private static final String DEFAULT_NUCLEONS_AMOUNT = "10";
    private Thread recrystallizationThread;

    @FXML
    private ComboBox<NucleationType> selectNucleationType;

    @FXML
    private TextField nucleonsAmountField;

    @FXML
    private ToggleButton startSRXMC;

    @FXML
    private CheckBox onBoundaryCheckBox;

    public StaticRecrystallizationController(SimulationModel simulationModel) {
        super(simulationModel);
    }

    @FXML
    private void initialize() {
        nucleonsAmountField.setText(DEFAULT_NUCLEONS_AMOUNT);
        selectNucleationType.getItems().addAll(
                new ConstantNucleationType(),
                new IncreasingNucleationType(),
                new InitialNucleationType());
        selectNucleationType.getSelectionModel().selectFirst();
        startSRXMC.setToggleGroup(new ToggleGroup());
        startSRXMC.getToggleGroup().selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                recrystallizationThread.interrupt();
            } else {
                NucleationType nucleationType = getSelectedNucleationType();
                recrystallizationThread = new Thread(new RecrystallizationTask(simulationModel, nucleationType));
                recrystallizationThread.start();
            }
        });
    }

     private NucleationType getSelectedNucleationType() {
         int amount = Integer.valueOf(nucleonsAmountField.getText());
         boolean onBoundary = onBoundaryCheckBox.isSelected();
         NucleationType nucleationType = selectNucleationType.getSelectionModel().getSelectedItem();
         nucleationType.setNucleonsAmount(amount);
         nucleationType.setOnBoundary(onBoundary);
         return nucleationType;
     }

    private static class RecrystallizationTask implements Runnable {
        private SimulationModel simulationModel;
        private NucleationType nucleationType;
        private RecrystallizationTask(SimulationModel simulationModel, NucleationType nucleationType) {
            this.simulationModel = simulationModel;
            this.nucleationType = nucleationType;
        }
        @Override
        public void run() {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    nucleationType.apply(simulationModel);
                    simulationModel.getMonteCarlo().recrystallize();
                }
            } catch (InterruptedException e) {
                Logger.getGlobal().log(Level.ALL, "SimulationTask has been interrupted. {0}", e);
                Thread.currentThread().interrupt();
            }
        }
    }
}
