package view;

import model.Article;
import model.Marque;
import controller.ArticleClickListener;
import view.theme.AppTheme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.font.TextAttribute;
import java.util.HashMap;
import java.util.Map;

public class ArticleDetailView extends JPanel {
    private ArticleClickListener clickListener;
    private JButton addToCartButton;
    private JSpinner quantitySpinner; 
    private Article currentArticle; 

    public ArticleDetailView() {
        setLayout(new BorderLayout());
        setBackground(AppTheme.BACKGROUND_DARK);
    }

    public void setArticleClickListener(ArticleClickListener listener) {
        this.clickListener = listener;
    }

    /**
     * Affiche le détail d'un article sans information de marque spécifique
     * @param article L'article à afficher
     */
    public void afficherDetailArticle(Article article) {
        afficherDetailArticle(article, null);
    }
    
    /**
     * Affiche le détail d'un article avec les informations de marque provenant de la table articleMarque
     * @param article L'article à afficher
     * @param marqueAssociee La marque associée à l'article dans la table articleMarque (peut être null)
     */
    public void afficherDetailArticle(Article article, Marque marqueAssociee) {
        removeAll();
        
        this.currentArticle = article;
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(AppTheme.BACKGROUND_DARK);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 50, 50));
        
        JButton backButton = new JButton("← RETOUR");
        AppTheme.styleButton(backButton, false);
        backButton.addActionListener(e -> {
            if (clickListener != null) {
                clickListener.onArticleClick(null); 
            }
        });

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(AppTheme.BACKGROUND_DARK);
        topPanel.add(backButton, BorderLayout.WEST);
        topPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));

        JLabel title = new JLabel(article.getNom().toUpperCase(), SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 36));
        title.setForeground(AppTheme.TEXT_WHITE);
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));

        // Afficher la marque de l'article
        String marqueText = article.getMarque().toUpperCase();
        
        // Créer un panel pour afficher les informations de marque
        JPanel marquePanel = new JPanel();
        marquePanel.setLayout(new BoxLayout(marquePanel, BoxLayout.Y_AXIS));
        marquePanel.setBackground(AppTheme.BACKGROUND_DARK);
        marquePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel marqueLabel = new JLabel(marqueText, SwingConstants.CENTER);
        marqueLabel.setFont(new Font("Arial", Font.BOLD, 18));
        marqueLabel.setForeground(AppTheme.TEXT_GRAY);
        marqueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        marquePanel.add(marqueLabel);
        
        // Si une marque est associée via la table articleMarque, afficher cette information
        if (marqueAssociee != null) {
            JLabel marqueAssocieeLabel = new JLabel("Marque associée: " + marqueAssociee.getNom(), SwingConstants.CENTER);
            marqueAssocieeLabel.setFont(new Font("Arial", Font.ITALIC, 14));
            marqueAssocieeLabel.setForeground(AppTheme.ACCENT_COLOR);
            marqueAssocieeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            marquePanel.add(Box.createRigidArea(new Dimension(0, 5)));
            marquePanel.add(marqueAssocieeLabel);
        }
        
        marquePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));

        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.X_AXIS));
        center.setBackground(AppTheme.BACKGROUND_DARK);
        center.setBorder(BorderFactory.createEmptyBorder(0, 0, 40, 0));

        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setBackground(AppTheme.BACKGROUND_MEDIUM);
        imagePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppTheme.BACKGROUND_LIGHT, 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imageLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        try {
            String imagePath = "src/view/images/" + article.getUrlImage();
            ImageIcon icon = new ImageIcon(imagePath);
            Image image = icon.getImage().getScaledInstance(400, 400, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(image));
        } catch (Exception e) {
            imageLabel.setText("IMAGE NON DISPONIBLE");
            imageLabel.setFont(new Font("Arial", Font.ITALIC, 16));
            imageLabel.setForeground(AppTheme.TEXT_GRAY);
        }

        imagePanel.add(imageLabel, BorderLayout.CENTER);

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(AppTheme.BACKGROUND_DARK);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(0, 40, 0, 0));
        
        JLabel productTitle = new JLabel(article.getNom().toUpperCase());
        productTitle.setFont(new Font("Arial", Font.BOLD, 24));
        productTitle.setForeground(AppTheme.TEXT_WHITE);
        productTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Afficher le prix
        JPanel prixPanel = new JPanel();
        prixPanel.setLayout(new BoxLayout(prixPanel, BoxLayout.Y_AXIS));
        prixPanel.setBackground(AppTheme.BACKGROUND_DARK);
        prixPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        prixPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        
        // Vérifier si l'article a une promotion
        double prixAffiche = article.getPrixUnitaire();
        double promotion = 0;
        
        if (article.getPromotion() != null) {
            promotion = article.getPromotion().getPourcentage();
            prixAffiche = article.getPrixApresPromotion();
        }
        
        JLabel prixLabel = new JLabel(String.format("%.2f €", prixAffiche));
        prixLabel.setFont(new Font("Arial", Font.BOLD, 28));
        prixLabel.setForeground(AppTheme.TEXT_WHITE);
        prixLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        prixPanel.add(prixLabel);
        
        if (promotion > 0) {
            double prixOriginal = prixAffiche / (1 - promotion / 100);
            
            JLabel prixOriginalLabel = new JLabel(String.format("%.2f €", prixOriginal));
            prixOriginalLabel.setFont(new Font("Arial", Font.PLAIN, 18));
            prixOriginalLabel.setForeground(AppTheme.TEXT_GRAY);
            prixOriginalLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            
            // Appliquer un style barré au prix original
            Map<TextAttribute, Object> attributes = new HashMap<>();
            attributes.put(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
            prixOriginalLabel.setFont(prixOriginalLabel.getFont().deriveFont(attributes));
            
            JLabel promotionLabel = new JLabel(String.format("-%.0f%%", promotion));
            promotionLabel.setFont(new Font("Arial", Font.BOLD, 18));
            promotionLabel.setForeground(AppTheme.ACCENT_COLOR);
            promotionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            
            prixPanel.add(prixOriginalLabel);
            prixPanel.add(Box.createRigidArea(new Dimension(0, 5)));
            prixPanel.add(promotionLabel);
        }
        
        // Afficher les informations de vrac si disponibles
        JPanel vracPanel = new JPanel();
        vracPanel.setLayout(new BoxLayout(vracPanel, BoxLayout.Y_AXIS));
        vracPanel.setBackground(AppTheme.BACKGROUND_DARK);
        vracPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        vracPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        if (article.getQuantiteVrac() > 0) {
            JLabel vracTitleLabel = new JLabel("OFFRE VRAC");
            vracTitleLabel.setFont(new Font("Arial", Font.BOLD, 16));
            vracTitleLabel.setForeground(AppTheme.ACCENT_COLOR);
            vracTitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            
            JLabel vracDescLabel = new JLabel(
                String.format("Achetez par lot de %d pour %.2f € l'unité", 
                    article.getQuantiteVrac(), article.getPrixVrac())
            );
            vracDescLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            vracDescLabel.setForeground(AppTheme.TEXT_WHITE);
            vracDescLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            
            vracPanel.add(vracTitleLabel);
            vracPanel.add(Box.createRigidArea(new Dimension(0, 5)));
            vracPanel.add(vracDescLabel);
        }
        
        // Afficher le stock
        JLabel stockLabel = new JLabel();
        if (article.getStock() > 0) {
            stockLabel.setText(String.format("En stock: %d", article.getStock()));
            stockLabel.setForeground(new Color(0, 150, 0));
        } else {
            stockLabel.setText("RUPTURE DE STOCK");
            stockLabel.setForeground(new Color(200, 0, 0));
        }
        stockLabel.setFont(new Font("Arial", Font.BOLD, 16));
        stockLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        stockLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        // Sélecteur de quantité
        JPanel quantityPanel = new JPanel();
        quantityPanel.setLayout(new BoxLayout(quantityPanel, BoxLayout.X_AXIS));
        quantityPanel.setBackground(AppTheme.BACKGROUND_DARK);
        quantityPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        quantityPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        JLabel quantityLabel = new JLabel("Quantité:");
        quantityLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        quantityLabel.setForeground(AppTheme.TEXT_WHITE);
        
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(1, 1, article.getStock(), 1);
        quantitySpinner = new JSpinner(spinnerModel); 
        quantitySpinner.setPreferredSize(new Dimension(80, 30));
        ((JSpinner.DefaultEditor) quantitySpinner.getEditor()).getTextField().setFont(new Font("Arial", Font.PLAIN, 14));
        ((JSpinner.DefaultEditor) quantitySpinner.getEditor()).getTextField().setBackground(AppTheme.BACKGROUND_MEDIUM);
        ((JSpinner.DefaultEditor) quantitySpinner.getEditor()).getTextField().setForeground(AppTheme.TEXT_WHITE);
        
        quantityPanel.add(quantityLabel);
        quantityPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        quantityPanel.add(quantitySpinner);
        
        addToCartButton = new JButton("AJOUTER AU PANIER");
        AppTheme.styleButton(addToCartButton, true);
        addToCartButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel descriptionTitle = new JLabel("DESCRIPTION");
        descriptionTitle.setFont(new Font("Arial", Font.BOLD, 18));
        descriptionTitle.setForeground(AppTheme.TEXT_WHITE);
        descriptionTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        descriptionTitle.setBorder(BorderFactory.createEmptyBorder(30, 0, 15, 0));

        JTextArea descriptionArea = new JTextArea();
        descriptionArea.setText(article.getDescription() != null ? article.getDescription() : "Aucune description disponible.");
        descriptionArea.setFont(new Font("Arial", Font.PLAIN, 14));
        descriptionArea.setForeground(AppTheme.TEXT_GRAY);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setEditable(false);
        descriptionArea.setBackground(AppTheme.BACKGROUND_DARK);
        descriptionArea.setAlignmentX(Component.LEFT_ALIGNMENT);
        descriptionArea.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));

        infoPanel.add(productTitle);
        infoPanel.add(prixPanel);
        infoPanel.add(vracPanel);
        infoPanel.add(stockLabel);
        
        if (article.getStock() > 0) {
            infoPanel.add(quantityPanel);
            infoPanel.add(addToCartButton);
        } else {
            JButton notifyButton = new JButton("NOTIFIER QUAND DISPONIBLE");
            AppTheme.styleButton(notifyButton, false);
            notifyButton.setAlignmentX(Component.LEFT_ALIGNMENT);
            infoPanel.add(notifyButton);
        }
        
        infoPanel.add(descriptionTitle);
        infoPanel.add(descriptionArea);

        center.add(imagePanel);
        center.add(infoPanel);

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(AppTheme.BACKGROUND_DARK);
        titlePanel.add(title, BorderLayout.NORTH);
        titlePanel.add(marquePanel, BorderLayout.CENTER);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(center, BorderLayout.CENTER);

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getViewport().setBackground(AppTheme.BACKGROUND_DARK);
        
        scrollPane.getVerticalScrollBar().setUI(new view.theme.ModernScrollBarUI());

        add(scrollPane, BorderLayout.CENTER);
        revalidate();
        repaint();
    }
    
    public void setAddToCartAction(ActionListener listener) {
        if (addToCartButton != null) {
            for (ActionListener al : addToCartButton.getActionListeners()) {
                addToCartButton.removeActionListener(al);
            }
            addToCartButton.addActionListener(listener);
        }
    }
    
    public int getSelectedQuantity() {
        if (quantitySpinner != null) {
            return (Integer) quantitySpinner.getValue();
        }
        return 1; 
    }
    
    public Article getCurrentArticle() {
        return currentArticle;
    }
}
