import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import DAO.ClientDAO;
import model.Client;
import controller.*;

public class Main {

    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/shpoopping"; // ⚠️ à adapter
        String user = "root";
        String password = "";

        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            System.out.println("Connexion réussie");

            // Tester : créer un nouveau client
            ClientDAO clientDAO = new ClientDAO(connection);
            Client newClient = new Client(0, "Doe", "John", "john.doe@email.com", "pass123", "standard");
            clientDAO.create(newClient);

            // Tester : récupérer un client par ID
            Client fetchedClient = clientDAO.getById(1);
            if (fetchedClient != null) {
                System.out.println("Client trouvé : " + fetchedClient.getPrenom());
            } else {
                System.out.println("Client non trouvé");
            }

            ClientController clientCtrl = new ClientController(connection);

            // Se connecter :
            Client clientConnecte = clientCtrl.seConnecter("paul.drochon@gmail.com", "1234");

            // Afficher les infos :
            if (clientConnecte != null) {
                clientCtrl.afficherProfil(clientConnecte);
            }

        } catch (SQLException e) {
            System.out.println("Erreur de connexion");
            e.printStackTrace();
        }


    }
}
