package view;

import javax.swing.*;
import java.util.Map;

public class ReportFrame extends JFrame {
    public ReportFrame() {
        super("Statistiques de vente - MVC");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 750);
    }

    public void initUI(Map<String, JPanel> categoryPanels) {
        JTabbedPane tabbedPane = new JTabbedPane();
        categoryPanels.forEach(tabbedPane::addTab);
        add(tabbedPane);
        setLocationRelativeTo(null);
    }
}