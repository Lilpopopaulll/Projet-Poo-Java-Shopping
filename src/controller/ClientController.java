package controller;

import dao.ClientDAO;
import model.Client;
import javax.swing.JOptionPane;

import java.sql.Connection;

public class ClientController {

    private ClientDAO clientDAO;
    private Client clientConnecte;

    public ClientController(Connection connection) {
        this.clientDAO = new ClientDAO(connection);
    }

    // Connexion d'un client existant
    public boolean seConnecter(String email, String motDePasse) {
        Client client = clientDAO.getByEmail(email);
        
        if (client != null && client.getMotDePasse().equals(motDePasse)) {
            this.clientConnecte = client;
            return true;
        } else {
            return false;
        }
    }

    // Inscription d'un nouveau client
    public boolean inscrireClient(String nom, String prenom, String email, String motDePasse) {
        // Vérifier si l'email existe déjà
        Client clientExistant = clientDAO.getByEmail(email);
        if (clientExistant != null) {
            return false;
        }
        
        // Créer un nouveau client
        Client nouveauClient = new Client(0, nom, prenom, email, motDePasse, "client");
        
        // Ajouter le client à la base de données
        clientDAO.create(nouveauClient);
        
        // Récupérer le client nouvellement créé pour obtenir son ID
        Client clientCree = clientDAO.getByEmail(email);
        
        // Vérifier si l'ajout a réussi
        if (clientCree != null && clientCree.getIdClient() > 0) {
            this.clientConnecte = clientCree;
            return true;
        } else {
            return false;
        }
    }

    // Afficher les infos d’un client
    public void afficherProfil() {
        if (clientConnecte != null) {
            // Afficher les informations du client dans une boîte de dialogue
            JOptionPane.showMessageDialog(null,
                "Profil du client\n\n" +
                "Nom : " + clientConnecte.getNom() + "\n" +
                "Prénom : " + clientConnecte.getPrenom() + "\n" +
                "Email : " + clientConnecte.getEmail() + "\n" +
                "Type : " + clientConnecte.getTypeClient(),
                "Profil",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
