package controller;

import dao.ArticleDAO;
import model.Article;
import model.Panier;
import view.ArticleView;
import view.ArticleDetailView;

import javax.swing.*;
import java.awt.Component;
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
                try {
                    // Récupérer la quantité depuis le spinner
                    JButton button = (JButton) e.getSource();
                    JPanel infoPanel = (JPanel) button.getParent();
                    
                    // Trouver le panel de quantité (il est à l'index 4 dans infoPanel)
                    JPanel quantityPanel = null;
                    for (int i = 0; i < infoPanel.getComponentCount(); i++) {
                        Component comp = infoPanel.getComponent(i);
                        if (comp instanceof JPanel && ((JPanel) comp).getComponentCount() > 0 
                                && ((JPanel) comp).getComponent(0) instanceof JLabel 
                                && ((JLabel) ((JPanel) comp).getComponent(0)).getText().startsWith("Quantité")) {
                            quantityPanel = (JPanel) comp;
                            break;
                        }
                    }
                    
                    if (quantityPanel == null) {
                        throw new Exception("Impossible de trouver le panel de quantité");
                    }
                    
                    // Le spinner est le 3ème composant (index 2) du panel de quantité
                    JSpinner spinner = (JSpinner) quantityPanel.getComponent(2);
                    int quantite = (Integer) spinner.getValue();
                    
                    System.out.println("Tentative d'ajout au panier: " + article.getNom() + ", quantité: " + quantite);
                    
                    // Ajouter l'article au panier
                    ajouterAuPanier(article, quantite);
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
