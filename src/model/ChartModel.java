package model;

import org.jfree.data.general.DefaultPieDataset;
import java.util.Map;

public class ChartModel {
    public DefaultPieDataset creerJeuDeDonnees(String categorie) {
        DefaultPieDataset dataset = new DefaultPieDataset();
        Map<String, Integer> produits = SalesData.getDetailedSales().get(categorie);
        if (produits != null) {
            produits.forEach(dataset::setValue);
        }
        return dataset;
    }
}
