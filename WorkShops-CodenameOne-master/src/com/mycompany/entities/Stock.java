package com.yummygout.entities;

import java.util.Date;

public class Stock {

    private int id;
    private Fournisseur fournisseur;
    private String nom;
    private Date dateAjout;
    private Date dateFin;
    private float prix;
    private int quantite;

    public Stock() {
    }

    public Stock(int id, Fournisseur fournisseur, String nom, Date dateAjout, Date dateFin, float prix, int quantite) {
        this.id = id;
        this.fournisseur = fournisseur;
        this.nom = nom;
        this.dateAjout = dateAjout;
        this.dateFin = dateFin;
        this.prix = prix;
        this.quantite = quantite;
    }

    public Stock(Fournisseur fournisseur, String nom, Date dateAjout, Date dateFin, float prix, int quantite) {
        this.fournisseur = fournisseur;
        this.nom = nom;
        this.dateAjout = dateAjout;
        this.dateFin = dateFin;
        this.prix = prix;
        this.quantite = quantite;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Fournisseur getFournisseur() {
        return fournisseur;
    }

    public void setFournisseur(Fournisseur fournisseur) {
        this.fournisseur = fournisseur;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Date getDateAjout() {
        return dateAjout;
    }

    public void setDateAjout(Date dateAjout) {
        this.dateAjout = dateAjout;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public float getPrix() {
        return prix;
    }

    public void setPrix(float prix) {
        this.prix = prix;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }


}