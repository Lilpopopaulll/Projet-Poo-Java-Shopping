package view;

import controller.ArticleClickListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class LandingPageView extends JPanel {
    
    private ArticleView articleView;
    private HeroBannerView heroBannerView;
    private JScrollPane scrollPane;
    
    public LandingPageView() {
        setLayout(new BorderLayout());
        setBackground(Color.decode("#F5F5F5"));
        
        // Créer la bannière hero
        heroBannerView = new HeroBannerView();
        
        // Créer la vue des articles
        articleView = new ArticleView();
        
        // Créer un panneau pour contenir tous les éléments
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.decode("#F5F5F5"));
        
        // Ajouter les composants au panneau de contenu
        contentPanel.add(heroBannerView);
        contentPanel.add(articleView);
        
        // Créer un JScrollPane pour permettre le défilement
        scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        // Ajouter un écouteur de molette de souris pour permettre le défilement partout
        addMouseWheelListener(new GlobalScrollListener());
        
        // Ajouter le scrollPane au panneau principal
        add(scrollPane, BorderLayout.CENTER);
    }
    
    public ArticleView getArticleView() {
        return articleView;
    }
    
    public void setArticleClickListener(ArticleClickListener listener) {
        articleView.setArticleClickListener(listener);
    }
    
    // Classe interne pour gérer le défilement global
    private class GlobalScrollListener implements MouseWheelListener {
        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            // Récupérer la barre de défilement vertical
            JScrollBar verticalBar = scrollPane.getVerticalScrollBar();
            
            // Calculer la nouvelle position
            int newValue = verticalBar.getValue() + (e.getWheelRotation() * verticalBar.getUnitIncrement());
            
            // Limiter la valeur à la plage valide
            newValue = Math.max(newValue, verticalBar.getMinimum());
            newValue = Math.min(newValue, verticalBar.getMaximum() - verticalBar.getVisibleAmount());
            
            // Définir la nouvelle position
            verticalBar.setValue(newValue);
        }
    }
}