package view;

import model.Client;
import model.Commande;
import model.LigneCommande;
import controller.ArticleClickListener;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Vue pour afficher l'historique des commandes d'un client
 */
public class HistoriqueView extends JPanel {
    private JTable commandesTable;
    private DefaultTableModel tableModel;
    private ArticleClickListener retourListener;
    private JPanel detailPanel;
    private CardLayout cardLayout;
    private JPanel mainCardPanel;
    private ActionListener detailListener;

    public HistoriqueView() {
        // Configuration du panneau
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        
        // Création du modèle de table
        String[] colonnes = {"ID", "Date", "Statut", "Montant total", "Actions"};
        tableModel = new DefaultTableModel(colonnes, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4; // Seule la colonne "Actions" est éditable
            }
        };
        
        // Création de la table
        commandesTable = new JTable(tableModel);
        commandesTable.setRowHeight(40);
        commandesTable.getColumnModel().getColumn(0).setPreferredWidth(50);  // ID
        commandesTable.getColumnModel().getColumn(1).setPreferredWidth(150); // Date
        commandesTable.getColumnModel().getColumn(2).setPreferredWidth(100); // Statut
        commandesTable.getColumnModel().getColumn(3).setPreferredWidth(150); // Montant total
        commandesTable.getColumnModel().getColumn(4).setPreferredWidth(100); // Actions
        
        // Création du card layout pour alterner entre la liste des commandes et le détail d'une commande
        cardLayout = new CardLayout();
        mainCardPanel = new JPanel(cardLayout);
        mainCardPanel.setBackground(Color.WHITE);
        
        // Panneau pour la liste des commandes
        JPanel listePanel = new JPanel(new BorderLayout());
        listePanel.setBackground(Color.WHITE);
        
        // Panneau pour le détail d'une commande
        detailPanel = new JPanel(new BorderLayout());
        detailPanel.setBackground(Color.WHITE);
        
        // Ajouter les panneaux au card layout
        mainCardPanel.add(listePanel, "liste");
        mainCardPanel.add(detailPanel, "detail");
        
        // Afficher la liste par défaut
        cardLayout.show(mainCardPanel, "liste");
        
        // Ajouter le card layout au panneau principal
        add(mainCardPanel, BorderLayout.CENTER);
    }
    
    /**
     * Affiche l'historique des commandes d'un client
     * @param commandes La liste des commandes du client
     * @param client Le client connecté
     */
    public void afficherHistorique(List<Commande> commandes, Client client) {
        // Vider tout
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
        JLabel title = new JLabel("Historique de vos commandes", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 32));
        title.setForeground(Color.decode("#212529"));
        title.setBorder(BorderFactory.createEmptyBorder(10, 0, 30, 0));
        
        // Vider la table
        tableModel.setRowCount(0);
        
        // Formatter pour les prix et dates
        NumberFormat formatPrix = NumberFormat.getCurrencyInstance(Locale.FRANCE);
        SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        
        // Ajouter chaque commande à la table
        for (Commande commande : commandes) {
            String id = String.valueOf(commande.getIdCommande());
            String date = commande.getDateCommande() != null ? formatDate.format(commande.getDateCommande()) : "N/A";
            String statut = commande.getStatut();
            String montantTotal = formatPrix.format(commande.getTotal() / 100.0); // Conversion en euros
            
            // Créer un bouton pour voir le détail
            JButton detailButton = new JButton("Détail");
            detailButton.setActionCommand(String.valueOf(commande.getIdCommande()));
            
            // Ajouter la ligne à la table
            tableModel.addRow(new Object[]{id, date, statut, montantTotal, detailButton});
        }
        
        // Configurer le rendu des boutons dans la table
        commandesTable.getColumn("Actions").setCellRenderer(new ButtonRenderer());
        commandesTable.getColumn("Actions").setCellEditor(new ButtonEditor(new JCheckBox()));
        
        // Ajout de la table dans un JScrollPane
        JScrollPane scrollPane = new JScrollPane(commandesTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        
        // Ajouter les composants au panel principal
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.add(topPanel, BorderLayout.NORTH);
        headerPanel.add(title, BorderLayout.CENTER);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Ajouter le panel principal avec scrolling
        JScrollPane mainScrollPane = new JScrollPane(mainPanel);
        mainScrollPane.setBorder(null);
        mainScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        mainScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        add(mainScrollPane, BorderLayout.CENTER);
        revalidate();
        repaint();
    }
    
    /**
     * Affiche le détail d'une commande
     * @param commande La commande à afficher
     */
    public void afficherDetailCommande(Commande commande) {
        // Vider le panneau de détail
        detailPanel.removeAll();
        
        // Panel principal avec une marge
        JPanel detailContentPanel = new JPanel(new BorderLayout());
        detailContentPanel.setBackground(Color.WHITE);
        detailContentPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 40, 40));
        
        // Bouton retour avec style amélioré
        JButton backButton = new JButton("← Retour à la liste");
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
            cardLayout.show(mainCardPanel, "liste");
        });

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        topPanel.add(backButton, BorderLayout.WEST);
        topPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        // Titre avec style amélioré
        SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String dateStr = commande.getDateCommande() != null ? formatDate.format(commande.getDateCommande()) : "N/A";
        
        JLabel title = new JLabel("Détail de la commande #" + commande.getIdCommande() + " du " + dateStr, SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setForeground(Color.decode("#212529"));
        title.setBorder(BorderFactory.createEmptyBorder(10, 0, 30, 0));
        
        // Créer un modèle de table pour les lignes de commande
        String[] colonnes = {"Article", "Prix unitaire", "Quantité", "Sous-total"};
        DefaultTableModel detailTableModel = new DefaultTableModel(colonnes, 0);
        
        // Création de la table
        JTable detailTable = new JTable(detailTableModel);
        detailTable.setRowHeight(40);
        detailTable.getColumnModel().getColumn(0).setPreferredWidth(300); // Article
        detailTable.getColumnModel().getColumn(1).setPreferredWidth(100); // Prix unitaire
        detailTable.getColumnModel().getColumn(2).setPreferredWidth(80);  // Quantité
        detailTable.getColumnModel().getColumn(3).setPreferredWidth(100); // Sous-total
        
        // Formatter pour les prix
        NumberFormat formatPrix = NumberFormat.getCurrencyInstance(Locale.FRANCE);
        
        // Ajouter chaque ligne de commande à la table
        for (LigneCommande ligne : commande.getLignesCommande()) {
            String nomArticle = ligne.getArticle() != null ? ligne.getArticle().getNom() : "Article inconnu";
            String prixUnitaire = formatPrix.format(ligne.getPrixApplique() / 100.0); // Conversion en euros
            int quantite = ligne.getQuantite();
            String sousTotal = formatPrix.format(ligne.getSousTotal() / 100.0); // Conversion en euros
            
            // Ajouter la ligne à la table
            detailTableModel.addRow(new Object[]{nomArticle, prixUnitaire, quantite, sousTotal});
        }
        
        // Ajout de la table dans un JScrollPane
        JScrollPane scrollPane = new JScrollPane(detailTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        
        // Panneau pour le total
        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        totalPanel.setBackground(Color.WHITE);
        
        JLabel totalLabel = new JLabel("Total : " + formatPrix.format(commande.getTotal() / 100.0));
        totalLabel.setFont(new Font("Arial", Font.BOLD, 18));
        totalPanel.add(totalLabel);
        
        // Ajouter les composants au panel principal
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.add(topPanel, BorderLayout.NORTH);
        headerPanel.add(title, BorderLayout.CENTER);
        
        detailContentPanel.add(headerPanel, BorderLayout.NORTH);
        detailContentPanel.add(scrollPane, BorderLayout.CENTER);
        detailContentPanel.add(totalPanel, BorderLayout.SOUTH);
        
        // Ajouter le panel principal avec scrolling
        JScrollPane detailScrollPane = new JScrollPane(detailContentPanel);
        detailScrollPane.setBorder(null);
        detailScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        detailScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        detailPanel.add(detailScrollPane, BorderLayout.CENTER);
        detailPanel.revalidate();
        detailPanel.repaint();
        
        // Afficher le panneau de détail
        cardLayout.show(mainCardPanel, "detail");
    }
    
    /**
     * Définir l'écouteur pour le bouton de retour
     * @param listener L'écouteur à ajouter
     */
    public void setRetourListener(ArticleClickListener listener) {
        this.retourListener = listener;
    }
    
    /**
     * Définir l'écouteur pour les boutons de détail
     * @param listener L'écouteur à ajouter
     */
    public void setDetailListener(ActionListener listener) {
        this.detailListener = listener;
    }
    
    /**
     * Classe pour rendre les boutons dans la table
     */
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
            setText((value == null) ? "" : "Détail");
            return this;
        }
    }
    
    /**
     * Classe pour éditer les boutons dans la table
     */
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
            
            button.setText("Détail");
            isPushed = true;
            return button;
        }
        
        @Override
        public Object getCellEditorValue() {
            if (isPushed) {
                int row = commandesTable.getSelectedRow();
                if (row >= 0 && row < tableModel.getRowCount()) {
                    // Récupérer l'ID de la commande et l'utiliser pour afficher le détail
                    int idCommande = Integer.parseInt((String) tableModel.getValueAt(row, 0));
                    // Créer un événement avec l'ID de la commande comme action command
                    ActionEvent event = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, String.valueOf(idCommande));
                    // Appeler l'écouteur d'événements avec l'ID de la commande
                    // Cela sera traité par le contrôleur pour afficher les détails
                    if (detailListener != null) {
                        detailListener.actionPerformed(event);
                    }
                }
            }
            isPushed = false;
            return "Détail";
        }
        
        @Override
        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }
    }
}
