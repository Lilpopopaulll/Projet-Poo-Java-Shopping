package view;

import model.Admin;
import model.Client;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Vue pour la gestion des comptes utilisateurs (clients et administrateurs)
 */
public class UserManagementView extends JPanel {
    private JTable clientsTable;
    private DefaultTableModel clientsTableModel;
    private JTable adminsTable;
    private DefaultTableModel adminsTableModel;
    private JButton addClientButton;
    private JButton addAdminButton;
    private JButton deleteClientButton;
    private JButton deleteAdminButton;
    private JTabbedPane tabbedPane;

    /**
     * Constructeur
     */
    public UserManagementView() {
        // Configuration du panneau
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        
        // Titre
        JLabel titleLabel = new JLabel("Gestion des Comptes Utilisateurs");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        
        // Créer un panneau à onglets
        tabbedPane = new JTabbedPane();
        
        // Onglet Clients
        JPanel clientsPanel = createClientsPanel();
        tabbedPane.addTab("Clients", clientsPanel);
        
        // Onglet Administrateurs
        JPanel adminsPanel = createAdminsPanel();
        tabbedPane.addTab("Administrateurs", adminsPanel);
        
        // Ajouter les composants au panneau principal
        add(titleLabel, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);
    }
    
    /**
     * Créer le panneau pour la gestion des clients
     * @return Le panneau créé
     */
    private JPanel createClientsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        // Boutons d'action
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        actionPanel.setBackground(Color.WHITE);
        actionPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        addClientButton = new JButton("Ajouter Client");
        addClientButton.setBackground(new Color(40, 167, 69)); // Vert
        addClientButton.setForeground(Color.WHITE);
        addClientButton.setFont(new Font("Arial", Font.BOLD, 14));
        addClientButton.setFocusPainted(false);
        addClientButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        actionPanel.add(addClientButton);
        
        deleteClientButton = new JButton("Supprimer Client");
        deleteClientButton.setBackground(new Color(220, 53, 69)); // Rouge
        deleteClientButton.setForeground(Color.WHITE);
        deleteClientButton.setFont(new Font("Arial", Font.BOLD, 14));
        deleteClientButton.setFocusPainted(false);
        deleteClientButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        deleteClientButton.setEnabled(false); // Désactivé par défaut
        actionPanel.add(deleteClientButton);
        
        // Tableau des clients
        String[] clientColumns = {"ID", "Nom", "Prénom", "Email", "Type Client"};
        clientsTableModel = new DefaultTableModel(clientColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Tableau non éditable
            }
        };
        
        clientsTable = new JTable(clientsTableModel);
        clientsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        clientsTable.getTableHeader().setReorderingAllowed(false);
        clientsTable.setRowHeight(30);
        clientsTable.setFont(new Font("Arial", Font.PLAIN, 14));
        
        // Définir les largeurs des colonnes
        clientsTable.getColumnModel().getColumn(0).setPreferredWidth(50);  // ID
        clientsTable.getColumnModel().getColumn(1).setPreferredWidth(150); // Nom
        clientsTable.getColumnModel().getColumn(2).setPreferredWidth(150); // Prénom
        clientsTable.getColumnModel().getColumn(3).setPreferredWidth(200); // Email
        clientsTable.getColumnModel().getColumn(4).setPreferredWidth(100); // Type Client
        
        // Ajouter un écouteur de sélection pour activer/désactiver le bouton de suppression
        clientsTable.getSelectionModel().addListSelectionListener(e -> {
            deleteClientButton.setEnabled(clientsTable.getSelectedRow() != -1);
        });
        
        // Ajouter les composants au panneau
        panel.add(actionPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(clientsTable), BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Créer le panneau pour la gestion des administrateurs
     * @return Le panneau créé
     */
    private JPanel createAdminsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        // Boutons d'action
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        actionPanel.setBackground(Color.WHITE);
        actionPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        addAdminButton = new JButton("Ajouter Administrateur");
        addAdminButton.setBackground(new Color(40, 167, 69)); // Vert
        addAdminButton.setForeground(Color.WHITE);
        addAdminButton.setFont(new Font("Arial", Font.BOLD, 14));
        addAdminButton.setFocusPainted(false);
        addAdminButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        actionPanel.add(addAdminButton);
        
        deleteAdminButton = new JButton("Supprimer Administrateur");
        deleteAdminButton.setBackground(new Color(220, 53, 69)); // Rouge
        deleteAdminButton.setForeground(Color.WHITE);
        deleteAdminButton.setFont(new Font("Arial", Font.BOLD, 14));
        deleteAdminButton.setFocusPainted(false);
        deleteAdminButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        deleteAdminButton.setEnabled(false); // Désactivé par défaut
        actionPanel.add(deleteAdminButton);
        
        // Tableau des administrateurs
        String[] adminColumns = {"ID", "Email"};
        adminsTableModel = new DefaultTableModel(adminColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Tableau non éditable
            }
        };
        
        adminsTable = new JTable(adminsTableModel);
        adminsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        adminsTable.getTableHeader().setReorderingAllowed(false);
        adminsTable.setRowHeight(30);
        adminsTable.setFont(new Font("Arial", Font.PLAIN, 14));
        
        // Définir les largeurs des colonnes
        adminsTable.getColumnModel().getColumn(0).setPreferredWidth(50);  // ID
        adminsTable.getColumnModel().getColumn(1).setPreferredWidth(300); // Email
        
        // Ajouter un écouteur de sélection pour activer/désactiver le bouton de suppression
        adminsTable.getSelectionModel().addListSelectionListener(e -> {
            deleteAdminButton.setEnabled(adminsTable.getSelectedRow() != -1);
        });
        
        // Ajouter les composants au panneau
        panel.add(actionPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(adminsTable), BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Afficher la liste des clients
     * @param clients Liste des clients à afficher
     */
    public void afficherClients(List<Client> clients) {
        // Vider la table
        clientsTableModel.setRowCount(0);
        
        // Ajouter chaque client à la table
        for (Client client : clients) {
            Object[] row = {
                client.getIdClient(),
                client.getNom(),
                client.getPrenom(),
                client.getEmail(),
                client.getTypeClient()
            };
            clientsTableModel.addRow(row);
        }
    }
    
    /**
     * Afficher la liste des administrateurs
     * @param admins Liste des administrateurs à afficher
     */
    public void afficherAdmins(List<Admin> admins) {
        // Vider la table
        adminsTableModel.setRowCount(0);
        
        // Ajouter chaque administrateur à la table
        for (Admin admin : admins) {
            Object[] row = {
                admin.getIdAdmin(),
                admin.getEmail()
            };
            adminsTableModel.addRow(row);
        }
    }
    
    /**
     * Récupérer l'ID du client sélectionné
     * @return L'ID du client sélectionné ou -1 si aucun client n'est sélectionné
     */
    public int getSelectedClientId() {
        int selectedRow = clientsTable.getSelectedRow();
        if (selectedRow != -1) {
            return (int) clientsTableModel.getValueAt(selectedRow, 0);
        }
        return -1;
    }
    
    /**
     * Récupérer l'ID de l'administrateur sélectionné
     * @return L'ID de l'administrateur sélectionné ou -1 si aucun administrateur n'est sélectionné
     */
    public int getSelectedAdminId() {
        int selectedRow = adminsTable.getSelectedRow();
        if (selectedRow != -1) {
            return (int) adminsTableModel.getValueAt(selectedRow, 0);
        }
        return -1;
    }
    
    /**
     * Définir l'écouteur pour le bouton Ajouter Client
     * @param listener L'écouteur à définir
     */
    public void setAddClientButtonListener(ActionListener listener) {
        addClientButton.addActionListener(listener);
    }
    
    /**
     * Définir l'écouteur pour le bouton Supprimer Client
     * @param listener L'écouteur à définir
     */
    public void setDeleteClientButtonListener(ActionListener listener) {
        deleteClientButton.addActionListener(listener);
    }
    
    /**
     * Définir l'écouteur pour le bouton Ajouter Administrateur
     * @param listener L'écouteur à définir
     */
    public void setAddAdminButtonListener(ActionListener listener) {
        addAdminButton.addActionListener(listener);
    }
    
    /**
     * Définir l'écouteur pour le bouton Supprimer Administrateur
     * @param listener L'écouteur à définir
     */
    public void setDeleteAdminButtonListener(ActionListener listener) {
        deleteAdminButton.addActionListener(listener);
    }
}
