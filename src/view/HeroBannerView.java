package view;

import javax.swing.*;
import java.awt.*;

public class HeroBannerView extends JPanel {
    
    private final String title = "ShPOOpping";
    private final String slogan = "La mode orientée objet à votre portée";
    
    public HeroBannerView() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE); // Fond blanc pour un design épuré
        setBorder(BorderFactory.createEmptyBorder(40, 0, 40, 0));
        
        // Création du panneau central qui contiendra le titre et le slogan
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);
        
        // Création du titre avec un style moderne et épuré
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 60));
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Création du slogan
        JLabel sloganLabel = new JLabel(slogan);
        sloganLabel.setFont(new Font("Arial", Font.ITALIC, 24));
        sloganLabel.setForeground(new Color(80, 80, 80)); // Gris foncé
        sloganLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Ajout d'un effet visuel avec une ligne décorative
        JPanel decorLine = new JPanel();
        decorLine.setBackground(Color.BLACK); // Ligne noire pour le design épuré
        decorLine.setPreferredSize(new Dimension(150, 2));
        decorLine.setMaximumSize(new Dimension(150, 2));
        decorLine.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Ajout d'espacement entre les éléments
        centerPanel.add(Box.createVerticalGlue());
        centerPanel.add(titleLabel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        centerPanel.add(decorLine);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        centerPanel.add(sloganLabel);
        centerPanel.add(Box.createVerticalGlue());
        
        // Ajout des éléments au panneau principal
        add(centerPanel, BorderLayout.CENTER);
    }
}
