package model;

/**
 * Énumération représentant les différents états de connexion possibles
 */
public enum EtatConnexion {
    NON_CONNECTE("Non connecté"),
    CONNEXION_CLIENT("Connexion Client"),
    CONNEXION_ADMINISTRATEUR("Connexion Administrateur");
    
    private final String libelle;
    
    EtatConnexion(String libelle) {
        this.libelle = libelle;
    }
    
    public String getLibelle() {
        return libelle;
    }
}
