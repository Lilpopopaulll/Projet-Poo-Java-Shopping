package model;

import org.jfree.data.general.DefaultPieDataset;
import java.util.Map;

public class ChartModel {
    public DefaultPieDataset createDataset(String category) {
        DefaultPieDataset dataset = new DefaultPieDataset();
        Map<String, Integer> products = SalesData.getDetailedSales().get(category);
        if (products != null) {
            products.forEach(dataset::setValue);
        }
        return dataset;
    }
}