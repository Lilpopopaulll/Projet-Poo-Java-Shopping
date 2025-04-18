package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class RegisterView extends JFrame {
    private JTextField nomField;
    private JTextField prenomField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton registerButton;
    private JButton cancelButton;

    public RegisterView() {
        setTitle("Inscription Client");
        setSize(450, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Centrer la fenêtre

        // Panel principal avec GridBagLayout pour un design fluide
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Espacement entre les composants
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Titre
        JLabel titleLabel = new JLabel("Créer un compte", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        // Nom
        JLabel nomLabel = new JLabel("Nom:");
        nomField = new JTextField(20);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(nomLabel, gbc);
        gbc.gridx = 1;
        panel.add(nomField, gbc);

        // Prénom
        JLabel prenomLabel = new JLabel("Prénom:");
        prenomField = new JTextField(20);
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(prenomLabel, gbc);
        gbc.gridx = 1;
        panel.add(prenomField, gbc);

        // Email
        JLabel emailLabel = new JLabel("Email:");
        emailField = new JTextField(20);
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(emailLabel, gbc);
        gbc.gridx = 1;
        panel.add(emailField, gbc);

        // Mot de passe
        JLabel passwordLabel = new JLabel("Mot de passe:");
        passwordField = new JPasswordField(20);
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(passwordLabel, gbc);
        gbc.gridx = 1;
        panel.add(passwordField, gbc);

        // Panel pour les boutons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        
        // Bouton d'inscription
        registerButton = new JButton("S'inscrire");
        registerButton.setPreferredSize(new Dimension(130, 40));
        
        // Bouton d'annulation
        cancelButton = new JButton("Annuler");
        cancelButton.setPreferredSize(new Dimension(130, 40));
        
        buttonPanel.add(registerButton);
        buttonPanel.add(cancelButton);
        
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        panel.add(buttonPanel, gbc);

        // Personnalisation du design
        panel.setBackground(Color.decode("#f0f0f0"));
        buttonPanel.setBackground(Color.decode("#f0f0f0"));
        
        nomLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        prenomLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        
        registerButton.setFont(new Font("Arial", Font.BOLD, 14));
        registerButton.setBackground(new Color(40, 167, 69)); // Vert pour inscription
        registerButton.setForeground(Color.white);
        registerButton.setFocusPainted(false);
        
        cancelButton.setFont(new Font("Arial", Font.BOLD, 14));
        cancelButton.setBackground(new Color(108, 117, 125)); // Gris pour annuler
        cancelButton.setForeground(Color.white);
        cancelButton.setFocusPainted(false);

        // Ajouter le panel à la fenêtre
        add(panel);
    }

    public String getNom() {
        return nomField.getText();
    }

    public String getPrenom() {
        return prenomField.getText();
    }

    public String getEmail() {
        return emailField.getText();
    }

    public String getPassword() {
        return new String(passwordField.getPassword());
    }

    public void setRegisterButtonListener(ActionListener listener) {
        registerButton.addActionListener(listener);
    }
    
    public void setCancelButtonListener(ActionListener listener) {
        cancelButton.addActionListener(listener);
    }
}
