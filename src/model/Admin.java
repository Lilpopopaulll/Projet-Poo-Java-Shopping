package model;

/**
 * Classe représentant un administrateur
 */
public class Admin {
    private int idAdmin;
    private String email;
    private String motDePasse;

    /**
     * Constructeur
     * @param idAdmin ID de l'administrateur
     * @param email Email de l'administrateur
     * @param motDePasse Mot de passe de l'administrateur
     */
    public Admin(int idAdmin, String email, String motDePasse) {
        this.idAdmin = idAdmin;
        this.email = email;
        this.motDePasse = motDePasse;
    }

    /**
     * Constructeur sans ID (pour la création)
     * @param email Email de l'administrateur
     * @param motDePasse Mot de passe de l'administrateur
     */
    public Admin(String email, String motDePasse) {
        this.email = email;
        this.motDePasse = motDePasse;
    }

    // Getters et Setters
    public int getIdAdmin() {
        return idAdmin;
    }

    public void setIdAdmin(int idAdmin) {
        this.idAdmin = idAdmin;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }
}
