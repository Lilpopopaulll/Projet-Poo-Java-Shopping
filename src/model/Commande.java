package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Commande {
    private int idCommande;
    private int idClient;
    private Date dateCommande;
    private int total;
    private String panier; // "panier" pour une commande en cours, autre valeur pour une commande validée
    private List<LigneCommande> lignesCommande;

    // Constructeur
    public Commande(int idCommande, int idClient, Date dateCommande, int total, String panier) {
        this.idCommande = idCommande;
        this.idClient = idClient;
        this.dateCommande = dateCommande;
        this.total = total;
        this.panier = panier;
        this.lignesCommande = new ArrayList<>();
    }

    // Getters et setters
    public int getIdCommande() {
        return idCommande;
    }

    public void setIdCommande(int idCommande) {
        this.idCommande = idCommande;
    }

    public int getIdClient() {
        return idClient;
    }

    public void setIdClient(int idClient) {
        this.idClient = idClient;
    }

    public Date getDateCommande() {
        return dateCommande;
    }

    public void setDateCommande(Date dateCommande) {
        this.dateCommande = dateCommande;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getPanier() {
        return panier;
    }
    
    public void setPanier(String panier) {
        this.panier = panier;
    }
    
    // Méthode pour obtenir le statut de la commande
    public String getStatut() {
        if ("panier".equals(panier)) {
            return "En cours";
        } else if ("validee".equals(panier)) {
            return "Validée";
        } else {
            return panier; // Retourne la valeur brute si ce n'est ni "panier" ni "validee"
        }
    }

    public List<LigneCommande> getLignesCommande() {
        return lignesCommande;
    }

    public void setLignesCommande(List<LigneCommande> lignesCommande) {
        this.lignesCommande = lignesCommande;
    }

    // Méthodes pour gérer les lignes de commande
    public void ajouterLigneCommande(LigneCommande ligneCommande) {
        this.lignesCommande.add(ligneCommande);
        // Mettre à jour le total
        this.total += ligneCommande.getPrixApplique() * ligneCommande.getQuantite();
    }

    public void supprimerLigneCommande(LigneCommande ligneCommande) {
        if (this.lignesCommande.remove(ligneCommande)) {
            // Mettre à jour le total
            this.total -= ligneCommande.getPrixApplique() * ligneCommande.getQuantite();
        }
    }

    // Vérifier si c'est un panier en cours
    public boolean estPanier() {
        return "panier".equals(this.panier);
    }

    // Calculer le total de la commande
    public void calculerTotal() {
        this.total = 0;
        for (LigneCommande ligne : this.lignesCommande) {
            this.total += ligne.getPrixApplique() * ligne.getQuantite();
        }
    }
}
