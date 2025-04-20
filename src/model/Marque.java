package model;

public class Marque {
    private int idMarque;
    private String nom;
    private String logo;
    private String description;

    public Marque(int idMarque, String nom, String logo, String description) {
        this.idMarque = idMarque;
        this.nom = nom;
        this.logo = logo;
        this.description = description;
    }

    // Getters
    public int getIdMarque() { return idMarque; }
    public String getNom() { return nom; }
    public String getLogo() { return logo; }
    public String getDescription() { return description; }

    // Setters
    public void setIdMarque(int idMarque) { this.idMarque = idMarque; }
    public void setNom(String nom) { this.nom = nom; }
    public void setLogo(String logo) { this.logo = logo; }
    public void setDescription(String description) { this.description = description; }
}
