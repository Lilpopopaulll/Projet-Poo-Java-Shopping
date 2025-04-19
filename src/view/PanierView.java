package view;

import model.Commande;
import model.LigneCommande;
import controller.ArticleClickListener;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.Locale;

public class PanierView extends JPanel {
    private JTable panierTable;
    private DefaultTableModel tableModel;
    private JLabel totalLabel;
    private JButton validerButton;
    private ActionListener suppressionListener;
    private ArticleClickListener retourListener;

    public PanierView() {
        // Configuration du panneau
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        
        // Création du modèle de table
        String[] colonnes = {"Article", "Prix unitaire", "Quantité", "Sous-total", "Actions"};
        tableModel = new DefaultTableModel(colonnes, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4; // Seule la colonne "Actions" est éditable
            }
        };
        
        // Création de la table
        panierTable = new JTable(tableModel);
        panierTable.setRowHeight(40);
        panierTable.getColumnModel().getColumn(0).setPreferredWidth(300); // Article
        panierTable.getColumnModel().getColumn(1).setPreferredWidth(100); // Prix unitaire
        panierTable.getColumnModel().getColumn(2).setPreferredWidth(80);  // Quantité
        panierTable.getColumnModel().getColumn(3).setPreferredWidth(100); // Sous-total
        panierTable.getColumnModel().getColumn(4).setPreferredWidth(100); // Actions
    }
    
    // Méthode pour afficher le contenu du panier
    public void afficherPanier(Commande panier) {
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
            if (retourListener != null) {
                retourListener.onArticleClick(null); // signal de retour
            }
        });

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        topPanel.add(backButton, BorderLayout.WEST);
        topPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        // Titre avec style amélioré
        JLabel title = new JLabel("Votre Panier", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 32));
        title.setForeground(Color.decode("#212529"));
        title.setBorder(BorderFactory.createEmptyBorder(10, 0, 30, 0));
        
        // Vider la table
        tableModel.setRowCount(0);
        
        // Formatter pour les prix
        NumberFormat formatPrix = NumberFormat.getCurrencyInstance(Locale.FRANCE);
        
        // Ajouter chaque ligne de commande à la table
        for (LigneCommande ligne : panier.getLignesCommande()) {
            String nomArticle = ligne.getArticle() != null ? ligne.getArticle().getNom() : "Article inconnu";
            String prixUnitaire = formatPrix.format(ligne.getPrixApplique() / 100.0); // Conversion en euros
            int quantite = ligne.getQuantite();
            String sousTotal = formatPrix.format(ligne.getSousTotal() / 100.0); // Conversion en euros
            
            // Créer un bouton de suppression
            JButton supprimerButton = new JButton("Supprimer");
            supprimerButton.setActionCommand(String.valueOf(ligne.getIdArticle()));
            if (suppressionListener != null) {
                supprimerButton.addActionListener(suppressionListener);
            }
            
            // Ajouter la ligne à la table
            tableModel.addRow(new Object[]{nomArticle, prixUnitaire, quantite, sousTotal, supprimerButton});
        }
        
        // Configurer le rendu des boutons dans la table
        panierTable.getColumn("Actions").setCellRenderer(new ButtonRenderer());
        panierTable.getColumn("Actions").setCellEditor(new ButtonEditor(new JCheckBox()));
        
        // Ajout de la table dans un JScrollPane
        JScrollPane scrollPane = new JScrollPane(panierTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        
        // Panneau pour le total et le bouton de validation
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        // Panneau pour le total
        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        totalPanel.setBackground(Color.WHITE);
        
        totalLabel = new JLabel("Total : " + formatPrix.format(panier.getTotal() / 100.0));
        totalLabel.setFont(new Font("Arial", Font.BOLD, 18));
        totalPanel.add(totalLabel);
        
        // Panneau pour le bouton de validation
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        validerButton = new JButton("Valider la commande");
        validerButton.setFont(new Font("Arial", Font.BOLD, 16));
        validerButton.setBackground(Color.decode("#28a745"));
        validerButton.setForeground(Color.WHITE);
        validerButton.setFocusPainted(false);
        validerButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.decode("#28a745")),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        validerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Ajouter un ActionCommand pour identifier le bouton
        validerButton.setActionCommand("valider_commande");
        
        buttonPanel.add(validerButton);
        
        bottomPanel.add(totalPanel, BorderLayout.NORTH);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Ajouter les composants au panel principal
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.add(topPanel, BorderLayout.NORTH);
        headerPanel.add(title, BorderLayout.CENTER);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        contentPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        // Ajouter le panel principal avec scrolling
        JScrollPane mainScrollPane = new JScrollPane(mainPanel);
        mainScrollPane.setBorder(null);
        mainScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        mainScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        add(mainScrollPane, BorderLayout.CENTER);
        revalidate();
        repaint();
    }
    
    // Définir l'écouteur pour les boutons de suppression
    public void setSuppressionListener(ActionListener listener) {
        this.suppressionListener = listener;
    }
    
    // Définir l'écouteur pour le bouton de validation
    public void setValidationListener(ActionListener listener) {
        if (validerButton != null) {
            // Supprimer les écouteurs existants pour éviter les doublons
            for (ActionListener al : validerButton.getActionListeners()) {
                validerButton.removeActionListener(al);
            }
            // Ajouter le nouvel écouteur
            validerButton.addActionListener(listener);
            System.out.println("Écouteur ajouté au bouton de validation");
        } else {
            System.out.println("Erreur: validerButton est null dans setValidationListener");
        }
    }
    
    // Définir l'écouteur pour le bouton de retour
    public void setRetourListener(ArticleClickListener listener) {
        this.retourListener = listener;
    }
    
    // Classe pour rendre les boutons dans la table
    class ButtonRenderer extends JButton implements javax.swing.table.TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (isSelected) {
                setForeground(table.getSelectionForeground());
                setBackground(table.getSelectionBackground());
            } else {
                setForeground(table.getForeground());
                setBackground(UIManager.getColor("Button.background"));
            }
            setText((value == null) ? "" : "Supprimer");
            return this;
        }
    }
    
    // Classe pour éditer les boutons dans la table
    class ButtonEditor extends DefaultCellEditor {
        protected JButton button;
        private boolean isPushed;
        
        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(e -> fireEditingStopped());
        }
        
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            if (isSelected) {
                button.setForeground(table.getSelectionForeground());
                button.setBackground(table.getSelectionBackground());
            } else {
                button.setForeground(table.getForeground());
                button.setBackground(table.getBackground());
            }
            
            button.setText("Supprimer");
            isPushed = true;
            return button;
        }
        
        @Override
        public Object getCellEditorValue() {
            if (isPushed) {
                // Simuler un clic sur le bouton avec l'actionCommand approprié
                if (suppressionListener != null) {
                    int row = panierTable.getSelectedRow();
                    if (row >= 0 && row < tableModel.getRowCount()) {
                        // Récupérer l'ID de l'article à partir de la table
                        JButton btn = (JButton) tableModel.getValueAt(row, 4);
                        suppressionListener.actionPerformed(new java.awt.event.ActionEvent(
                            this, java.awt.event.ActionEvent.ACTION_PERFORMED, btn.getActionCommand()));
                    }
                }
            }
            isPushed = false;
            return "Supprimer";
        }
        
        @Override
        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }
    }
}
