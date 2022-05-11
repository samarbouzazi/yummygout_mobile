package com.yummygout.entities;

public class Categorie {

    private int id;
    private String image;
    private String nom;

    public Categorie() {
    }

    public Categorie(int id, String image, String nom) {
        this.id = id;
        this.image = image;
        this.nom = nom;
    }

    public Categorie(String image, String nom) {
        this.image = image;
        this.nom = nom;
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

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }


}