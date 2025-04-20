package controller;

import model.ChartModel;
import view.ChartView;
import javax.swing.*;

public class ChartController {
    private final ChartModel model;
    private final ChartView view;

    public ChartController() {
        this.model = new ChartModel();
        this.view = new ChartView();
    }

    public JPanel createCategoryChart(String category) {
        return view.createChartPanel(
                view.createPieChart(
                        "Détail des ventes: " + category,
                        model.createDataset(category)
                )
        );
    }
}