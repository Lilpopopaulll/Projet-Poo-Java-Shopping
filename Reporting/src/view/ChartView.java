package view;

import org.jfree.chart.*;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import java.awt.*;




public class ChartView {
    private static final Color[] PIE_COLORS = {
            new Color(79, 129, 189), new Color(192, 80, 77),
            new Color(155, 187, 89), new Color(128, 100, 162)
    };

    public JFreeChart createPieChart(String title, DefaultPieDataset dataset) {
        JFreeChart chart = ChartFactory.createPieChart(
                title, dataset, true, true, false);

        PiePlot plot = (PiePlot) chart.getPlot();
        customizePlot(plot, dataset);
        return chart;
    }

    private void customizePlot(PiePlot plot, DefaultPieDataset dataset) {
        // Couleurs
        for (int i = 0; i < dataset.getItemCount(); i++) {
            if (i < PIE_COLORS.length) {
                plot.setSectionPaint(dataset.getKey(i), PIE_COLORS[i]);
            }
        }

        plot.setExplodePercent(dataset.getKey(0), 0.05);
        plot.setLabelGenerator(new StandardPieSectionLabelGenerator(
                "{0} : {1} ventes ({2})"));
        plot.setLabelFont(new Font("SansSerif", Font.PLAIN, 12));
        plot.setBackgroundPaint(Color.WHITE);
    }

    public ChartPanel createChartPanel(JFreeChart chart) {
        ChartPanel panel = new ChartPanel(chart) {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(650, 450);
            }
        };
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        return panel;
    }
}
