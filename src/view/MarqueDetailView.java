package view;

import controller.ArticleClickListener;
import model.Article;
import model.Marque;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class MarqueDetailView extends JPanel {
    private Marque marque;
    private ArticleClickListener articleClickListener;
    private JButton retourButton;
    
    public MarqueDetailView() {
        setLayout(new BorderLayout());
        setBackground(Color.decode("#F5F5F5"));
        setBorder(BorderFactory.createEmptyBorder(20, 40, 40, 40));
        
        // Bouton de retour
        retourButton = new JButton("← Retour aux marques");
        retourButton.setFocusPainted(false);
        retourButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Titre (sera défini lors de l'affichage des articles)
        JLabel titleLabel = new JLabel();
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        
        // Panel pour le titre et le bouton de retour
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.decode("#F5F5F5"));
        headerPanel.add(retourButton, BorderLayout.WEST);
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        add(headerPanel, BorderLayout.NORTH);
    }
    
    public void afficherArticlesMarque(Marque marque, List<Article> articles) {
        this.marque = marque;
        
        // Mettre à jour le titre
        JPanel headerPanel = (JPanel) getComponent(0);
        JLabel titleLabel = (JLabel) headerPanel.getComponent(1);
        titleLabel.setText("Articles de " + marque.getNom());
        
        // Créer un panneau pour les articles avec GridLayout
        JPanel articlesPanel = new JPanel(new GridLayout(0, 3, 20, 20));
        articlesPanel.setBackground(Color.decode("#F5F5F5"));
        
        // Formatteur de prix
        NumberFormat formatPrix = NumberFormat.getCurrencyInstance(Locale.FRANCE);
        
        // Ajouter les cartes pour chaque article
        for (Article article : articles) {
            JPanel card = createArticleCard(article, formatPrix);
            articlesPanel.add(card);
        }
        
        // Créer un JScrollPane pour le panneau des articles
        JScrollPane scrollPane = new JScrollPane(articlesPanel);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        // Supprimer l'ancien contenu central s'il existe
        if (getComponentCount() > 1) {
            remove(1);
        }
        
        // Ajouter le panneau des articles au centre
        add(scrollPane, BorderLayout.CENTER);
        
        // Forcer le rafraîchissement
        revalidate();
        repaint();
    }
    
    private JPanel createArticleCard(Article article, NumberFormat formatPrix) {
        // Créer une carte pour l'article
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.decode("#dee2e6")),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        // Image de l'article
        JLabel imageLabel = new JLabel();
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        imageLabel.setPreferredSize(new Dimension(150, 150));
        imageLabel.setMinimumSize(new Dimension(150, 150));
        imageLabel.setMaximumSize(new Dimension(150, 150));
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        
        // Charger l'image de l'article
        try {
            String imagePath = article.getUrlImage();
            ImageIcon imageIcon = null;
            
            // Vérifier si l'URL est au format nom.jpg
            if (imagePath.endsWith(".jpg") || imagePath.endsWith(".png") || imagePath.endsWith(".gif")) {
                // Construire le chemin complet vers l'image
                String fullPath = "src/view/images/" + imagePath;
                
                // Essayer de charger l'image depuis le chemin relatif
                java.io.File file = new java.io.File(fullPath);
                if (file.exists()) {
                    imageIcon = new ImageIcon(fullPath);
                } else {
                    // Essayer avec le chemin absolu du projet
                    String projectPath = System.getProperty("user.dir");
                    fullPath = projectPath + "/src/view/images/" + imagePath;
                    file = new java.io.File(fullPath);
                    if (file.exists()) {
                        imageIcon = new ImageIcon(fullPath);
                    }
                }
            } else {
                // Utiliser l'URL telle quelle
                imageIcon = new ImageIcon(imagePath);
            }
            
            // Si l'image a été chargée, la redimensionner
            if (imageIcon != null && imageIcon.getIconWidth() > 0) {
                Image img = imageIcon.getImage();
                Image resizedImg = img.getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                imageIcon = new ImageIcon(resizedImg);
                imageLabel.setIcon(imageIcon);
            } else {
                // Utiliser un texte de remplacement si l'image n'a pas pu être chargée
                imageLabel.setText(article.getNom().substring(0, 1).toUpperCase());
                imageLabel.setFont(new Font("Arial", Font.BOLD, 48));
                imageLabel.setForeground(Color.WHITE);
                imageLabel.setBackground(Color.decode("#007bff"));
                imageLabel.setOpaque(true);
            }
        } catch (Exception e) {
            // Utiliser un placeholder si l'image ne peut pas être chargée
            imageLabel.setText(article.getNom().substring(0, 1).toUpperCase());
            imageLabel.setFont(new Font("Arial", Font.BOLD, 48));
            imageLabel.setForeground(Color.WHITE);
            imageLabel.setBackground(Color.decode("#007bff"));
            imageLabel.setOpaque(true);
            System.err.println("Erreur lors du chargement de l'image pour " + article.getNom() + ": " + e.getMessage());
            e.printStackTrace();
        }
        
        // Nom de l'article
        JLabel nomLabel = new JLabel(article.getNom());
        nomLabel.setFont(new Font("Arial", Font.BOLD, 16));
        nomLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Prix de l'article
        JPanel prixPanel = new JPanel();
        prixPanel.setLayout(new BoxLayout(prixPanel, BoxLayout.Y_AXIS));
        prixPanel.setBackground(Color.WHITE);
        prixPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Vérifier si l'article a une promotion
        if (article.getPromotion() != null) {
            // Prix après promotion
            JLabel prixPromoLabel = new JLabel(formatPrix.format(article.getPrixApresPromotion()));
            prixPromoLabel.setFont(new Font("Arial", Font.BOLD, 16));
            prixPromoLabel.setForeground(Color.decode("#dc3545"));
            prixPromoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            // Prix original barré
            JLabel prixOriginalLabel = new JLabel("<html><strike>" + formatPrix.format(article.getPrixUnitaire()) + "</strike></html>");
            prixOriginalLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            prixOriginalLabel.setForeground(Color.GRAY);
            prixOriginalLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            // Pourcentage de réduction
            JLabel reductionLabel = new JLabel("-" + article.getPromotion().getPourcentage() + "%");
            reductionLabel.setFont(new Font("Arial", Font.BOLD, 14));
            reductionLabel.setForeground(Color.decode("#28a745"));
            reductionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            prixPanel.add(prixPromoLabel);
            prixPanel.add(prixOriginalLabel);
            prixPanel.add(reductionLabel);
        } else {
            // Prix standard
            JLabel prixLabel = new JLabel(formatPrix.format(article.getPrixUnitaire()));
            prixLabel.setFont(new Font("Arial", Font.BOLD, 16));
            prixLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            prixPanel.add(prixLabel);
        }
        
        // Ajouter les composants à la carte
        card.add(imageLabel);
        card.add(Box.createVerticalStrut(10));
        card.add(nomLabel);
        card.add(Box.createVerticalStrut(5));
        card.add(prixPanel);
        
        // Ajouter un écouteur de clic sur la carte
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (articleClickListener != null) {
                    articleClickListener.onArticleClick(article);
                }
            }
            
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                card.setBackground(Color.decode("#f8f9fa"));
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                card.setBackground(Color.WHITE);
            }
        });
        
        return card;
    }
    
    public void setArticleClickListener(ArticleClickListener listener) {
        this.articleClickListener = listener;
    }
    
    public void setRetourListener(ActionListener listener) {
        retourButton.addActionListener(listener);
    }
    
    public Marque getMarque() {
        return marque;
    }
}
