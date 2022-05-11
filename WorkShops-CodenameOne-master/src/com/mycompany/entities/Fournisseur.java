package com.yummygout.entities;

public class Fournisseur {

    private int id;
    private String nom;
    private String prenom;
    private String categorie;
    private int tel;
    private String add;

    public Fournisseur() {
    }

    public Fournisseur(int id, String nom, String prenom, String categorie, int tel, String add) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.categorie = categorie;
        this.tel = tel;
        this.add = add;
    }

    public Fournisseur(String nom, String prenom, String categorie, int tel, String add) {
        this.nom = nom;
        this.prenom = prenom;
        this.categorie = categorie;
        this.tel = tel;
        this.add = add;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public int getTel() {
        return tel;
    }

    public void setTel(int tel) {
        this.tel = tel;
    }

    public String getAdd() {
        return add;
    }

    public void setAdd(String add) {
        this.add = add;
    }


}