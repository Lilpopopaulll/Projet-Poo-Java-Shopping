package view;

import model.Article;
import view.theme.AppTheme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.font.TextAttribute;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import controller.ArticleClickListener;
import java.io.File;

public class ArticleView extends JPanel {
    private JPanel articlePanel;
    private ArticleClickListener clickListener;
    private JComboBox<String> categorieComboBox;
    private JComboBox<String> marqueComboBox;
    private JComboBox<String> prixComboBox;
    private ActionListener categoryFilterListener;
    private ActionListener marqueFilterListener;
    private ActionListener prixFilterListener;
    private List<String> categories;
    private List<String> marques;

    public ArticleView() {
        setLayout(new BorderLayout());
        setBackground(AppTheme.BACKGROUND_DARK);
        this.categories = new ArrayList<>();
        this.marques = new ArrayList<>();
    }

    public void setArticleClickListener(ArticleClickListener listener) {
        this.clickListener = listener;
    }
    
    public void setCategoryFilterListener(ActionListener listener) {
        this.categoryFilterListener = listener;
        
        if (categorieComboBox != null) {
            categorieComboBox.removeActionListener(categoryFilterListener); 
            categorieComboBox.addActionListener(categoryFilterListener);
        }
    }
    
    public void setMarqueFilterListener(ActionListener listener) {
        this.marqueFilterListener = listener;
        
        if (marqueComboBox != null) {
            marqueComboBox.removeActionListener(marqueFilterListener); 
            marqueComboBox.addActionListener(marqueFilterListener);
        }
    }
    
    public void setPrixFilterListener(ActionListener listener) {
        this.prixFilterListener = listener;
        
        if (prixComboBox != null) {
            prixComboBox.removeActionListener(prixFilterListener); 
            prixComboBox.addActionListener(prixFilterListener);
        }
    }
    
    public void setCategories(List<String> categories) {
        this.categories = new ArrayList<>(categories);
        
        if (categorieComboBox != null) {
            updateCategoriesComboBox();
        }
    }
    
    public void setMarques(List<String> marques) {
        this.marques = new ArrayList<>(marques);
        
        if (marqueComboBox != null) {
            updateMarquesComboBox();
        }
    }
    
    private void updateCategoriesComboBox() {
        if (categorieComboBox != null) {
            categorieComboBox.removeAllItems();
            categorieComboBox.addItem("TOUTES LES CATÉGORIES");
            
            for (String categorie : categories) {
                categorieComboBox.addItem(categorie.toUpperCase());
            }
        }
    }
    
    private void updateMarquesComboBox() {
        if (marqueComboBox != null) {
            marqueComboBox.removeAllItems();
            marqueComboBox.addItem("TOUTES LES MARQUES");
            
            for (String marque : marques) {
                marqueComboBox.addItem(marque.toUpperCase());
            }
        }
    }
    
    public String getSelectedCategory() {
        if (categorieComboBox != null && categorieComboBox.getSelectedIndex() > 0) {
            return ((String) categorieComboBox.getSelectedItem()).toLowerCase();
        }
        return null; 
    }

    public String getSelectedMarque() {
        if (marqueComboBox != null && marqueComboBox.getSelectedIndex() > 0) {
            return ((String) marqueComboBox.getSelectedItem()).toLowerCase();
        }
        return null; 
    }
    
    public JComboBox<String> getPrixComboBox() {
        return prixComboBox;
    }

    public void afficherArticles(List<Article> articles) {
        removeAll();

        JPanel containerPanel = new JPanel();
        containerPanel.setLayout(new BoxLayout(containerPanel, BoxLayout.Y_AXIS));
        containerPanel.setBackground(AppTheme.BACKGROUND_DARK);

        int sideMargin = 40;

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(AppTheme.BACKGROUND_DARK);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(30, sideMargin, 30, sideMargin));

        JLabel titleLabel = new JLabel("NOS ARTICLES");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(AppTheme.TEXT_WHITE);
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        filterPanel.setBackground(AppTheme.BACKGROUND_DARK);
        
        JLabel filterLabel = new JLabel("FILTRER:");
        filterLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        filterLabel.setForeground(AppTheme.TEXT_GRAY);
        
        if (categorieComboBox == null) {
            categorieComboBox = new JComboBox<>();
            
            categorieComboBox.setBackground(AppTheme.BACKGROUND_MEDIUM);
            categorieComboBox.setForeground(AppTheme.TEXT_WHITE);
            categorieComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
            
            updateCategoriesComboBox();
            
            if (categoryFilterListener != null) {
                categorieComboBox.addActionListener(categoryFilterListener);
            }
        }
        
        if (marqueComboBox == null) {
            marqueComboBox = new JComboBox<>();
            
            marqueComboBox.setBackground(AppTheme.BACKGROUND_MEDIUM);
            marqueComboBox.setForeground(AppTheme.TEXT_WHITE);
            marqueComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
            
            updateMarquesComboBox();
            
            if (marqueFilterListener != null) {
                marqueComboBox.addActionListener(marqueFilterListener);
            }
        }
        
        if (prixComboBox == null) {
            prixComboBox = new JComboBox<>();
            
            prixComboBox.setBackground(AppTheme.BACKGROUND_MEDIUM);
            prixComboBox.setForeground(AppTheme.TEXT_WHITE);
            prixComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
            
            prixComboBox.addItem("TOUTES LES PRIX");
            prixComboBox.addItem("0-50€");
            prixComboBox.addItem("50-100€");
            prixComboBox.addItem("100-200€");
            prixComboBox.addItem("200€+");
            
            if (prixFilterListener != null) {
                prixComboBox.addActionListener(prixFilterListener);
            }
        }
        
        filterPanel.add(filterLabel);
        filterPanel.add(categorieComboBox);
        filterPanel.add(marqueComboBox);
        filterPanel.add(prixComboBox);
        
        headerPanel.add(filterPanel, BorderLayout.EAST);
        
        containerPanel.add(headerPanel);

        JPanel descriptionPanel = new JPanel();
        descriptionPanel.setLayout(new BoxLayout(descriptionPanel, BoxLayout.Y_AXIS));
        descriptionPanel.setBackground(AppTheme.BACKGROUND_DARK);
        descriptionPanel.setBorder(BorderFactory.createEmptyBorder(0, sideMargin, 40, sideMargin));
        
        JLabel sloganLabel = new JLabel("TROUVEZ DES ARTICLES UNIQUES");
        sloganLabel.setFont(new Font("Arial", Font.BOLD, 24));
        sloganLabel.setForeground(AppTheme.TEXT_WHITE);
        sloganLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JTextArea descriptionText = new JTextArea(
    "Nos créations allient style urbain et savoir-faire haut de gamme " +
    "pour offrir un look unique qui vous distingue de la foule."
);

        descriptionText.setFont(new Font("Arial", Font.PLAIN, 14));
        descriptionText.setForeground(AppTheme.TEXT_GRAY);
        descriptionText.setBackground(AppTheme.BACKGROUND_DARK);
        descriptionText.setWrapStyleWord(true);
        descriptionText.setLineWrap(true);
        descriptionText.setEditable(false);
        descriptionText.setAlignmentX(Component.LEFT_ALIGNMENT);
        descriptionText.setMaximumSize(new Dimension(600, 100));
        
        descriptionPanel.add(sloganLabel);
        descriptionPanel.add(Box.createVerticalStrut(15));
        descriptionPanel.add(descriptionText);
        
        containerPanel.add(descriptionPanel);

        articlePanel = new JPanel(new GridBagLayout());
        articlePanel.setBackground(AppTheme.BACKGROUND_DARK);
        articlePanel.setBorder(BorderFactory.createEmptyBorder(0, sideMargin, 40, sideMargin));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        
        int col = 0;
        int row = 0;
        
        for (Article article : articles) {
            JPanel card = new JPanel(new BorderLayout(0, 10));
            card.setBackground(AppTheme.BACKGROUND_MEDIUM);
            card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(AppTheme.BACKGROUND_LIGHT, 1),
                    BorderFactory.createEmptyBorder(0, 0, 15, 0)
            ));
            
            JPanel imageContainer = new JPanel(new BorderLayout());
            imageContainer.setBackground(AppTheme.BACKGROUND_MEDIUM);
            imageContainer.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
            
            JLabel imageLabel = new JLabel();
            imageLabel.setHorizontalAlignment(JLabel.CENTER);
            imageLabel.setPreferredSize(new Dimension(220, 220));
            
            try {
                // Essayer de charger l'image de l'article
                ImageIcon imageIcon = null;
                
                // Construire le chemin de l'image
                String imagePath = article.getUrlImage();
                
                if (imagePath != null && !imagePath.isEmpty()) {
                    // Essayer de charger l'image spécifique de l'article
                    try {
                        java.net.URL imageUrl = getClass().getResource("/view/images/" + imagePath);
                        if (imageUrl != null) {
                            imageIcon = new ImageIcon(imageUrl);
                        } else {
                            // Essayer de charger l'image de la marque
                            String marqueImage = article.getMarque().toLowerCase().replace(" ", "_") + ".jpg";
                            java.net.URL marqueImageUrl = getClass().getResource("/view/images/" + marqueImage);
                            if (marqueImageUrl != null) {
                                imageIcon = new ImageIcon(marqueImageUrl);
                            } else {
                                // Essayer de charger une image par défaut basée sur la catégorie
                                String categoryFolder = "/view/images/categories/";
                                java.net.URL categoryUrl = getClass().getResource(categoryFolder);
                                
                                if (categoryUrl != null) {
                                    // Nous ne pouvons pas lister les fichiers dans un JAR directement
                                    // Essayons de charger une image par défaut
                                    java.net.URL defaultImageUrl = getClass().getResource(categoryFolder + "default.jpg");
                                    if (defaultImageUrl != null) {
                                        imageIcon = new ImageIcon(defaultImageUrl);
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        System.err.println("Erreur lors du chargement de l'image: " + e.getMessage());
                    }
                }
                
                // Si l'image a été chargée, la redimensionner
                if (imageIcon != null && imageIcon.getIconWidth() > 0) {
                    Image img = imageIcon.getImage();
                    Image resizedImg = img.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
                    imageIcon = new ImageIcon(resizedImg);
                    imageLabel.setIcon(imageIcon);
                } else {
                    // Utiliser un placeholder si aucune image n'est disponible
                    imageLabel.setText(article.getNom().substring(0, 1).toUpperCase());
                    imageLabel.setFont(new Font("Arial", Font.BOLD, 48));
                    imageLabel.setForeground(Color.WHITE);
                    imageLabel.setBackground(Color.decode("#007bff"));
                    imageLabel.setOpaque(true);
                }
            } catch (Exception e) {
                System.err.println("Erreur lors du chargement de l'image pour " + article.getNom() + ": " + e.getMessage());
                imageLabel.setText("NO IMAGE");
                imageLabel.setForeground(AppTheme.TEXT_GRAY);
                imageLabel.setFont(new Font("Arial", Font.ITALIC, 14));
            }
            
            imageContainer.add(imageLabel, BorderLayout.CENTER);
            
            JLabel nomLabel = new JLabel(article.getNom().toUpperCase());
            nomLabel.setFont(new Font("Arial", Font.BOLD, 14));
            nomLabel.setForeground(AppTheme.TEXT_WHITE);
            nomLabel.setBorder(BorderFactory.createEmptyBorder(10, 15, 5, 15));
            
            JPanel infoPanel = new JPanel();
            infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
            infoPanel.setBackground(AppTheme.BACKGROUND_MEDIUM);
            infoPanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));
            
            JPanel prixPanel = new JPanel();
            prixPanel.setLayout(new BoxLayout(prixPanel, BoxLayout.X_AXIS));
            prixPanel.setBackground(AppTheme.BACKGROUND_MEDIUM);
            
            if (article.getStock() <= 0) {
                JLabel prixLabel = new JLabel("ÉPUISÉ");
                prixLabel.setFont(new Font("Arial", Font.BOLD, 14));
                prixLabel.setForeground(new Color(220, 53, 69));
                prixPanel.add(prixLabel);
            } else if (article.getPromotion() != null) {
                JLabel prixOriginalLabel = new JLabel(String.format("%.2f €", article.getPrixUnitaire()));
                prixOriginalLabel.setForeground(AppTheme.TEXT_GRAY);
                prixOriginalLabel.setFont(new Font("Arial", Font.PLAIN, 14));
                
                Map<TextAttribute, Object> attributes = new HashMap<>();
                attributes.put(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
                prixOriginalLabel.setFont(prixOriginalLabel.getFont().deriveFont(attributes));
                
                double prixPromo = article.getPromotion().calculerPrixPromo(article.getPrixUnitaire());
                JLabel prixPromoLabel = new JLabel(String.format(" %.2f €", prixPromo));
                prixPromoLabel.setForeground(AppTheme.TEXT_WHITE);
                prixPromoLabel.setFont(new Font("Arial", Font.BOLD, 14));
                
                prixPanel.add(prixOriginalLabel);
                prixPanel.add(prixPromoLabel);
            } else {
                JLabel prixLabel = new JLabel(String.format("%.2f €", article.getPrixUnitaire()));
                prixLabel.setFont(new Font("Arial", Font.BOLD, 14));
                prixLabel.setForeground(AppTheme.TEXT_WHITE);
                prixPanel.add(prixLabel);
            }
            
            if (article.getCategorie() != null && !article.getCategorie().isEmpty()) {
                JLabel categorieLabel = new JLabel(article.getCategorie().toUpperCase());
                categorieLabel.setFont(new Font("Arial", Font.ITALIC, 12));
                categorieLabel.setForeground(AppTheme.TEXT_GRAY);
                
                JPanel categoriePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 5));
                categoriePanel.setBackground(AppTheme.BACKGROUND_MEDIUM);
                categoriePanel.add(categorieLabel);
                
                infoPanel.add(prixPanel);
                infoPanel.add(categoriePanel);
            } else {
                infoPanel.add(prixPanel);
            }
            
            JButton shopButton = new JButton("SHOP NOW");
            shopButton.setFont(new Font("Arial", Font.BOLD, 12));
            shopButton.setForeground(AppTheme.TEXT_WHITE);
            shopButton.setBackground(AppTheme.BACKGROUND_DARK);
            shopButton.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(AppTheme.TEXT_WHITE, 1),
                    BorderFactory.createEmptyBorder(5, 15, 5, 15)
            ));
            shopButton.setFocusPainted(false);
            shopButton.setAlignmentX(Component.LEFT_ALIGNMENT);
            shopButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            infoPanel.add(Box.createVerticalStrut(10));
            infoPanel.add(shopButton);
            
            card.add(imageContainer, BorderLayout.CENTER);
            card.add(nomLabel, BorderLayout.NORTH);
            card.add(infoPanel, BorderLayout.SOUTH);

            if (article.getStock() > 0) {
                card.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (clickListener != null) {
                            clickListener.onArticleClick(article);
                        }
                    }
                });
                card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                
                shopButton.addActionListener(e -> {
                    if (clickListener != null) {
                        clickListener.onArticleClick(article);
                    }
                });
            } else {
                card.setBackground(AppTheme.BACKGROUND_LIGHT);
                imageContainer.setBackground(AppTheme.BACKGROUND_LIGHT);
                infoPanel.setBackground(AppTheme.BACKGROUND_LIGHT);
                prixPanel.setBackground(AppTheme.BACKGROUND_LIGHT);
                shopButton.setEnabled(false);
                shopButton.setText("ÉPUISÉ");
            }

            gbc.gridx = col;
            gbc.gridy = row;
            articlePanel.add(card, gbc);
            
            col++;
            if (col >= 3) { 
                col = 0;
                row++;
            }
        }
        
        if (articles.isEmpty()) {
            JLabel emptyLabel = new JLabel("AUCUN ARTICLE TROUVÉ");
            emptyLabel.setFont(new Font("Arial", Font.ITALIC, 18));
            emptyLabel.setForeground(AppTheme.TEXT_GRAY);
            emptyLabel.setHorizontalAlignment(JLabel.CENTER);
            articlePanel.add(emptyLabel, gbc);
        }

        JScrollPane scrollPane = new JScrollPane(articlePanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBackground(AppTheme.BACKGROUND_DARK);
        scrollPane.getViewport().setBackground(AppTheme.BACKGROUND_DARK);
        
        scrollPane.getVerticalScrollBar().setUI(new view.theme.ModernScrollBarUI());
        
        containerPanel.add(scrollPane);

        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(AppTheme.BACKGROUND_MEDIUM);
        footerPanel.setLayout(new BorderLayout());
        footerPanel.setBorder(BorderFactory.createEmptyBorder(20, sideMargin, 20, sideMargin));
        
        JLabel footerLabel = new JLabel(" 2025 CUSTOMIZATION. ALL RIGHTS RESERVED.");
        footerLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        footerLabel.setForeground(AppTheme.TEXT_GRAY);
        footerPanel.add(footerLabel, BorderLayout.CENTER);
        
        add(containerPanel, BorderLayout.CENTER);
        add(footerPanel, BorderLayout.SOUTH);
        
        revalidate();
        repaint();
    }
}
