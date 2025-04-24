package view;

import javax.swing.*;
import java.awt.*;
import view.theme.AppTheme;

public class HeroBannerView extends JPanel {
    
    private final String title = "CUSTOMIZATION";
    private final String slogan = "CREATE YOUR OWN UNIQUE LOOK";
    
    public HeroBannerView() {
        setLayout(new BorderLayout());
        setBackground(AppTheme.BACKGROUND_DARK);
        setBorder(BorderFactory.createEmptyBorder(80, 40, 80, 40));
        
        // Création du panneau central qui contiendra le titre et le slogan
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(AppTheme.BACKGROUND_DARK);
        
        // Création du titre avec un style moderne et minimaliste
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 72));
        titleLabel.setForeground(AppTheme.TEXT_WHITE);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Création du slogan
        JLabel sloganLabel = new JLabel(slogan);
        sloganLabel.setFont(new Font("Arial", Font.BOLD, 28));
        sloganLabel.setForeground(AppTheme.TEXT_GRAY);
        sloganLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Ajout d'un effet visuel avec une ligne décorative
        JPanel decorLine = new JPanel();
        decorLine.setBackground(AppTheme.TEXT_WHITE);
        decorLine.setPreferredSize(new Dimension(100, 3));
        decorLine.setMaximumSize(new Dimension(100, 3));
        decorLine.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Ajout d'un texte descriptif
        JTextArea descriptionText = new JTextArea(
            "Each piece is hand-customized to your specifications. Our designs " +
            "combine street style with high-end craftsmanship for a unique look " +
            "that sets you apart from the crowd."
        );
        descriptionText.setFont(new Font("Arial", Font.PLAIN, 16));
        descriptionText.setForeground(AppTheme.TEXT_GRAY);
        descriptionText.setBackground(AppTheme.BACKGROUND_DARK);
        descriptionText.setWrapStyleWord(true);
        descriptionText.setLineWrap(true);
        descriptionText.setEditable(false);
        descriptionText.setAlignmentX(Component.LEFT_ALIGNMENT);
        descriptionText.setMaximumSize(new Dimension(600, 100));
        
        // Bouton "SHOP NOW"
        JButton shopButton = new JButton("SHOP NOW");
        shopButton.setFont(new Font("Arial", Font.BOLD, 16));
        shopButton.setForeground(AppTheme.TEXT_WHITE);
        shopButton.setBackground(AppTheme.BACKGROUND_DARK);
        shopButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppTheme.TEXT_WHITE, 1),
                BorderFactory.createEmptyBorder(10, 30, 10, 30)
        ));
        shopButton.setFocusPainted(false);
        shopButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        shopButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        shopButton.setMaximumSize(new Dimension(200, 50));
        
        // Ajout d'espacement entre les éléments
        centerPanel.add(titleLabel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        centerPanel.add(decorLine);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        centerPanel.add(sloganLabel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        centerPanel.add(descriptionText);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        centerPanel.add(shopButton);
        
        // Ajout des éléments au panneau principal
        add(centerPanel, BorderLayout.WEST);
    }
}
