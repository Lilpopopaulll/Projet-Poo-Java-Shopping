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
                // Récupérer la quantité depuis le spinner (implémentation à adapter selon votre UI)
                JSpinner spinner = ((JSpinner) ((JPanel) ((JButton) e.getSource()).getParent().getComponent(2)).getComponent(2));
                int quantite = (Integer) spinner.getValue();
                
                // Ajouter l'article au panier
                ajouterAuPanier(article, quantite);
            }
        });
    }

    private void ajouterAuPanier(Article article, int quantite) {
        if (article != null && quantite > 0) {
            panier.ajouterArticle(article, quantite);
            
            // Notifier le listener du panier si présent
            if (panierListener != null) {
                panierListener.onAddToCart(article, quantite);
            }
            
            // Afficher un message de confirmation
            JOptionPane.showMessageDialog(
                null, 
                quantite + " x " + article.getNom() + " ajouté(s) au panier",
                "Ajout au panier", 
                JOptionPane.INFORMATION_MESSAGE
            );
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
