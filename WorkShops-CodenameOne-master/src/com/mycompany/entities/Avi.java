package com.yummygout.entities;

import java.util.Date;

public class Avi {

    private int id;
    private Blog blog;
    private Clientinfo clientinfo;
    private Date dateavis;
    private int likee;
    private int deslike;
    private String description;

    public Avi() {
    }

    public Avi(int id, Blog blog, Clientinfo clientinfo, Date dateavis, int likee, int deslike, String description) {
        this.id = id;
        this.blog = blog;
        this.clientinfo = clientinfo;
        this.dateavis = dateavis;
        this.likee = likee;
        this.deslike = deslike;
        this.description = description;
    }

    public Avi(Blog blog, Clientinfo clientinfo, Date dateavis, int likee, int deslike, String description) {
        this.blog = blog;
        this.clientinfo = clientinfo;
        this.dateavis = dateavis;
        this.likee = likee;
        this.deslike = deslike;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Blog getBlog() {
        return blog;
    }

    public void setBlog(Blog blog) {
        this.blog = blog;
    }

    public Clientinfo getClientinfo() {
        return clientinfo;
    }

    public void setClientinfo(Clientinfo clientinfo) {
        this.clientinfo = clientinfo;
    }

    public Date getDateavis() {
        return dateavis;
    }

    public void setDateavis(Date dateavis) {
        this.dateavis = dateavis;
    }

    public int getLikee() {
        return likee;
    }

    public void setLikee(int likee) {
        this.likee = likee;
    }

    public int getDeslike() {
        return deslike;
    }

    public void setDeslike(int deslike) {
        this.deslike = deslike;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}