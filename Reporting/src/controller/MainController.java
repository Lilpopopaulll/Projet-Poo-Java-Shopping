package controller;

import view.ReportFrame;

import javax.swing.*;
import java.util.*;

public class MainController {
    public void startApplication() {
        ChartController chartController = new ChartController();
        ReportFrame frame = new ReportFrame();

        Map<String, JPanel> panels = new LinkedHashMap<>();
        panels.put("Chemises", chartController.createCategoryChart("Chemises"));
        panels.put("Chaussures", chartController.createCategoryChart("Chaussures"));
        panels.put("Chapeaux/Casquettes", chartController.createCategoryChart("Chapeaux/Casquettes"));
        panels.put("Pantalons", chartController.createCategoryChart("Pantalons"));

        frame.initUI(panels);
        frame.setVisible(true);
    }
}