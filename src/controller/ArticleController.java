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
    }

    public void setPanierListener(PanierListener listener) {
        this.panierListener = listener;
    }

    public void initialiser() {
        // Charger les catégories disponibles
        List<String> categories = articleDAO.getAllCategories();
        vue.setCategories(categories);
        
        // Configurer l'écouteur de filtre par catégorie
        vue.setCategoryFilterListener(e -> {
            String selectedCategory = vue.getSelectedCategory();
            if (selectedCategory != null) {
                // Filtrer par catégorie sélectionnée
                List<Article> filteredArticles = articleDAO.getByCategory(selectedCategory);
                vue.afficherArticles(filteredArticles);
            } else {
                // Afficher tous les articles
                afficherArticles();
            }
        });
        
        // Afficher tous les articles
        afficherArticles();
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
                    
                    System.out.println("Tentative d'ajout au panier: " + currentArticle.getNom() + ", quantité: " + quantite);
                    
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
