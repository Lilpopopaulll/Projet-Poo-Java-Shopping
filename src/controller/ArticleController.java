package controller;

import dao.ArticleDAO;
import model.Article;
import model.Panier;
import view.ArticleView;
import view.ArticleDetailView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class ArticleController implements ArticleClickListener {
    private final ArticleView vue;
    private final ArticleDetailView detailVue;
    private final ArticleDAO articleDAO;
    private final Panier panier;
    private PanierListener panierListener;
    private final Connection connection;

    public ArticleController(ArticleView vue, ArticleDetailView detailVue, Connection connection) {
        this.vue = vue;
        this.detailVue = detailVue;
        this.connection = connection;
        this.articleDAO = new ArticleDAO(connection);
        this.panier = new Panier();
        this.vue.setArticleClickListener(this);
        this.detailVue.setArticleClickListener(this);
        
        // Initialiser le contrôleur (chargement des catégories et configuration du filtrage)
        initialiser();
    }

    public void setPanierListener(PanierListener listener) {
        this.panierListener = listener;
    }

    public void initialiser() {
        // Charger les catégories disponibles
        List<String> categories = articleDAO.getAllCategories();
        vue.setCategories(categories);
        
        // Charger les marques disponibles
        List<String> marques = articleDAO.getAllMarques();
        vue.setMarques(marques);
        
        // Configurer l'écouteur de filtre par catégorie
        vue.setCategoryFilterListener(e -> {
            appliquerFiltres();
        });
        
        // Configurer l'écouteur de filtre par marque
        vue.setMarqueFilterListener(e -> {
            appliquerFiltres();
        });
        
        // Configurer l'écouteur de filtre par prix
        vue.setPrixFilterListener(e -> {
            appliquerFiltres();
        });
        
        // Afficher tous les articles
        afficherArticles();
    }
    
    private void appliquerFiltres() {
        String selectedCategory = vue.getSelectedCategory();
        String selectedMarque = vue.getSelectedMarque();
        
        List<Article> filteredArticles;
        
        if (selectedCategory != null && selectedMarque != null) {
            // Filtrer par catégorie et marque
            filteredArticles = articleDAO.getByCategoryAndMarque(selectedCategory, selectedMarque);
        } else if (selectedCategory != null) {
            // Filtrer par catégorie seulement
            filteredArticles = articleDAO.getByCategory(selectedCategory);
        } else if (selectedMarque != null) {
            // Filtrer par marque seulement
            filteredArticles = articleDAO.getByMarque(selectedMarque);
        } else {
            // Aucun filtre, afficher tous les articles
            filteredArticles = articleDAO.getAll();
        }
        
        // Appliquer le filtre de prix si nécessaire
        filteredArticles = filtrerParPrix(filteredArticles);
        
        vue.afficherArticles(filteredArticles);
    }
    
    private List<Article> filtrerParPrix(List<Article> articles) {
        JComboBox<String> prixComboBox = vue.getPrixComboBox();
        if (prixComboBox == null || prixComboBox.getSelectedIndex() == 0) {
            return articles; // Pas de filtre de prix
        }
        
        String selectedPrixRange = (String) prixComboBox.getSelectedItem();
        List<Article> filteredArticles = new ArrayList<>();
        
        for (Article article : articles) {
            double prix = article.getPrixUnitaire();
            
            if (selectedPrixRange.equals("0-50€") && prix <= 50) {
                filteredArticles.add(article);
            } else if (selectedPrixRange.equals("50-100€") && prix > 50 && prix <= 100) {
                filteredArticles.add(article);
            } else if (selectedPrixRange.equals("100-200€") && prix > 100 && prix <= 200) {
                filteredArticles.add(article);
            } else if (selectedPrixRange.equals("200€+") && prix > 200) {
                filteredArticles.add(article);
            }
        }
        
        return filteredArticles;
    }

    public void afficherArticles() {
        List<Article> articles = articleDAO.getAll();
        vue.afficherArticles(articles);
    }

    public void afficherDetailArticle(Article article) {
        detailVue.afficherDetailArticle(article);
        
        // Configurer le bouton d'ajout au panier
        detailVue.setAddToCartAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Utiliser les nouvelles méthodes de ArticleDetailView
                    Article currentArticle = detailVue.getCurrentArticle();
                    int quantite = detailVue.getSelectedQuantity();
                    
                    
                    // Ajouter l'article au panier
                    ajouterAuPanier(currentArticle, quantite);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, 
                        "Erreur lors de l'ajout au panier: " + ex.getMessage(), 
                        "Erreur", 
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private void ajouterAuPanier(Article article, int quantite) {
        if (article != null && quantite > 0) {
            // Notifier le listener du panier si présent
            if (panierListener != null) {
                panierListener.onAddToCart(article, quantite);
            }
        }
    }

    public Panier getPanier() {
        return panier;
    }
    
    public Connection getConnection() {
        return connection;
    }

    @Override
    public void onArticleClick(Article article) {
        if (article == null) {
            afficherArticles();
        } else {
            afficherDetailArticle(article);
        }
    }
}
