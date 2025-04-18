package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Panier {
    private Map<Article, Integer> articles;
    
    public Panier() {
        this.articles = new HashMap<>();
    }
    
    public void ajouterArticle(Article article, int quantite) {
        if (article == null || quantite <= 0) {
            return;
        }
        
        // Si l'article existe déjà, on ajoute la quantité
        if (articles.containsKey(article)) {
            int quantiteActuelle = articles.get(article);
            articles.put(article, quantiteActuelle + quantite);
        } else {
            articles.put(article, quantite);
        }
    }
    
    public void retirerArticle(Article article) {
        if (article != null) {
            articles.remove(article);
        }
    }
    
    public void modifierQuantite(Article article, int nouvelleQuantite) {
        if (article != null && nouvelleQuantite > 0) {
            articles.put(article, nouvelleQuantite);
        } else if (nouvelleQuantite <= 0) {
            retirerArticle(article);
        }
    }
    
    public Map<Article, Integer> getArticles() {
        return articles;
    }
    
    public int getNombreArticles() {
        return articles.size();
    }
    
    public int getQuantiteTotale() {
        int total = 0;
        for (int quantite : articles.values()) {
            total += quantite;
        }
        return total;
    }
    
    public double getMontantTotal() {
        double total = 0.0;
        for (Map.Entry<Article, Integer> entry : articles.entrySet()) {
            total += entry.getKey().getPrixUnitaire() * entry.getValue();
        }
        return total;
    }
    
    public void viderPanier() {
        articles.clear();
    }
    
    public List<LigneCommande> creerLignesCommande() {
        List<LigneCommande> lignes = new ArrayList<>();
        // Implémentation à compléter quand la classe LigneCommande sera définie
        return lignes;
    }
}
