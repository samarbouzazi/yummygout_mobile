package com.yummygout.entities;

import com.yummygout.utils.Statics;

public class Plat implements Comparable<Plat> {

    private int id;
    private String image;
    private Categorie categorie;
    private String nom;
    private String description;
    private int prix;
    private int quantite;
    private int stock;

    public Plat() {
    }

    public Plat(int id, String image, Categorie categorie, String nom, String description, int prix, int quantite, int stock) {
        this.id = id;
        this.image = image;
        this.categorie = categorie;
        this.nom = nom;
        this.description = description;
        this.prix = prix;
        this.quantite = quantite;
        this.stock = stock;
    }

    public Plat(String image, Categorie categorie, String nom, String description, int prix, int quantite, int stock) {
        this.image = image;
        this.categorie = categorie;
        this.nom = nom;
        this.description = description;
        this.prix = prix;
        this.quantite = quantite;
        this.stock = stock;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Categorie getCategorie() {
        return categorie;
    }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrix() {
        return prix;
    }

    public void setPrix(int prix) {
        this.prix = prix;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }


    @Override
    public String toString() {
        return "Plat : " +
                "id=" + id
                + ", Image=" + image
                + ", Categorie=" + categorie
                + ", Nom=" + nom
                + ", Description=" + description
                + ", Prix=" + prix
                + ", Quantite=" + quantite
                + ", Stock=" + stock
                ;
    }

    @Override
    public int compareTo(Plat plat) {
        switch (Statics.compareVar) {
            case "Image":
                return this.getImage().compareTo(plat.getImage());
            case "Categorie":
                return this.getCategorie().getNom().compareTo(plat.getCategorie().getNom());
            case "Nom":
                return this.getNom().compareTo(plat.getNom());
            case "Description":
                return this.getDescription().compareTo(plat.getDescription());
            case "Prix":
                return Integer.compare(this.getPrix(), plat.getPrix());
            case "Quantite":
                return Integer.compare(this.getQuantite(), plat.getQuantite());
            case "Stock":
                return Integer.compare(this.getStock(), plat.getStock());

            default:
                return 0;
        }
    }

}