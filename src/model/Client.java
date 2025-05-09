package model;

public class Client {
    private int idClient;
    private String nom;
    private String prenom;
    private String email;
    private String motDePasse;
    private String typeClient;

    // Constructeur
    public Client(int idClient, String nom, String prenom, String email, String motDePasse, String typeClient) {
        this.idClient = idClient;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.motDePasse = motDePasse;
        this.typeClient = typeClient;
    }

    // Getters et setters
    public int getIdClient() { return idClient; }
    public String getNom() { return nom; }
    public String getPrenom() { return prenom; }
    public String getEmail() { return email; }
    public String getMotDePasse() { return motDePasse; }
    public String getTypeClient() { return typeClient; }

    public void setIdClient(int idClient) { this.idClient = idClient; }
    public void setNom(String nom) { this.nom = nom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public void setEmail(String email) { this.email = email; }
    public void setMotDePasse(String motDePasse) { this.motDePasse = motDePasse; }
    public void setTypeClient(String typeClient) { this.typeClient = typeClient; }

    // Méthode pour comparer le mot de passe de l'objet avec un mot de passe donné
    public boolean checkPassword(String motDePasse) {
        return this.motDePasse.equals(motDePasse); // Idéalement, on aurait un hash à vérifier ici
    }
}
