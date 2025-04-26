package view;

import model.Marque;
import controller.MarqueClickListener;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MarqueView extends JPanel {
    private static final int CARD_WIDTH = 200;
    private static final int CARD_HEIGHT = 250;
    private MarqueClickListener marqueClickListener;
    
    public MarqueView() {
        setLayout(new BorderLayout());
        setBackground(Color.decode("#F5F5F5"));
        setBorder(BorderFactory.createEmptyBorder(20, 40, 40, 40));
        
        // Définir une hauteur préférée (environ 30% de la hauteur de l'écran)
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int preferredHeight = (int)(screenSize.height * 0.3);
        setPreferredSize(new Dimension(screenSize.width, preferredHeight));
        
        // Titre de la section
        JLabel titleLabel = new JLabel("Nos Marques");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        add(titleLabel, BorderLayout.NORTH);
        
        // Créer un panneau vide pour les marques
        JPanel emptyPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        emptyPanel.setBackground(Color.decode("#F5F5F5"));
        add(emptyPanel, BorderLayout.CENTER);
    }
    
    public void afficherMarques(List<Marque> marques) {
        // Supprimer tous les composants existants sauf le titre
        Component titleComponent = getComponent(0);
        removeAll();
        add(titleComponent, BorderLayout.NORTH);
        
        // Créer un panneau principal avec BoxLayout vertical
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.decode("#F5F5F5"));
        
        // Créer un panneau pour les cartes de marques avec FlowLayout
        JPanel marquesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        marquesPanel.setBackground(Color.decode("#F5F5F5"));
        
        // Ajouter les cartes pour chaque marque
        for (Marque marque : marques) {
            JPanel card = createMarqueCard(marque);
            marquesPanel.add(card);
            System.out.println("Carte ajoutée pour la marque: " + marque.getNom());
        }
        
        // Ajouter le panneau des marques au panneau principal
        mainPanel.add(marquesPanel);
        
        // Ajouter un texte explicatif sous les cartes
        JLabel explanationLabel = new JLabel("<html><div style='text-align: center;'>Cliquez sur une marque pour voir tous ses articles</div></html>");
        explanationLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        explanationLabel.setForeground(Color.decode("#6c757d"));
        explanationLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        explanationLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        
        // Créer un panneau pour centrer le texte explicatif
        JPanel explanationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        explanationPanel.setBackground(Color.decode("#F5F5F5"));
        explanationPanel.add(explanationLabel);
        
        // Ajouter le panneau d'explication au panneau principal
        mainPanel.add(explanationPanel);
        
        // Ajouter le panneau principal au centre
        add(mainPanel, BorderLayout.CENTER);
        
        // Forcer le rafraîchissement
        revalidate();
        repaint();
    }
    
    private JPanel createMarqueCard(Marque marque) {
        // Créer une carte pour la marque
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(CARD_WIDTH, CARD_HEIGHT));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.decode("#dee2e6")),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        // Logo de la marque (image)
        JLabel logoLabel = new JLabel();
        logoLabel.setPreferredSize(new Dimension(150, 150));
        logoLabel.setMaximumSize(new Dimension(150, 150));
        logoLabel.setMinimumSize(new Dimension(150, 150));
        logoLabel.setHorizontalAlignment(JLabel.CENTER);
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Charger l'image de la marque
        try {
            String imagePath = marque.getLogo();
            ImageIcon logoIcon = null;
            
            if (imagePath == null || imagePath.isEmpty()) {
                // Utiliser le nom de la marque comme nom de fichier par défaut
                imagePath = marque.getNom().toLowerCase().replace(" ", "_") + ".jpg";
            }
            
            System.out.println("Chargement de l'image pour " + marque.getNom() + ": " + imagePath);
            
            // Essayer de charger l'image depuis différents chemins
            if (!imagePath.startsWith("http")) {
                // Essayer d'abord avec le chemin relatif
                logoIcon = new ImageIcon("src/view/images/" + imagePath);
                
                // Si l'image n'a pas pu être chargée, essayer avec un chemin absolu
                if (logoIcon.getIconWidth() <= 0) {
                    String projectPath = System.getProperty("user.dir");
                    String fullPath = projectPath + "/src/view/images/" + imagePath;
                    System.out.println("Chemin complet de l'image: " + fullPath);
                    
                    // Essayer avec différentes extensions
                    String[] extensions = {".jpg", ".jpeg", ".png"};
                    String baseFileName = imagePath;
                    
                    // Supprimer l'extension existante si présente
                    int dotIndex = baseFileName.lastIndexOf(".");
                    if (dotIndex > 0) {
                        baseFileName = baseFileName.substring(0, dotIndex);
                    }
                    
                    java.io.File file = null;
                    boolean imageFound = false;
                    
                    // Essayer chaque extension
                    for (String ext : extensions) {
                        String fileNameWithExt = baseFileName + ext;
                        fullPath = projectPath + "/src/view/images/" + fileNameWithExt;
                        file = new java.io.File(fullPath);
                        
                        if (file.exists()) {
                            logoIcon = new ImageIcon(fullPath);
                            System.out.println("Image trouvée: " + file.getAbsolutePath());
                            imageFound = true;
                            break;
                        }
                    }
                    
                    if (!imageFound) {
                        System.out.println("Aucune image trouvée pour " + marque.getNom() + " avec les extensions testées");
                    }
                }
            } else {
                // Utiliser l'URL telle quelle
                logoIcon = new ImageIcon(imagePath);
            }
            
            // Si l'image a été chargée, la redimensionner
            if (logoIcon != null && logoIcon.getIconWidth() > 0) {
                Image img = logoIcon.getImage();
                Image resizedImg = img.getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                logoIcon = new ImageIcon(resizedImg);
                logoLabel.setIcon(logoIcon);
                System.out.println("Image chargée avec succès pour " + marque.getNom());
            } else {
                // Utiliser un texte de remplacement si l'image n'a pas pu être chargée
                logoLabel.setText(marque.getNom().substring(0, 1).toUpperCase());
                logoLabel.setFont(new Font("Arial", Font.BOLD, 48));
                logoLabel.setForeground(Color.WHITE);
                logoLabel.setBackground(Color.decode("#007bff"));
                logoLabel.setOpaque(true);
                System.out.println("Utilisation d'une lettre comme placeholder pour " + marque.getNom());
            }
        } catch (Exception e) {
            // Utiliser un placeholder si l'image ne peut pas être chargée
            logoLabel.setText(marque.getNom().substring(0, 1).toUpperCase());
            logoLabel.setFont(new Font("Arial", Font.BOLD, 48));
            logoLabel.setForeground(Color.WHITE);
            logoLabel.setBackground(Color.decode("#007bff"));
            logoLabel.setOpaque(true);
            System.err.println("Erreur lors du chargement de l'image pour " + marque.getNom() + ": " + e.getMessage());
            e.printStackTrace();
        }
        
        // Nom de la marque
        JLabel nomLabel = new JLabel(marque.getNom());
        nomLabel.setFont(new Font("Arial", Font.BOLD, 16));
        nomLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Description de la marque (limitée à une ligne)
        JLabel descriptionLabel = new JLabel("<html><div style='text-align: center; width: 150px;'>" + 
                                           marque.getDescription() + "</div></html>");
        descriptionLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        descriptionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Ajouter les composants à la carte
        card.add(logoLabel);
        card.add(Box.createVerticalStrut(10));
        card.add(nomLabel);
        card.add(Box.createVerticalStrut(5));
        card.add(descriptionLabel);
        
        // Ajouter un écouteur de clic sur la carte
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (marqueClickListener != null) {
                    marqueClickListener.onMarqueClick(marque);
                }
            }
            
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                card.setBackground(Color.decode("#f8f9fa"));
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                card.setBackground(Color.WHITE);
            }
        });
        
        return card;
    }
    
    public void setMarqueClickListener(MarqueClickListener listener) {
        this.marqueClickListener = listener;
    }
}
