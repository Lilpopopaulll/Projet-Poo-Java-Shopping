package view;

import model.Article;
import model.Promotion;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Vue pour l'interface d'administration
 */
public class AdminView extends JPanel {
    private JTable articlesTable;
    private DefaultTableModel tableModel;
    private JButton ajouterButton;
    private JButton modifierButton;
    private JButton supprimerButton;
    private JButton gererPromoButton; // Bouton pour gérer les promotions
    private JTextField rechercheField;
    private JButton rechercheButton;
    private JButton articlesNavButton;
    private JButton usersNavButton;
    private JButton deconnexionButton;
    private JPanel contentPanel;
    private CardLayout cardLayout;
    private UserManagementView userManagementView;

    /**
     * Constructeur
     */
    public AdminView() {
        // Configuration du panneau
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        
        // Créer le panneau de contenu avec CardLayout pour la navigation
        contentPanel = new JPanel();
        cardLayout = new CardLayout();
        contentPanel.setLayout(cardLayout);
        
        // Créer le panneau de gestion des articles
        JPanel articlesPanel = createArticlesPanel();
        contentPanel.add(articlesPanel, "articles");
        
        // Créer le panneau de gestion des utilisateurs
        userManagementView = new UserManagementView();
        contentPanel.add(userManagementView, "users");
        
        // Créer les boutons de navigation
        articlesNavButton = new JButton("Gestion Articles");
        articlesNavButton.setBackground(new Color(0, 123, 255)); // Bleu pour le bouton actif par défaut
        articlesNavButton.setForeground(Color.WHITE);
        articlesNavButton.setFont(new Font("Arial", Font.BOLD, 14));
        articlesNavButton.setFocusPainted(false);
        
        usersNavButton = new JButton("Gestion Comptes");
        usersNavButton.setBackground(new Color(73, 80, 87)); // Gris pour le bouton inactif
        usersNavButton.setForeground(Color.WHITE);
        usersNavButton.setFont(new Font("Arial", Font.BOLD, 14));
        usersNavButton.setFocusPainted(false);
        
        // Afficher le panneau des articles par défaut
        cardLayout.show(contentPanel, "articles");
        
        // Ajouter le panneau de contenu au panneau principal
        add(contentPanel, BorderLayout.CENTER);
    }
    
    /**
     * Créer le panneau de gestion des articles
     * @return Le panneau créé
     */
    private JPanel createArticlesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        // Titre
        JLabel titleLabel = new JLabel("Gestion des Articles");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        panel.add(titleLabel, BorderLayout.NORTH);
        
        // Panneau pour les boutons d'action et la recherche
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        topPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        
        // Panneau pour les boutons d'action
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        actionPanel.setBackground(Color.WHITE);
        
        ajouterButton = new JButton("Ajouter");
        ajouterButton.setBackground(new Color(40, 167, 69)); // Vert
        ajouterButton.setForeground(Color.WHITE);
        ajouterButton.setFont(new Font("Arial", Font.BOLD, 14));
        ajouterButton.setFocusPainted(false);
        ajouterButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        actionPanel.add(ajouterButton);
        
        modifierButton = new JButton("Modifier");
        modifierButton.setBackground(new Color(255, 193, 7)); // Jaune
        modifierButton.setForeground(Color.BLACK);
        modifierButton.setFont(new Font("Arial", Font.BOLD, 14));
        modifierButton.setFocusPainted(false);
        modifierButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        modifierButton.setEnabled(false); // Désactivé par défaut
        actionPanel.add(modifierButton);
        
        supprimerButton = new JButton("Supprimer");
        supprimerButton.setBackground(new Color(220, 53, 69)); // Rouge
        supprimerButton.setForeground(Color.WHITE);
        supprimerButton.setFont(new Font("Arial", Font.BOLD, 14));
        supprimerButton.setFocusPainted(false);
        supprimerButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        supprimerButton.setEnabled(false); // Désactivé par défaut
        actionPanel.add(supprimerButton);
        
        gererPromoButton = new JButton("Gérer Promotion");
        gererPromoButton.setBackground(new Color(23, 162, 184)); // Bleu clair
        gererPromoButton.setForeground(Color.WHITE);
        gererPromoButton.setFont(new Font("Arial", Font.BOLD, 14));
        gererPromoButton.setFocusPainted(false);
        gererPromoButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        gererPromoButton.setEnabled(false); // Désactivé par défaut
        actionPanel.add(gererPromoButton);
        
        topPanel.add(actionPanel, BorderLayout.WEST);
        
        // Panneau pour la recherche
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setBackground(Color.WHITE);
        
        rechercheField = new JTextField(20);
        rechercheField.setFont(new Font("Arial", Font.PLAIN, 14));
        searchPanel.add(rechercheField);
        
        rechercheButton = new JButton("Rechercher");
        rechercheButton.setBackground(new Color(23, 162, 184)); // Bleu clair
        rechercheButton.setForeground(Color.WHITE);
        rechercheButton.setFont(new Font("Arial", Font.BOLD, 14));
        rechercheButton.setFocusPainted(false);
        rechercheButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        searchPanel.add(rechercheButton);
        
        topPanel.add(searchPanel, BorderLayout.EAST);
        panel.add(topPanel, BorderLayout.NORTH);
        
        // Création du modèle de table
        String[] colonnes = {"ID", "Nom", "Marque", "Prix Unitaire", "Prix Vrac", "Qté Vrac", "Stock", "Promotion"};
        tableModel = new DefaultTableModel(colonnes, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Table non éditable directement
            }
            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0 || columnIndex == 5 || columnIndex == 6) {
                    return Integer.class; // ID, Qté Vrac et Stock sont des entiers
                } else if (columnIndex == 3 || columnIndex == 4) {
                    return Double.class; // Prix sont des doubles
                } else if (columnIndex == 7) {
                    return String.class; // Promotion est une chaîne
                }
                return String.class;
            }
        };
        
        articlesTable = new JTable(tableModel);
        articlesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        articlesTable.getTableHeader().setReorderingAllowed(false);
        articlesTable.setRowHeight(30);
        articlesTable.setFont(new Font("Arial", Font.PLAIN, 14));
        
        // Définir les largeurs des colonnes
        articlesTable.getColumnModel().getColumn(0).setPreferredWidth(50);  // ID
        articlesTable.getColumnModel().getColumn(1).setPreferredWidth(180); // Nom
        articlesTable.getColumnModel().getColumn(2).setPreferredWidth(120); // Marque
        articlesTable.getColumnModel().getColumn(3).setPreferredWidth(100); // Prix Unitaire
        articlesTable.getColumnModel().getColumn(4).setPreferredWidth(100); // Prix Vrac
        articlesTable.getColumnModel().getColumn(5).setPreferredWidth(80);  // Qté Vrac
        articlesTable.getColumnModel().getColumn(6).setPreferredWidth(80);  // Stock
        articlesTable.getColumnModel().getColumn(7).setPreferredWidth(100); // Promotion
        
        // Ajouter un écouteur de sélection pour activer/désactiver les boutons
        articlesTable.getSelectionModel().addListSelectionListener(e -> {
            boolean rowSelected = articlesTable.getSelectedRow() != -1;
            modifierButton.setEnabled(rowSelected);
            supprimerButton.setEnabled(rowSelected);
            gererPromoButton.setEnabled(rowSelected);
        });
        
        // Ajouter la table dans un JScrollPane
        JScrollPane scrollPane = new JScrollPane(articlesTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }

    /**
     * Afficher la liste des articles
     * @param articles Liste des articles à afficher
     */
    public void afficherArticles(List<Article> articles) {
        // Vider la table
        tableModel.setRowCount(0);
        
        // Ajouter chaque article à la table
        for (Article article : articles) {
            String promotionInfo = "Aucune";
            if (article.getPromotion() != null) {
                promotionInfo = "-" + article.getPromotion().getPourcentage() + "%";
            }
            
            Object[] row = {
                article.getIdArticle(),
                article.getNom(),
                article.getMarque(),
                article.getPrixUnitaire(),
                article.getPrixVrac(),
                article.getQuantiteVrac() > 0 ? article.getQuantiteVrac() : "Pas de quantité vrac",
                article.getStock(),
                promotionInfo
            };
            tableModel.addRow(row);
        }
    }
    
    /**
     * Récupérer l'article sélectionné
     * @return L'ID de l'article sélectionné ou -1 si aucun article n'est sélectionné
     */
    public int getSelectedArticleId() {
        int selectedRow = articlesTable.getSelectedRow();
        if (selectedRow != -1) {
            return (int) tableModel.getValueAt(selectedRow, 0);
        }
        return -1;
    }
    
    /**
     * Définir l'écouteur pour le bouton Ajouter
     * @param listener L'écouteur à définir
     */
    public void setAjouterListener(ActionListener listener) {
        ajouterButton.addActionListener(listener);
    }
    
    /**
     * Définir l'écouteur pour le bouton Modifier
     * @param listener L'écouteur à définir
     */
    public void setModifierListener(ActionListener listener) {
        modifierButton.addActionListener(listener);
    }
    
    /**
     * Définir l'écouteur pour le bouton Supprimer
     * @param listener L'écouteur à définir
     */
    public void setSupprimerListener(ActionListener listener) {
        supprimerButton.addActionListener(listener);
    }
    
    /**
     * Définir l'écouteur pour le bouton Rechercher
     * @param listener L'écouteur à définir
     */
    public void setRechercheListener(ActionListener listener) {
        rechercheButton.addActionListener(listener);
    }
    
    /**
     * Récupérer le texte de recherche
     * @return Le texte de recherche
     */
    public String getRechercheText() {
        return rechercheField.getText();
    }
    
    /**
     * Définir l'administrateur connecté dans la barre de navigation
     * @param adminEmail Administrateur connecté
     */
    public void setAdminName(String adminEmail) {
        // Cette méthode n'est plus nécessaire car la barre de navigation a été supprimée
        // mais on la conserve pour la compatibilité avec le code existant
    }
    
    /**
     * Définir l'écouteur pour le bouton Articles dans la barre de navigation
     * @param listener L'écouteur à définir
     */
    public void setArticlesNavButtonListener(ActionListener listener) {
        articlesNavButton.addActionListener(listener);
    }
    
    /**
     * Définir l'écouteur pour le bouton Utilisateurs dans la barre de navigation
     * @param listener L'écouteur à définir
     */
    public void setUsersNavButtonListener(ActionListener listener) {
        usersNavButton.addActionListener(listener);
    }
    
    /**
     * Définir l'écouteur pour le bouton Déconnexion dans la barre de navigation
     * @param listener L'écouteur à définir
     */
    public void setNavDeconnexionListener(ActionListener listener) {
        if (deconnexionButton == null) {
            deconnexionButton = new JButton("Déconnexion");
            deconnexionButton.setBackground(new Color(220, 53, 69)); // Rouge
            deconnexionButton.setForeground(Color.WHITE);
            deconnexionButton.setFont(new Font("Arial", Font.BOLD, 14));
            deconnexionButton.setFocusPainted(false);
        }
        
        // Supprimer les écouteurs existants pour éviter les doublons
        for (ActionListener al : deconnexionButton.getActionListeners()) {
            deconnexionButton.removeActionListener(al);
        }
        
        // Ajouter le nouvel écouteur
        deconnexionButton.addActionListener(listener);
    }
    
    /**
     * Afficher le panneau de gestion des articles
     */
    public void showArticlesPanel() {
        cardLayout.show(contentPanel, "articles");
        articlesNavButton.setBackground(new Color(0, 123, 255)); // Bleu pour le bouton actif
        usersNavButton.setBackground(new Color(73, 80, 87)); // Gris pour le bouton inactif
    }
    
    /**
     * Afficher le panneau de gestion des utilisateurs
     */
    public void showUsersPanel() {
        cardLayout.show(contentPanel, "users");
        usersNavButton.setBackground(new Color(0, 123, 255)); // Bleu pour le bouton actif
        articlesNavButton.setBackground(new Color(73, 80, 87)); // Gris pour le bouton inactif
    }
    
    /**
     * Récupérer la vue de gestion des utilisateurs
     * @return La vue de gestion des utilisateurs
     */
    public UserManagementView getUserManagementView() {
        return userManagementView;
    }
    
    /**
     * Récupérer le bouton Articles pour l'ajouter à la barre de navigation principale
     * @return Le bouton Articles
     */
    public JButton getArticlesNavButton() {
        return articlesNavButton;
    }
    
    /**
     * Récupérer le bouton Utilisateurs pour l'ajouter à la barre de navigation principale
     * @return Le bouton Utilisateurs
     */
    public JButton getUsersNavButton() {
        return usersNavButton;
    }
    
    /**
     * Récupérer le bouton Déconnexion pour l'ajouter à la barre de navigation principale
     * @return Le bouton Déconnexion
     */
    public JButton getDeconnexionButton() {
        return deconnexionButton;
    }
    
    /**
     * Définir l'écouteur pour le bouton Gérer Promotion
     * @param listener L'écouteur à définir
     */
    public void setGererPromoListener(ActionListener listener) {
        gererPromoButton.addActionListener(listener);
    }
    
    /**
     * Afficher la boîte de dialogue pour gérer une promotion
     * @param article L'article dont on veut gérer la promotion
     * @param existingPromotion La promotion existante ou null s'il n'y en a pas
     * @return La nouvelle promotion ou null si annulé
     */
    public Promotion showPromotionDialog(Article article, Promotion existingPromotion) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Gérer Promotion", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(400, 250);
        dialog.setLocationRelativeTo(this);
        
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Informations sur l'article
        JLabel articleInfoLabel = new JLabel("Article: " + article.getNom() + " (" + article.getMarque() + ")");
        articleInfoLabel.setFont(new Font("Arial", Font.BOLD, 16));
        articleInfoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(articleInfoLabel);
        
        JLabel prixLabel = new JLabel("Prix: " + String.format("%.2f €", article.getPrixUnitaire()));
        prixLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        prixLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(prixLabel);
        
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Panneau pour la saisie du pourcentage
        JPanel pourcentagePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pourcentagePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel pourcentageLabel = new JLabel("Pourcentage de réduction:");
        pourcentageLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        pourcentagePanel.add(pourcentageLabel);
        
        SpinnerModel spinnerModel = new SpinnerNumberModel(
            existingPromotion != null ? existingPromotion.getPourcentage() : 10, // valeur initiale
            1,      // minimum
            99,     // maximum
            1       // pas
        );
        JSpinner pourcentageSpinner = new JSpinner(spinnerModel);
        pourcentageSpinner.setPreferredSize(new Dimension(80, 25));
        pourcentagePanel.add(pourcentageSpinner);
        
        JLabel percentLabel = new JLabel("%");
        percentLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        pourcentagePanel.add(percentLabel);
        
        contentPanel.add(pourcentagePanel);
        
        // Afficher le prix après promotion
        JPanel prixPromoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        prixPromoPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel prixPromoLabel = new JLabel("Prix après promotion: ");
        prixPromoLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        prixPromoPanel.add(prixPromoLabel);
        
        JLabel prixPromoValeur = new JLabel();
        prixPromoValeur.setFont(new Font("Arial", Font.BOLD, 14));
        prixPromoValeur.setForeground(new Color(0, 150, 0));
        prixPromoPanel.add(prixPromoValeur);
        
        // Mettre à jour le prix après promotion lorsque le pourcentage change
        pourcentageSpinner.addChangeListener(e -> {
            int pourcentage = (Integer) pourcentageSpinner.getValue();
            double prixPromo = article.getPrixUnitaire() * (100 - pourcentage) / 100.0;
            prixPromoValeur.setText(String.format("%.2f €", prixPromo));
        });
        
        // Initialiser le prix après promotion
        int pourcentageInitial = (Integer) pourcentageSpinner.getValue();
        double prixPromoInitial = article.getPrixUnitaire() * (100 - pourcentageInitial) / 100.0;
        prixPromoValeur.setText(String.format("%.2f €", prixPromoInitial));
        
        contentPanel.add(prixPromoPanel);
        
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Panneau pour les boutons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JButton cancelButton = new JButton("Annuler");
        cancelButton.addActionListener(e -> dialog.dispose());
        
        JButton saveButton = new JButton("Enregistrer");
        saveButton.setBackground(new Color(40, 167, 69));
        saveButton.setForeground(Color.WHITE);
        
        JButton deleteButton = null;
        if (existingPromotion != null) {
            deleteButton = new JButton("Supprimer Promotion");
            deleteButton.setBackground(new Color(220, 53, 69));
            deleteButton.setForeground(Color.WHITE);
            buttonPanel.add(deleteButton);
        }
        
        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);
        
        // Variable pour stocker le résultat
        final Promotion[] result = {null};
        
        // Action du bouton Enregistrer
        saveButton.addActionListener(e -> {
            int pourcentage = (Integer) pourcentageSpinner.getValue();
            if (existingPromotion != null) {
                existingPromotion.setPourcentage(pourcentage);
                result[0] = existingPromotion;
            } else {
                result[0] = new Promotion(article.getIdArticle(), pourcentage);
            }
            dialog.dispose();
        });
        
        // Action du bouton Supprimer si existant
        if (deleteButton != null) {
            deleteButton.addActionListener(e -> {
                int confirm = JOptionPane.showConfirmDialog(
                    dialog,
                    "Êtes-vous sûr de vouloir supprimer cette promotion?",
                    "Confirmation de suppression",
                    JOptionPane.YES_NO_OPTION
                );
                if (confirm == JOptionPane.YES_OPTION) {
                    result[0] = new Promotion(article.getIdArticle(), 0);
                    result[0].setIdPromotion(existingPromotion.getIdPromotion());
                    result[0].setPourcentage(-1); // Valeur spéciale pour indiquer une suppression
                    dialog.dispose();
                }
            });
        }
        
        dialog.add(contentPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.setVisible(true);
        
        return result[0];
    }
}
