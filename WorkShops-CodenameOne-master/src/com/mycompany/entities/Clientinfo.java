package com.yummygout.entities;

public class Clientinfo {

    private int id;
    private String nom;

    public Clientinfo() {
    }

    public Clientinfo(int id, String nom) {
        this.id = id;
        this.nom = nom;
    }

    public Clientinfo(String nom) {
        this.nom = nom;
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

    @Override
    public String toString() {
        return nom;
    }
}