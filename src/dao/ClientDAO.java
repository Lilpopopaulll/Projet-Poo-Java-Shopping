package dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Client;

/**
 * DAO pour les clients
 */
public class ClientDAO {

    private Connection connection;

    public ClientDAO(Connection connection) {
        this.connection = connection;
    }

    public Client getById(int id) {
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Client WHERE idClient = ?");
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Client(
                        rs.getInt("idClient"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("email"),
                        rs.getString("motDePasse"),
                        rs.getString("typeClient")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Client getByEmail(String email) {
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Client WHERE email = ?");
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Client(
                        rs.getInt("idClient"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("email"),
                        rs.getString("motDePasse"),
                        rs.getString("typeClient")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Récupérer tous les clients
     * @return Liste de tous les clients
     */
    public List<Client> getAll() {
        List<Client> clients = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Client");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                clients.add(new Client(
                    rs.getInt("idClient"),
                    rs.getString("nom"),
                    rs.getString("prenom"),
                    rs.getString("email"),
                    rs.getString("motDePasse"),
                    rs.getString("typeClient")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clients;
    }

    public void create(Client client) {
        try {
            PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO Client(nom, prenom, email, motDePasse, typeClient) VALUES (?, ?, ?, ?, ?)"
            );
            stmt.setString(1, client.getNom());
            stmt.setString(2, client.getPrenom());
            stmt.setString(3, client.getEmail());
            stmt.setString(4, client.getMotDePasse());
            stmt.setString(5, client.getTypeClient());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
