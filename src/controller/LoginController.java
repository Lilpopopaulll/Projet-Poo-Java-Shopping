package controller;

import dao.AdminDAO;
import dao.ClientDAO;
import model.Admin;
import model.Client;
import model.EtatConnexion;
import view.LoginView;
import view.RegisterView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

/**
 * Contrôleur pour gérer l'état de connexion des utilisateurs
 */
public class LoginController {
    private final LoginView loginView;
    private final ClientDAO clientDAO;
    private final AdminDAO adminDAO;
    private Client clientConnecte;
    private Admin adminConnecte;
    private EtatConnexion etatConnexion;
    private final List<LoginStateListener> listeners;

    /**
     * Constructeur du contrôleur de connexion
     * @param loginView La vue de connexion
     * @param connection La connexion à la base de données
     */
    public LoginController(LoginView loginView, Connection connection) {
        this.loginView = loginView;
        this.clientDAO = new ClientDAO(connection);
        this.adminDAO = new AdminDAO(connection);
        this.etatConnexion = EtatConnexion.NON_CONNECTE;
        this.listeners = new ArrayList<>();
        
        // Configurer le bouton de connexion
        this.loginView.setLoginButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tenterConnexion();
            }
        });
        
        // Configurer le bouton d'inscription
        this.loginView.setRegisterButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                afficherFenetreInscription();
            }
        });
    }
    
    /**
     * Affiche la fenêtre d'inscription
     */
    private void afficherFenetreInscription() {
        RegisterView registerView = new RegisterView();
        registerView.setLocationRelativeTo(loginView);
        
        // Configurer le bouton d'inscription
        registerView.setRegisterButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inscrireClient(registerView);
            }
        });
        
        // Configurer le bouton d'annulation
        registerView.setCancelButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerView.dispose();
            }
        });
        
        registerView.setVisible(true);
    }
    
    /**
     * Inscrit un nouveau client à partir des données du formulaire
     * @param registerView La vue d'inscription
     */
    private void inscrireClient(RegisterView registerView) {
        String nom = registerView.getNom();
        String prenom = registerView.getPrenom();
        String email = registerView.getEmail();
        String motDePasse = registerView.getPassword();
        
        // Vérification des champs
        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || motDePasse.isEmpty()) {
            JOptionPane.showMessageDialog(
                registerView,
                "Veuillez remplir tous les champs",
                "Erreur d'inscription",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        
        // Vérifier si l'email existe déjà
        Client clientExistant = clientDAO.getByEmail(email);
        if (clientExistant != null) {
            JOptionPane.showMessageDialog(
                registerView,
                "Un compte existe déjà avec cet email",
                "Erreur d'inscription",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        
        // Créer le nouveau client
        Client nouveauClient = new Client(0, nom, prenom, email, motDePasse, "CLIENT");
        clientDAO.create(nouveauClient);
        
        // Afficher un message de succès
        JOptionPane.showMessageDialog(
            registerView,
            "Inscription réussie ! Vous pouvez maintenant vous connecter.",
            "Inscription réussie",
            JOptionPane.INFORMATION_MESSAGE
        );
        
        // Fermer la fenêtre d'inscription
        registerView.dispose();
    }
    
    /**
     * Tente de connecter l'utilisateur avec les informations fournies
     */
    private void tenterConnexion() {
        String email = loginView.getUsername();
        String motDePasse = loginView.getPassword();
        
        if (email.isEmpty() || motDePasse.isEmpty()) {
            JOptionPane.showMessageDialog(
                loginView,
                "Veuillez remplir tous les champs",
                "Erreur de connexion",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        
        // Essayer d'abord l'authentification administrateur
        Admin admin = adminDAO.authentifier(email, motDePasse);
        
        if (admin != null) {
            // Connexion administrateur réussie
            this.adminConnecte = admin;
            this.clientConnecte = null;
            this.etatConnexion = EtatConnexion.CONNEXION_ADMINISTRATEUR;
            
            // Fermer la fenêtre de connexion
            loginView.setVisible(false);
            loginView.dispose();
            
            // Notifier les écouteurs du changement d'état
            notifierChangementEtat();
            
            // Afficher un message de bienvenue
            JOptionPane.showMessageDialog(
                null,
                "Bienvenue Administrateur " + admin.getEmail() + " !",
                "Connexion réussie",
                JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }
        
        // Si ce n'est pas un admin, essayer l'authentification client
        Client client = clientDAO.getByEmail(email);
        
        if (client == null) {
            JOptionPane.showMessageDialog(
                loginView,
                "Aucun compte trouvé avec cet email",
                "Erreur de connexion",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        
        // Vérifier le mot de passe
        if (!client.checkPassword(motDePasse)) {
            JOptionPane.showMessageDialog(
                loginView,
                "Mot de passe incorrect",
                "Erreur de connexion",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        
        // Connexion client réussie
        this.clientConnecte = client;
        this.adminConnecte = null;
        this.etatConnexion = EtatConnexion.CONNEXION_CLIENT;
        
        // Fermer la fenêtre de connexion
        loginView.setVisible(false);
        loginView.dispose();
        
        // Notifier les écouteurs du changement d'état
        notifierChangementEtat();
        
        // Afficher un message de bienvenue
        JOptionPane.showMessageDialog(
            null,
            "Bienvenue " + client.getPrenom() + " " + client.getNom() + " !",
            "Connexion réussie",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
    
    /**
     * Déconnecte l'utilisateur actuellement connecté
     */
    public void deconnecter() {
        this.clientConnecte = null;
        this.adminConnecte = null;
        this.etatConnexion = EtatConnexion.NON_CONNECTE;
        notifierChangementEtat();
    }
    
    /**
     * Ajoute un écouteur pour les changements d'état de connexion
     * @param listener L'écouteur à ajouter
     */
    public void addLoginStateListener(LoginStateListener listener) {
        if (listener != null && !listeners.contains(listener)) {
            listeners.add(listener);
        }
    }
    
    /**
     * Retire un écouteur pour les changements d'état de connexion
     * @param listener L'écouteur à retirer
     */
    public void removeLoginStateListener(LoginStateListener listener) {
        listeners.remove(listener);
    }
    
    /**
     * Notifie tous les écouteurs d'un changement d'état de connexion
     */
    private void notifierChangementEtat() {
        for (LoginStateListener listener : listeners) {
            listener.onLoginStateChanged(etatConnexion, clientConnecte, adminConnecte);
        }
    }
    
    /**
     * Retourne l'état de connexion actuel
     * @return L'état de connexion
     */
    public EtatConnexion getEtatConnexion() {
        return etatConnexion;
    }
    
    /**
     * Retourne le client actuellement connecté
     * @return Le client connecté ou null si aucun client n'est connecté
     */
    public Client getClientConnecte() {
        return clientConnecte;
    }
    
    /**
     * Retourne l'administrateur actuellement connecté
     * @return L'administrateur connecté ou null si aucun administrateur n'est connecté
     */
    public Admin getAdminConnecte() {
        return adminConnecte;
    }
    
    /**
     * Vérifie si un utilisateur est connecté
     * @return true si un utilisateur est connecté, false sinon
     */
    public boolean estConnecte() {
        return etatConnexion != EtatConnexion.NON_CONNECTE;
    }
    
    /**
     * Vérifie si l'utilisateur connecté est un administrateur
     * @return true si l'utilisateur est un administrateur, false sinon
     */
    public boolean estAdministrateur() {
        return etatConnexion == EtatConnexion.CONNEXION_ADMINISTRATEUR;
    }
    
    /**
     * Affiche la fenêtre de connexion
     */
    public void afficherFenetreConnexion() {
        loginView.setVisible(true);
    }
}