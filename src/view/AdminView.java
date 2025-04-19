package view;

import model.Article;

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
        String[] colonnes = {"ID", "Nom", "Marque", "Prix Unitaire", "Prix Vrac", "Qté Vrac", "Stock"};
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
        articlesTable.getColumnModel().getColumn(1).setPreferredWidth(200); // Nom
        articlesTable.getColumnModel().getColumn(2).setPreferredWidth(150); // Marque
        articlesTable.getColumnModel().getColumn(3).setPreferredWidth(100); // Prix Unitaire
        articlesTable.getColumnModel().getColumn(4).setPreferredWidth(100); // Prix Vrac
        articlesTable.getColumnModel().getColumn(5).setPreferredWidth(80);  // Qté Vrac
        articlesTable.getColumnModel().getColumn(6).setPreferredWidth(80);  // Stock
        
        // Ajouter un écouteur de sélection pour activer/désactiver les boutons
        articlesTable.getSelectionModel().addListSelectionListener(e -> {
            boolean rowSelected = articlesTable.getSelectedRow() != -1;
            modifierButton.setEnabled(rowSelected);
            supprimerButton.setEnabled(rowSelected);
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
            Object[] row = {
                article.getIdArticle(),
                article.getNom(),
                article.getMarque(),
                article.getPrixUnitaire() / 100.0, // Conversion en euros
                article.getPrixVrac() / 100.0,     // Conversion en euros
                article.getQuantiteVrac(),
                article.getStock()
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
}
