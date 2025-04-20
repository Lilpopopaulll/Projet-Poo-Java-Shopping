package model;

import java.util.*;



public class SalesData {
    private static final Map<String, Map<String, Integer>> detailedSales = new LinkedHashMap<>();

    static {
        // Chemises
        Map<String, Integer> chemises = new LinkedHashMap<>();
        chemises.put("Chemise #1 - Blanche", 120);
        chemises.put("Chemise #2 - Rayée", 85);
        chemises.put("Chemise #3 - Oxford", 65);
        detailedSales.put("Chemises", chemises);

        // Chaussures
        Map<String, Integer> chaussures = new LinkedHashMap<>();
        chaussures.put("Baskets #1 - Sport", 95);
        chaussures.put("Escarpins #2 - Noir", 70);
        chaussures.put("Bottes #3 - Cuir", 55);
        detailedSales.put("Chaussures", chaussures);

        // Chapeaux/Casquettes
        Map<String, Integer> headwear = new LinkedHashMap<>();
        headwear.put("Casquette #1 - Baseball", 60);
        headwear.put("Chapeau #2 - Panama", 45);
        headwear.put("Bonnet #3 - Laine", 35);
        detailedSales.put("Chapeaux/Casquettes", headwear);

        // Pantalons
        Map<String, Integer> pantalons = new LinkedHashMap<>();
        pantalons.put("Jean #1 - Slim", 110);
        pantalons.put("Chino #2 - Classique", 75);
        pantalons.put("Pantalon #3 - Costume", 50);
        detailedSales.put("Pantalons", pantalons);
    }

    public static Map<String, Map<String, Integer>> getDetailedSales() {
        return new LinkedHashMap<>(detailedSales);
    }
}
