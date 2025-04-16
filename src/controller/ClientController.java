package controller;

import DAO.ClientDAO;
import model.Client;

import java.sql.Connection;
import java.util.Scanner;

public class ClientController {

    private ClientDAO clientDAO;

    public ClientController(Connection connection) {
        this.clientDAO = new ClientDAO(connection);
    }

    // 🔐 Connexion d'un client existant
    public Client seConnecter(String email, String motDePasse) {
        Client client = clientDAO.getByEmail(email);
        if (client != null && client.getMotDePasse().equals(motDePasse)) {
            System.out.println("✅ Connexion réussie !");
            return client;
        } else {
            System.out.println("❌ Email ou mot de passe incorrect.");
            return null;
        }
    }

    // 📝 Inscription d'un nouveau client
    public void inscrireClient() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== Inscription ===");

        System.out.print("Nom : ");
        String nom = scanner.nextLine();

        System.out.print("Prénom : ");
        String prenom = scanner.nextLine();

        System.out.print("Email : ");
        String email = scanner.nextLine();

        System.out.print("Mot de passe : ");
        String motDePasse = scanner.nextLine();

        String typeClient = "nouveau";

        Client client = new Client(0, nom, prenom, email, motDePasse, typeClient);
        clientDAO.create(client);

        System.out.println("✅ Inscription réussie !");
    }

    // 📋 Afficher les infos d’un client
    public void afficherProfil(Client client) {
        System.out.println("=== Profil du client ===");
        System.out.println("Nom : " + client.getNom());
        System.out.println("Prénom : " + client.getPrenom());
        System.out.println("Email : " + client.getEmail());
        System.out.println("Type : " + client.getTypeClient());
    }
}
