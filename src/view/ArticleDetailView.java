package view;

import model.Article;
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

    public void afficherDetailArticle(Article article) {
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

        JLabel marqueLabel = new JLabel(article.getMarque().toUpperCase(), SwingConstants.CENTER);
        marqueLabel.setFont(new Font("Arial", Font.BOLD, 18));
        marqueLabel.setForeground(AppTheme.TEXT_GRAY);
        marqueLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));

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
        
        JPanel prixPanel = new JPanel();
        prixPanel.setLayout(new BoxLayout(prixPanel, BoxLayout.X_AXIS));
        prixPanel.setBackground(AppTheme.BACKGROUND_DARK);
        prixPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        prixPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        
        if (article.getStock() <= 0) {
            JLabel prixLabel = new JLabel("ÉPUISÉ");
            prixLabel.setFont(new Font("Arial", Font.BOLD, 18));
            prixLabel.setForeground(new Color(220, 53, 69));
            prixPanel.add(prixLabel);
        } else if (article.getPromotion() != null) {
            JLabel prixOriginalLabel = new JLabel(String.format("%.2f €", article.getPrixUnitaire()));
            prixOriginalLabel.setForeground(AppTheme.TEXT_GRAY);
            prixOriginalLabel.setFont(new Font("Arial", Font.PLAIN, 18));
            
            Map<TextAttribute, Object> attributes = new HashMap<>();
            attributes.put(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
            prixOriginalLabel.setFont(prixOriginalLabel.getFont().deriveFont(attributes));
            
            double prixPromo = article.getPromotion().calculerPrixPromo(article.getPrixUnitaire());
            JLabel prixPromoLabel = new JLabel(String.format(" %.2f €", prixPromo));
            prixPromoLabel.setForeground(AppTheme.TEXT_WHITE);
            prixPromoLabel.setFont(new Font("Arial", Font.BOLD, 22));
            
            prixPanel.add(prixOriginalLabel);
            prixPanel.add(Box.createRigidArea(new Dimension(10, 0)));
            prixPanel.add(prixPromoLabel);
        } else {
            JLabel prixLabel = new JLabel(String.format("%.2f €", article.getPrixUnitaire()));
            prixLabel.setFont(new Font("Arial", Font.BOLD, 22));
            prixLabel.setForeground(AppTheme.TEXT_WHITE);
            prixPanel.add(prixLabel);
        }
        
        JPanel vracPanel = new JPanel();
        vracPanel.setLayout(new BoxLayout(vracPanel, BoxLayout.Y_AXIS));
        vracPanel.setBackground(AppTheme.BACKGROUND_DARK);
        vracPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        vracPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        System.out.println("Article: " + article.getNom() + ", Quantité vrac: " + article.getQuantiteVrac() + ", Prix vrac: " + article.getPrixVrac());
        
        if (article.getQuantiteVrac() > 0) {
            JLabel vracTitleLabel = new JLabel("VENTE EN VRAC");
            vracTitleLabel.setFont(new Font("Arial", Font.BOLD, 16));
            vracTitleLabel.setForeground(AppTheme.TEXT_WHITE);
            vracTitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            
            JLabel vracQuantityLabel = new JLabel(String.format("À PARTIR DE %d UNITÉS", article.getQuantiteVrac()));
            vracQuantityLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            vracQuantityLabel.setForeground(AppTheme.TEXT_GRAY);
            vracQuantityLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            
            JLabel vracPriceLabel = new JLabel(String.format("PRIX UNITAIRE: %.2f €", article.getPrixVrac()));
            vracPriceLabel.setFont(new Font("Arial", Font.BOLD, 14));
            vracPriceLabel.setForeground(AppTheme.TEXT_WHITE);
            vracPriceLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            
            double economie = article.getPrixUnitaire() - article.getPrixVrac();
            JLabel economyLabel = new JLabel(String.format("ÉCONOMIE: %.2f € PAR UNITÉ", economie));
            economyLabel.setFont(new Font("Arial", Font.ITALIC, 14));
            economyLabel.setForeground(new Color(40, 167, 69)); 
            economyLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            
            vracPanel.add(vracTitleLabel);
            vracPanel.add(Box.createRigidArea(new Dimension(0, 5)));
            vracPanel.add(vracQuantityLabel);
            vracPanel.add(Box.createRigidArea(new Dimension(0, 5)));
            vracPanel.add(vracPriceLabel);
            vracPanel.add(Box.createRigidArea(new Dimension(0, 5)));
            vracPanel.add(economyLabel);
        } else {
            JLabel noVracLabel = new JLabel("PAS DE VENTE EN VRAC DISPONIBLE");
            noVracLabel.setFont(new Font("Arial", Font.ITALIC, 14));
            noVracLabel.setForeground(AppTheme.TEXT_GRAY);
            noVracLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            vracPanel.add(noVracLabel);
        }
        
        JLabel stockLabel = new JLabel(article.getStock() > 0 
                ? "EN STOCK: " + article.getStock() + " UNITÉS" 
                : "RUPTURE DE STOCK");
        stockLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        stockLabel.setForeground(article.getStock() > 0 ? new Color(40, 167, 69) : new Color(220, 53, 69));
        stockLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        stockLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        JPanel quantityPanel = new JPanel();
        quantityPanel.setLayout(new BoxLayout(quantityPanel, BoxLayout.X_AXIS));
        quantityPanel.setBackground(AppTheme.BACKGROUND_DARK);
        quantityPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        quantityPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 25, 0));
        
        JLabel quantityLabel = new JLabel("QUANTITÉ: ");
        quantityLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        quantityLabel.setForeground(AppTheme.TEXT_WHITE);
        
        SpinnerModel spinnerModel = new SpinnerNumberModel(1, 1, article.getStock(), 1);
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
        titlePanel.add(marqueLabel, BorderLayout.CENTER);

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
