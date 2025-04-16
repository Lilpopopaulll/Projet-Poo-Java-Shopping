import java.util.*;

// Entités de base
class Client {
    int idClient; // id client 
    String nom, prenom, email, motDePasse; // information a prendre en compte 
    String type; // "nouveau client (offre d'arrivé ou client confirmé
    List<Commande> commandes = new ArrayList<>();
}

class Article {
    int idArticle; // id article
    String nom, description, categorie; // information a prendre en compte 
    double prixUnitaire, prixVrac; //prix qui depend de la quatité ( 10 c'eest enormes pour un magasin de vetements donc il faut qu'on voit si on garde ce feautre )
    int quantiteVrac, stock; // quantité de l'article pour un effet sold out 
    Marque marque; // marque c'est uniquement si on est un revendeur si on a notre propre marque de vetements dans ce cas la ca on peut le supproimer 
    List<Promotion> promotions = new ArrayList<>();
}

class Commande {
    int idCommande;  // id de la commmande 
    Date dateCommande; // date de la commande pour les gerer par oredre chronologique 
    double total;
    Client client; // id du client 
    List<LigneCommande> lignes = new ArrayList<>();
}

class LigneCommande {
    Article article;
    int quantite; // prix qui depned de l quantité a prevoir ( avec remiseappliqué)
    double prixApplique;
    double remiseApplique;
}

class Promotion {
    int idPromotion; 
    int seuilQuantite;
    double prixVrac;
    Article article;
}

class Marque {
    int idMarque; // peut etre inutile si l'idee c'est d'avoir notre propre marque de vetements 
    String nomMarque;
    List<Article> articles = new ArrayList<>();
}

class Admin {
    int idAdmin;
    String email, motDePasse;
    List<Article> articlesGeres = new ArrayList<>();
    List<Promotion> promotionsGerees = new ArrayList<>();
}
