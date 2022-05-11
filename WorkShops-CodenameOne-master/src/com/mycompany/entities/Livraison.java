/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yummygout.entities;

import java.util.Date;

/**
 *
 * @author LENOVO
 */
public class Livraison {
    private int idLivraison;
    private Integer reflivraison;
    private Date date;
    private String etat;
    private String region;
    private String rueliv;
    private int idlivreur;
    private String client;

    public Livraison(Integer reflivraison, String region, String rueliv) {
        this.reflivraison = reflivraison;
        this.region = region;
        this.rueliv = rueliv;
    }
    public int getIdLivraison() {
        return idLivraison;
    }

    public void setIdLivraison(int idLivraison) {
        this.idLivraison = idLivraison;
    }

    public int getReflivraison() {
        return reflivraison;
    }

    public Livraison() {
    }

    public Livraison(int idLivraison, int reflivraison, Date date, String etat, String region, String rueliv, int idlivreur, String client) {
        this.idLivraison = idLivraison;
        this.reflivraison = reflivraison;
        this.date = date;
        this.etat = etat;
        this.region = region;
        this.rueliv = rueliv;
        this.idlivreur = idlivreur;
        this.client = client;
    }

    @Override
    public String toString() {
        return "Livraison{" + "idLivraison=" + idLivraison + ", reflivraison=" + reflivraison + ", date=" + date + ", etat=" + etat + ", region=" + region + ", rueliv=" + rueliv + ", idlivreur=" + idlivreur + ", client=" + client + '}';
    }

   
    public void setReflivraison(int reflivraison) {
        this.reflivraison = reflivraison;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getRueliv() {
        return rueliv;
    }

    public void setRueliv(String rueliv) {
        this.rueliv = rueliv;
    }

    public int getIdlivreur() {
        return idlivreur;
    }

    public void setIdlivreur(int idlivreur) {
        this.idlivreur = idlivreur;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

   
    
    
}
