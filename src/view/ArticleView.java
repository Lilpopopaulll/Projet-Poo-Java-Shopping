package view;

import model.Article;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.font.TextAttribute;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import controller.ArticleClickListener;

public class ArticleView extends JPanel {
    private JPanel articlePanel;
    private ArticleClickListener clickListener;
    private JComboBox<String> categorieComboBox;
    private ActionListener categoryFilterListener;

    public ArticleView() {
        setLayout(new BorderLayout());
        setBackground(Color.decode("#F5F5F5"));
    }

    public void setArticleClickListener(ArticleClickListener listener) {
        this.clickListener = listener;
    }
    
    public void setCategoryFilterListener(ActionListener listener) {
        this.categoryFilterListener = listener;
        
        // Si le combobox existe déjà, lui ajouter l'écouteur
        if (categorieComboBox != null) {
            categorieComboBox.removeActionListener(categoryFilterListener); // Éviter les doublons
            categorieComboBox.addActionListener(categoryFilterListener);
        }
    }
    
    public void setCategories(List<String> categories) {
        // Si le combobox n'existe pas encore, il sera créé lors de l'affichage des articles
        if (categorieComboBox != null) {
            categorieComboBox.removeAllItems();
            categorieComboBox.addItem("Toutes les catégories");
            
            for (String categorie : categories) {
                categorieComboBox.addItem(categorie);
            }
        }
    }
    
    public String getSelectedCategory() {
        if (categorieComboBox != null && categorieComboBox.getSelectedIndex() > 0) {
            return (String) categorieComboBox.getSelectedItem();
        }
        return null; // Aucune catégorie sélectionnée ou "Toutes les catégories"
    }

    public void afficherArticles(List<Article> articles) {
        removeAll();

        JPanel containerPanel = new JPanel();
        containerPanel.setLayout(new BoxLayout(containerPanel, BoxLayout.Y_AXIS));
        containerPanel.setBackground(Color.decode("#F5F5F5"));

        int sideMargin = 30;

        // Panel pour le titre et le filtre
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.decode("#F5F5F5"));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, sideMargin, 10, sideMargin));

        // Titre
        JLabel titleLabel = new JLabel("Nos Vêtements");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        // Filtre par catégorie
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        filterPanel.setBackground(Color.decode("#F5F5F5"));
        
        JLabel filterLabel = new JLabel("Filtrer par catégorie:");
        filterLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        
        // Créer ou réutiliser le combobox
        if (categorieComboBox == null) {
            categorieComboBox = new JComboBox<>();
            categorieComboBox.addItem("Toutes les catégories");
            
            // Ajouter l'écouteur s'il existe
            if (categoryFilterListener != null) {
                categorieComboBox.addActionListener(categoryFilterListener);
            }
        }
        
        filterPanel.add(filterLabel);
        filterPanel.add(categorieComboBox);
        
        headerPanel.add(filterPanel, BorderLayout.EAST);
        
        containerPanel.add(headerPanel);

        articlePanel = new JPanel(new GridBagLayout());
        articlePanel.setBackground(Color.decode("#F5F5F5"));
        articlePanel.setBorder(BorderFactory.createEmptyBorder(0, sideMargin, 25, sideMargin));

        int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
        int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
        
        int maxCardWidth = 300; 
        int cardWidth = Math.min((screenWidth - 2 * sideMargin - 60) / 4, maxCardWidth);
        int cardHeight = (int) (screenHeight * 0.40);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;
        
        int col = 0;
        int row = 0;
        
        for (Article article : articles) {
            JPanel card = new JPanel(new BorderLayout());
            card.setBackground(Color.WHITE);
            card.setPreferredSize(new Dimension(cardWidth, cardHeight));
            card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                    BorderFactory.createEmptyBorder(10, 10, 10, 10)
            ));

            JLabel imageLabel = new JLabel();
            imageLabel.setHorizontalAlignment(JLabel.CENTER);
            try {
                String imagePath = "src/view/images/" + article.getUrlImage();
                ImageIcon icon = new ImageIcon(imagePath);
                Image image = icon.getImage().getScaledInstance(cardWidth - 40, cardHeight / 2, Image.SCALE_SMOOTH);
                imageLabel.setIcon(new ImageIcon(image));
            } catch (Exception e) {
                imageLabel.setText("Image manquante");
            }

            JLabel nomLabel = new JLabel(article.getNom());
            nomLabel.setFont(new Font("Arial", Font.BOLD, 16));

            JPanel prixPanel = new JPanel();
            prixPanel.setLayout(new BoxLayout(prixPanel, BoxLayout.Y_AXIS));
            prixPanel.setOpaque(false);
            
            if (article.getStock() <= 0) {
                JLabel prixLabel = new JLabel("Rupture de stock");
                prixLabel.setForeground(Color.RED);
                prixPanel.add(prixLabel);
            } else if (article.getPromotion() != null) {
                // Afficher le prix original barré en rouge
                JLabel prixOriginalLabel = new JLabel(String.format("%.2f €", article.getPrixUnitaire()));
                prixOriginalLabel.setForeground(Color.RED);
                
                // Appliquer le style barré
                Map<TextAttribute, Object> attributes = new HashMap<>();
                attributes.put(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
                prixOriginalLabel.setFont(prixOriginalLabel.getFont().deriveFont(attributes));
                
                // Calculer le prix après promotion
                double prixPromo = article.getPromotion().calculerPrixPromo(article.getPrixUnitaire());
                JLabel prixPromoLabel = new JLabel(String.format("%.2f € (-%d%%)", 
                        prixPromo, article.getPromotion().getPourcentage()));
                prixPromoLabel.setForeground(new Color(0, 150, 0)); // Vert pour le prix en promo
                prixPromoLabel.setFont(new Font("Arial", Font.BOLD, 14));
                
                prixPanel.add(prixOriginalLabel);
                prixPanel.add(prixPromoLabel);
            } else {
                JLabel prixLabel = new JLabel(String.format("%.2f €", article.getPrixUnitaire()));
                prixLabel.setFont(new Font("Arial", Font.PLAIN, 14));
                prixPanel.add(prixLabel);
            }
            
            // Ajouter la catégorie
            if (article.getCategorie() != null && !article.getCategorie().isEmpty()) {
                JLabel categorieLabel = new JLabel(article.getCategorie());
                categorieLabel.setFont(new Font("Arial", Font.ITALIC, 12));
                categorieLabel.setForeground(Color.DARK_GRAY);
                prixPanel.add(Box.createVerticalStrut(5));
                prixPanel.add(categorieLabel);
            }

            JPanel bottom = new JPanel();
            bottom.setLayout(new BoxLayout(bottom, BoxLayout.Y_AXIS));
            bottom.setOpaque(false);
            bottom.add(nomLabel);
            bottom.add(prixPanel);
            nomLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            prixPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

            card.add(imageLabel, BorderLayout.CENTER);
            card.add(bottom, BorderLayout.SOUTH);

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
            } else {
                card.setBackground(new Color(245, 245, 245));
                card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                        BorderFactory.createEmptyBorder(10, 10, 10, 10)
                ));
            }

            gbc.gridx = col;
            gbc.gridy = row;
            articlePanel.add(card, gbc);
            
            col++;
            if (col >= 4) {
                col = 0;
                row++;
            }
        }
        
        // Message si aucun article n'est trouvé
        if (articles.isEmpty()) {
            JLabel emptyLabel = new JLabel("Aucun article trouvé dans cette catégorie");
            emptyLabel.setFont(new Font("Arial", Font.ITALIC, 16));
            emptyLabel.setForeground(Color.GRAY);
            emptyLabel.setHorizontalAlignment(JLabel.CENTER);
            articlePanel.add(emptyLabel, gbc);
        }

        containerPanel.add(articlePanel);

        add(containerPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }
}
