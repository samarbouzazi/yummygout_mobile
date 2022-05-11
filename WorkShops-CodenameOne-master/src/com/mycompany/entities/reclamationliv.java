/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yummygout.entities;

import java.util.Date;

/**
 *
 * @author HP
 */
public class reclamationliv {
    private int  idreclivv ;
    private String reclamation , sujetrec , clientname ;
    private Date createdat , updatedat;
    private int idLivraison ;

    public reclamationliv(int idreclivv, String reclamation, String sujetrec, String clientname, Date createdat, Date updatedat, int idLivraison) {
        this.idreclivv = idreclivv;
        this.reclamation = reclamation;
        this.sujetrec = sujetrec;
        this.clientname = clientname;
        this.createdat = createdat;
        this.updatedat = updatedat;
        this.idLivraison = idLivraison;
    } 

    public reclamationliv(String reclamation, String sujetrec) {
        this.reclamation = reclamation;
        this.sujetrec = sujetrec;
    }

    

    

    public reclamationliv() {
    }

    public reclamationliv(int idreclivv, String reclamation) {
        this.idreclivv = idreclivv;
        this.reclamation = reclamation;
    }

    public int getIdreclivv() {
        return idreclivv;
    }

    public void setIdreclivv(int idreclivv) {
        this.idreclivv = idreclivv;
    }

    public String getReclamation() {
        return reclamation;
    }

    public void setReclamation(String reclamation) {
        this.reclamation = reclamation;
    }

    public String getSujetrec() {
        return sujetrec;
    }

    public void setSujetrec(String sujetrec) {
        this.sujetrec = sujetrec;
    }

    public String getClientname() {
        return clientname;
    }

    public void setClientname(String clientname) {
        this.clientname = clientname;
    }

    public Date getCreatedat() {
        return createdat;
    }

    public void setCreatedat(Date createdat) {
        this.createdat = createdat;
    }

    public Date getUpdatedat() {
        return updatedat;
    }

    public void setUpdatedat(Date updatedat) {
        this.updatedat = updatedat;
    }

    public int getIdLivraison() {
        return idLivraison;
    }

    public void setIdLivraison(int idLivraison) {
        this.idLivraison = idLivraison;
    }

    @Override
    public String toString() {
        return "reclamationliv{" + "idreclivv=" + idreclivv + ", reclamation=" + reclamation + ", sujetrec=" + sujetrec + ", clientname=" + clientname + ", createdat=" + createdat + ", updatedat=" + updatedat + ", idLivraison=" + idLivraison + '}';
    }

    
    
    
}
