package view;

import model.Article;
import controller.ArticleClickListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class ArticleDetailView extends JPanel {
    private ArticleClickListener clickListener;
    private JButton addToCartButton;

    public ArticleDetailView() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
    }

    public void setArticleClickListener(ArticleClickListener listener) {
        this.clickListener = listener;
    }

    public void afficherDetailArticle(Article article) {
        removeAll();
        
        // Panel principal avec une marge
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 40, 40));
        
        // Bouton retour avec style amélioré
        JButton backButton = new JButton("← Retour");
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        backButton.setFocusPainted(false);
        backButton.setBackground(Color.decode("#f8f9fa"));
        backButton.setForeground(Color.decode("#495057"));
        backButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.decode("#dee2e6")),
                BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> {
            if (clickListener != null) {
                clickListener.onArticleClick(null); // signal de retour
            }
        });

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        topPanel.add(backButton, BorderLayout.WEST);
        topPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        // Titre avec style amélioré
        JLabel title = new JLabel(article.getNom(), SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 32));
        title.setForeground(Color.decode("#212529"));
        title.setBorder(BorderFactory.createEmptyBorder(10, 0, 30, 0));

        // Marque avec style
        JLabel marqueLabel = new JLabel(article.getMarque(), SwingConstants.CENTER);
        marqueLabel.setFont(new Font("Arial", Font.ITALIC, 18));
        marqueLabel.setForeground(Color.decode("#6c757d"));
        marqueLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        // Panel central avec image et informations
        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.X_AXIS));
        center.setBackground(Color.WHITE);
        center.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));

        // Panel pour l'image avec bordure et ombre
        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setBackground(Color.WHITE);
        imagePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.decode("#dee2e6"), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imageLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        try {
            String imagePath = "src/view/images/" + article.getUrlImage();
            ImageIcon icon = new ImageIcon(imagePath);
            Image image = icon.getImage().getScaledInstance(350, 350, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(image));
        } catch (Exception e) {
            imageLabel.setText("Image manquante");
            imageLabel.setFont(new Font("Arial", Font.ITALIC, 16));
            imageLabel.setForeground(Color.decode("#dc3545"));
        }

        imagePanel.add(imageLabel, BorderLayout.CENTER);

        // Panel d'informations avec style amélioré
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 0));

        // Prix avec style amélioré
        JLabel prixLabel = new JLabel("Prix unitaire: " + String.format("%.2f €", article.getPrixUnitaire() / 100.0));
        prixLabel.setFont(new Font("Arial", Font.BOLD, 20));
        prixLabel.setForeground(Color.decode("#212529"));
        prixLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        prixLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
        
        // Informations sur le prix en vrac
        JPanel vracPanel = new JPanel();
        vracPanel.setLayout(new BoxLayout(vracPanel, BoxLayout.Y_AXIS));
        vracPanel.setBackground(Color.decode("#f8f9fa"));
        vracPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.decode("#dee2e6"), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        vracPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        vracPanel.setMaximumSize(new Dimension(300, 100));
        
        JLabel vracTitleLabel = new JLabel("Offre spéciale en vrac :");
        vracTitleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        vracTitleLabel.setForeground(Color.decode("#28a745"));
        vracTitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel vracPriceLabel = new JLabel(String.format("%.2f € par unité à partir de %d unités", 
                article.getPrixVrac() / 100.0, article.getQuantiteVrac()));
        vracPriceLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        vracPriceLabel.setForeground(Color.decode("#212529"));
        vracPriceLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel economyLabel = new JLabel(String.format("Économisez %.2f € par unité", 
                (article.getPrixUnitaire() - article.getPrixVrac()) / 100.0));
        economyLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        economyLabel.setForeground(Color.decode("#dc3545"));
        economyLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        vracPanel.add(vracTitleLabel);
        vracPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        vracPanel.add(vracPriceLabel);
        vracPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        vracPanel.add(economyLabel);
        
        // Stock avec style
        JLabel stockLabel = new JLabel("En stock: " + article.getStock() + " unités");
        stockLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        stockLabel.setForeground(article.getStock() > 0 ? Color.decode("#28a745") : Color.decode("#dc3545"));
        stockLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        stockLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));

        // Spinner pour la quantité
        JPanel quantityPanel = new JPanel();
        quantityPanel.setLayout(new BoxLayout(quantityPanel, BoxLayout.X_AXIS));
        quantityPanel.setBackground(Color.WHITE);
        quantityPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        quantityPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        JLabel quantityLabel = new JLabel("Quantité: ");
        quantityLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        quantityLabel.setForeground(Color.decode("#495057"));
        
        SpinnerModel spinnerModel = new SpinnerNumberModel(1, 1, article.getStock(), 1);
        JSpinner quantitySpinner = new JSpinner(spinnerModel);
        quantitySpinner.setPreferredSize(new Dimension(80, 30));
        ((JSpinner.DefaultEditor) quantitySpinner.getEditor()).getTextField().setFont(new Font("Arial", Font.PLAIN, 14));
        
        quantityPanel.add(quantityLabel);
        quantityPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        quantityPanel.add(quantitySpinner);
        
        // Bouton ajouter au panier
        addToCartButton = new JButton("Ajouter au panier");
        addToCartButton.setFont(new Font("Arial", Font.BOLD, 16));
        addToCartButton.setForeground(Color.WHITE);
        addToCartButton.setBackground(Color.decode("#007bff"));
        addToCartButton.setFocusPainted(false);
        addToCartButton.setBorder(BorderFactory.createEmptyBorder(12, 25, 12, 25));
        addToCartButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addToCartButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Description du produit
        JLabel descriptionTitle = new JLabel("Description");
        descriptionTitle.setFont(new Font("Arial", Font.BOLD, 18));
        descriptionTitle.setForeground(Color.decode("#343a40"));
        descriptionTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        descriptionTitle.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));

        JTextArea descriptionArea = new JTextArea();
        descriptionArea.setText(article.getDescription() != null ? article.getDescription() : "Aucune description disponible.");
        descriptionArea.setFont(new Font("Arial", Font.PLAIN, 14));
        descriptionArea.setForeground(Color.decode("#495057"));
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setEditable(false);
        descriptionArea.setBackground(Color.WHITE);
        descriptionArea.setAlignmentX(Component.LEFT_ALIGNMENT);
        descriptionArea.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));

        // Ajouter les composants au panel d'informations
        infoPanel.add(prixLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        infoPanel.add(vracPanel);
        infoPanel.add(stockLabel);
        infoPanel.add(quantityPanel);
        infoPanel.add(addToCartButton);
        infoPanel.add(descriptionTitle);
        infoPanel.add(descriptionArea);

        // Ajouter les composants au panel central
        center.add(imagePanel);
        center.add(infoPanel);

        // Panel de titre
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(Color.WHITE);
        titlePanel.add(title, BorderLayout.NORTH);
        titlePanel.add(marqueLabel, BorderLayout.CENTER);

        // Assembler tous les panels
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(titlePanel, BorderLayout.CENTER);
        mainPanel.add(center, BorderLayout.CENTER);

        // Ajouter le panel principal avec scrolling
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        add(scrollPane, BorderLayout.CENTER);
        revalidate();
        repaint();
    }
    
    public void setAddToCartAction(ActionListener listener) {
        if (addToCartButton != null) {
            addToCartButton.addActionListener(listener);
        }
    }
}
