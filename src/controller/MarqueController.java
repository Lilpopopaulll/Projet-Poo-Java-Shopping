package controller;

import dao.ArticleMarqueDAO;
import dao.MarqueDAO;
import model.Article;
import model.Marque;
import view.MarqueDetailView;
import view.MarqueView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.List;

public class MarqueController implements MarqueClickListener {
    private final MarqueDAO marqueDAO;
    private final ArticleMarqueDAO articleMarqueDAO;
    private MarqueView marqueView;
    private MarqueDetailView marqueDetailView;
    private JPanel mainPanel;
    private ArticleClickListener articleClickListener;

    public MarqueController(Connection connection, JPanel mainPanel) {
        this.marqueDAO = new MarqueDAO(connection);
        this.articleMarqueDAO = new ArticleMarqueDAO(connection);
        this.mainPanel = mainPanel;
        
        // Initialiser les vues
        this.marqueView = new MarqueView();
        this.marqueDetailView = new MarqueDetailView();
        
        // Configurer les écouteurs
        this.marqueView.setMarqueClickListener(this);
        this.marqueDetailView.setRetourListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Retour à la landing page
                if (mainPanel != null) {
                    CardLayout cl = (CardLayout) mainPanel.getLayout();
                    cl.show(mainPanel, "landing");
                }
            }
        });
        
        // Ajouter les vues au mainPanel
        if (mainPanel != null) {
            mainPanel.add(marqueDetailView, "marqueDetail");
        }
    }
    
    /**
     * Constructeur alternatif qui utilise une vue de marques existante
     */
    public MarqueController(Connection connection, JPanel mainPanel, MarqueView existingMarqueView) {
        this.marqueDAO = new MarqueDAO(connection);
        this.articleMarqueDAO = new ArticleMarqueDAO(connection);
        this.mainPanel = mainPanel;
        
        // Utiliser la vue de marques existante
        this.marqueView = existingMarqueView;
        this.marqueDetailView = new MarqueDetailView();
        
        // Configurer les écouteurs
        this.marqueView.setMarqueClickListener(this);
        this.marqueDetailView.setRetourListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Retour à la landing page
                if (mainPanel != null) {
                    CardLayout cl = (CardLayout) mainPanel.getLayout();
                    cl.show(mainPanel, "landing");
                }
            }
        });
        
        // Ajouter les vues au mainPanel
        if (mainPanel != null) {
            mainPanel.add(marqueDetailView, "marqueDetail");
        }
    }
    
    /**
     * Charger et afficher toutes les marques
     */
    public void afficherMarques() {
        List<Marque> marques = marqueDAO.getAll();
        marqueView.afficherMarques(marques);
    }
    
    /**
     * Méthode appelée lorsqu'une marque est cliquée
     */
    @Override
    public void onMarqueClick(Marque marque) {
        // Récupérer les articles de la marque
        List<Article> articles;
        
        // Essayer d'abord avec l'ID de la marque
        articles = articleMarqueDAO.getArticlesByMarqueId(marque.getIdMarque());
        
        // Si aucun article n'est trouvé, essayer avec le nom de la marque
        if (articles.isEmpty()) {
            articles = articleMarqueDAO.getArticlesByMarqueName(marque.getNom());
        }
        
        // Afficher les articles de la marque
        marqueDetailView.afficherArticlesMarque(marque, articles);
        
        // Configurer l'écouteur de clic sur les articles
        marqueDetailView.setArticleClickListener(articleClickListener);
        
        // Afficher la vue détaillée de la marque
        if (mainPanel != null) {
            CardLayout cl = (CardLayout) mainPanel.getLayout();
            cl.show(mainPanel, "marqueDetail");
        }
    }
    
    /**
     * Définir l'écouteur de clic sur les articles
     */
    public void setArticleClickListener(ArticleClickListener listener) {
        this.articleClickListener = listener;
        this.marqueDetailView.setArticleClickListener(listener);
    }
    
    /**
     * Obtenir la vue des marques
     */
    public MarqueView getMarqueView() {
        return marqueView;
    }
}
