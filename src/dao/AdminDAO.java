package dao;

import model.Admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe d'accès aux données pour les administrateurs
 */
public class AdminDAO {
    private Connection connection;

    /**
     * Constructeur
     * @param connection Connexion à la base de données
     */
    public AdminDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * Récupérer un administrateur par son ID
     * @param id ID de l'administrateur
     * @return L'administrateur correspondant ou null s'il n'existe pas
     */
    public Admin getById(int id) {
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Admin WHERE idAdmin = ?");
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Admin(
                    rs.getInt("idAdmin"),
                    rs.getString("email"),
                    rs.getString("motDePasse")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Récupérer un administrateur par son email
     * @param email Email de l'administrateur
     * @return L'administrateur correspondant ou null s'il n'existe pas
     */
    public Admin getByEmail(String email) {
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Admin WHERE email = ?");
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Admin(
                    rs.getInt("idAdmin"),
                    rs.getString("email"),
                    rs.getString("motDePasse")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Récupérer tous les administrateurs
     * @return Liste de tous les administrateurs
     */
    public List<Admin> getAll() {
        List<Admin> admins = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Admin");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                admins.add(new Admin(
                    rs.getInt("idAdmin"),
                    rs.getString("email"),
                    rs.getString("motDePasse")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return admins;
    }

    /**
     * Ajouter un nouvel administrateur
     * @param admin Administrateur à ajouter
     * @return L'administrateur ajouté avec son ID généré, ou null en cas d'erreur
     */
    public Admin ajouter(Admin admin) {
        try {
            PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO Admin (email, motDePasse) VALUES (?, ?)",
                PreparedStatement.RETURN_GENERATED_KEYS
            );
            stmt.setString(1, admin.getEmail());
            stmt.setString(2, admin.getMotDePasse());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                return null;
            }
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    admin.setIdAdmin(generatedKeys.getInt(1));
                    return admin;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Supprimer un administrateur
     * @param idAdmin ID de l'administrateur à supprimer
     * @return true si la suppression a réussi, false sinon
     */
    public boolean delete(int idAdmin) {
        try {
            PreparedStatement stmt = connection.prepareStatement(
                "DELETE FROM Admin WHERE idAdmin = ?"
            );
            stmt.setInt(1, idAdmin);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Mettre à jour un administrateur
     * @param admin Administrateur à mettre à jour
     * @return true si la mise à jour a réussi, false sinon
     */
    public boolean update(Admin admin) {
        try {
            PreparedStatement stmt = connection.prepareStatement(
                "UPDATE Admin SET email = ?, motDePasse = ? WHERE idAdmin = ?"
            );
            stmt.setString(1, admin.getEmail());
            stmt.setString(2, admin.getMotDePasse());
            stmt.setInt(3, admin.getIdAdmin());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Vérifier les identifiants d'un administrateur
     * @param email Email de l'administrateur
     * @param motDePasse Mot de passe de l'administrateur
     * @return L'administrateur si les identifiants sont corrects, null sinon
     */
    public Admin authentifier(String email, String motDePasse) {
        try {
            PreparedStatement stmt = connection.prepareStatement(
                "SELECT * FROM Admin WHERE email = ? AND motDePasse = ?"
            );
            stmt.setString(1, email);
            stmt.setString(2, motDePasse);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Admin(
                    rs.getInt("idAdmin"),
                    rs.getString("email"),
                    rs.getString("motDePasse")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
