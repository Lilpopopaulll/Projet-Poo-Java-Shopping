package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Map;

/**
 * Vue pour l'affichage des statistiques de vente
 */
public class StatisticsView extends JPanel {
    private JTabbedPane tabbedPane;
    private JButton refreshButton;
    
    /**
     * Constructeur
     */
    public StatisticsView() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        
        // Panneau de titre et boutons
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Titre
        JLabel titleLabel = new JLabel("Statistiques de Vente");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        // Bouton de rafraîchissement
        refreshButton = new JButton("Rafraîchir les données");
        refreshButton.setBackground(new Color(0, 123, 255)); // Bleu
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setFont(new Font("Arial", Font.BOLD, 14));
        refreshButton.setFocusPainted(false);
        refreshButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        headerPanel.add(refreshButton, BorderLayout.EAST);
        
        add(headerPanel, BorderLayout.NORTH);
       
        // Panneau à onglets pour les différentes catégories
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.PLAIN, 14));
        add(tabbedPane, BorderLayout.CENTER);
        
        // Panneau d'information
        JPanel infoPanel = new JPanel();
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
      
        
        add(infoPanel, BorderLayout.SOUTH);
    }
    
    /**
     * Initialiser l'interface utilisateur avec les panneaux de graphiques
     * @param categoryPanels Map contenant les panneaux de graphiques par catégorie
     */
    public void initialiserInterfaceUtilisateur(Map<String, JPanel> categoryPanels) {
        tabbedPane.removeAll();
        
        // Si aucune donnée n'est disponible
        if (categoryPanels.isEmpty()) {
            JPanel noDataPanel = new JPanel(new BorderLayout());
            noDataPanel.setBackground(Color.WHITE);
            
            JLabel noDataLabel = new JLabel("Aucune donnée de vente disponible", JLabel.CENTER);
            noDataLabel.setFont(new Font("Arial", Font.BOLD, 16));
            noDataPanel.add(noDataLabel, BorderLayout.CENTER);
            
            tabbedPane.addTab("Aucune donnée", noDataPanel);
        } else {
            // Ajouter les onglets pour chaque catégorie
            categoryPanels.forEach((title, panel) -> tabbedPane.addTab(title, panel));
        }
        
        revalidate();
        repaint();
    }
    
    /**
     * Pour la compatibilité avec le code existant
     */
    public void initUI(Map<String, JPanel> categoryPanels) {
        initialiserInterfaceUtilisateur(categoryPanels);
    }
    
    /**
     * Définir l'écouteur pour le bouton de rafraîchissement
     * @param listener L'écouteur à définir
     */
    public void definirEcouteurBoutonRafraichissement(ActionListener listener) {
        refreshButton.addActionListener(listener);
    }
}
