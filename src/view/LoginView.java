package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class LoginView extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;

    public LoginView() {
        setTitle("Connexion Client");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centrer la fenêtre

        // Panel principal avec GridBagLayout pour un design fluide
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Espacement entre les composants

        // Label et champ pour le nom d'utilisateur
        JLabel usernameLabel = new JLabel("Nom d'utilisateur:");
        usernameField = new JTextField(20);
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(usernameLabel, gbc);
        gbc.gridx = 1;
        panel.add(usernameField, gbc);

        // Label et champ pour le mot de passe
        JLabel passwordLabel = new JLabel("Mot de passe:");
        passwordField = new JPasswordField(20);
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(passwordLabel, gbc);
        gbc.gridx = 1;
        panel.add(passwordField, gbc);

        // Panel pour les boutons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        
        // Bouton de connexion
        loginButton = new JButton("Se connecter");
        loginButton.setPreferredSize(new Dimension(130, 40));
        
        // Bouton d'inscription
        registerButton = new JButton("S'inscrire");
        registerButton.setPreferredSize(new Dimension(130, 40));
        
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        panel.add(buttonPanel, gbc);

        // Personnalisation du design
        panel.setBackground(Color.decode("#f0f0f0"));
        buttonPanel.setBackground(Color.decode("#f0f0f0"));
        
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setBackground(new Color(50, 150, 255)); // Bleu pour connexion
        loginButton.setForeground(Color.white);
        loginButton.setFocusPainted(false);
        
        registerButton.setFont(new Font("Arial", Font.BOLD, 14));
        registerButton.setBackground(new Color(40, 167, 69)); // Vert pour inscription
        registerButton.setForeground(Color.white);
        registerButton.setFocusPainted(false);

        // Ajouter le panel à la fenêtre
        add(panel);
    }

    public String getUsername() {
        return usernameField.getText();
    }

    public String getPassword() {
        return new String(passwordField.getPassword());
    }

    public void setLoginButtonListener(ActionListener listener) {
        loginButton.addActionListener(listener);
    }
    
    public void setRegisterButtonListener(ActionListener listener) {
        registerButton.addActionListener(listener);
    }
}
