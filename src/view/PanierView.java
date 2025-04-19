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
        double totalEconomies = 0.0;
        for (LigneCommande ligne : panier.getLignesCommande()) {
            String nomArticle = ligne.getArticle() != null ? ligne.getArticle().getNom() : "Article inconnu";
            
            // Informations sur les prix
            double prixUnitaireStandard = ligne.getArticle().getPrixUnitaire() / 100.0;
            double prixUnitaireVrac = ligne.getArticle().getPrixVrac() / 100.0;
            double prixUnitaireApplique = ligne.getPrixApplique() / 100.0;
            int quantite = ligne.getQuantite();
            double sousTotal = ligne.getSousTotal() / 100.0;
            int quantiteVrac = ligne.getArticle().getQuantiteVrac();
            
            // Calcul des économies et détermination si le prix mixte est appliqué
            boolean prixMixteApplique = quantite >= quantiteVrac;
            double economie = 0.0;
            
            if (prixMixteApplique) {
                // Calcul des économies réalisées avec le prix mixte
                int nombreLotsVrac = quantite / quantiteVrac;
                int uniteRestantes = quantite % quantiteVrac;
                
                // Prix total si tout était au prix unitaire standard
                double prixTotalStandard = quantite * prixUnitaireStandard;
                
                // Prix total avec la répartition vrac/standard
                double prixTotalMixte = (nombreLotsVrac * quantiteVrac * prixUnitaireVrac) + 
                                        (uniteRestantes * prixUnitaireStandard);
                
                economie = prixTotalStandard - prixTotalMixte;
                totalEconomies += economie;
            }
            
            // Création du composant pour afficher le prix
            JPanel prixPanel = new JPanel();
            prixPanel.setLayout(new BoxLayout(prixPanel, BoxLayout.Y_AXIS));
            prixPanel.setBackground(Color.WHITE);
            
            JLabel prixLabel = new JLabel(formatPrix.format(prixUnitaireApplique));
            prixLabel.setFont(new Font("Arial", Font.BOLD, 14));
            prixLabel.setForeground(Color.decode("#212529"));
            prixPanel.add(prixLabel);
            
            if (prixMixteApplique) {
                // Afficher le détail du calcul du prix mixte
                int nombreLotsVrac = quantite / quantiteVrac;
                int uniteRestantes = quantite % quantiteVrac;
                
                JLabel detailPrixLabel = new JLabel("<html><small>" + 
                    nombreLotsVrac + " lot" + (nombreLotsVrac > 1 ? "s" : "") + " de " + quantiteVrac + 
                    " à " + formatPrix.format(prixUnitaireVrac) + "/unité" +
                    (uniteRestantes > 0 ? " + " + uniteRestantes + " à " + formatPrix.format(prixUnitaireStandard) : "") +
                    "</small></html>");
                detailPrixLabel.setFont(new Font("Arial", Font.PLAIN, 11));
                detailPrixLabel.setForeground(Color.decode("#6c757d"));
                prixPanel.add(detailPrixLabel);
                
                JLabel economieLabel = new JLabel("<html><font color='#28a745'>Prix en vrac appliqué</font></html>");
                economieLabel.setFont(new Font("Arial", Font.ITALIC, 11));
                prixPanel.add(economieLabel);
            }
            
            // Création du composant pour afficher le sous-total
            JPanel sousTotalPanel = new JPanel();
            sousTotalPanel.setLayout(new BoxLayout(sousTotalPanel, BoxLayout.Y_AXIS));
            sousTotalPanel.setBackground(Color.WHITE);
            
            JLabel sousTotalLabel = new JLabel(formatPrix.format(sousTotal));
            sousTotalLabel.setFont(new Font("Arial", Font.BOLD, 14));
            sousTotalLabel.setForeground(Color.decode("#212529"));
            sousTotalPanel.add(sousTotalLabel);
            
            if (prixMixteApplique) {
                JLabel economieDetailLabel = new JLabel("<html><font color='#28a745'>Économie: " + formatPrix.format(economie) + "</font></html>");
                economieDetailLabel.setFont(new Font("Arial", Font.ITALIC, 11));
                sousTotalPanel.add(economieDetailLabel);
            }
            
            // Bouton de suppression
            JButton supprimerButton = new JButton("Supprimer");
            supprimerButton.setActionCommand(String.valueOf(ligne.getIdArticle()));
            if (suppressionListener != null) {
                supprimerButton.addActionListener(suppressionListener);
            }
            
            // Ajouter la ligne à la table
            tableModel.addRow(new Object[]{
                nomArticle,
                prixPanel,
                quantite,
                sousTotalPanel,
                supprimerButton
            });
        }
        
        // Configurer les renderers et editors pour les colonnes
        panierTable.getColumnModel().getColumn(1).setCellRenderer(new PanelRenderer());
        panierTable.getColumnModel().getColumn(3).setCellRenderer(new PanelRenderer());
        panierTable.getColumnModel().getColumn(4).setCellRenderer(new ButtonRenderer());
        panierTable.getColumnModel().getColumn(4).setCellEditor(new ButtonEditor(new JCheckBox()));
        
        // Panel pour le total et le bouton de validation
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        // Panel pour le total avec style amélioré
        JPanel totalPanel = new JPanel();
        totalPanel.setLayout(new BoxLayout(totalPanel, BoxLayout.Y_AXIS));
        totalPanel.setBackground(Color.decode("#f8f9fa"));
        totalPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.decode("#dee2e6")),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        
        // Affichage du total
        totalLabel = new JLabel("Total: " + formatPrix.format(panier.getTotal() / 100.0));
        totalLabel.setFont(new Font("Arial", Font.BOLD, 18));
        totalLabel.setForeground(Color.decode("#212529"));
        totalLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        totalPanel.add(totalLabel);
        
        // Affichage des économies totales si applicable
        if (totalEconomies > 0) {
            JLabel economiesLabel = new JLabel("Économies réalisées: " + formatPrix.format(totalEconomies));
            economiesLabel.setFont(new Font("Arial", Font.BOLD, 14));
            economiesLabel.setForeground(Color.decode("#28a745"));
            economiesLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            economiesLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
            totalPanel.add(economiesLabel);
        }
        
        bottomPanel.add(totalPanel, BorderLayout.WEST);
        
        // Bouton de validation avec style amélioré
        validerButton = new JButton("Valider ma commande");
        validerButton.setFont(new Font("Arial", Font.BOLD, 16));
        validerButton.setForeground(Color.WHITE);
        validerButton.setBackground(Color.decode("#28a745"));
        validerButton.setFocusPainted(false);
        validerButton.setBorder(BorderFactory.createEmptyBorder(12, 25, 12, 25));
        validerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Désactiver le bouton si le panier est vide
        validerButton.setEnabled(!panier.getLignesCommande().isEmpty());
        
        bottomPanel.add(validerButton, BorderLayout.EAST);
        
        // Assembler les composants
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(title, BorderLayout.NORTH);
        
        // Créer le panel de la table avec défilement
        JScrollPane tableScrollPane = new JScrollPane(panierTable);
        tableScrollPane.setBorder(BorderFactory.createEmptyBorder());
        tableScrollPane.getViewport().setBackground(Color.WHITE);
        
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        // Créer un scroll pane pour le panel principal
        JScrollPane mainScrollPane = new JScrollPane(mainPanel);
        mainScrollPane.setBorder(BorderFactory.createEmptyBorder());
        mainScrollPane.getViewport().setBackground(Color.WHITE);
        mainScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
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
    
    // Classe pour rendre les panels dans la table
    class PanelRenderer implements javax.swing.table.TableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (value instanceof JPanel) {
                JPanel panel = (JPanel) value;
                if (isSelected) {
                    panel.setBackground(table.getSelectionBackground());
                } else {
                    panel.setBackground(table.getBackground());
                }
                return panel;
            }
            return new JLabel(value == null ? "" : value.toString());
        }
    }
}
