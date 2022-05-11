package com.yummygout.entities;

public class Blog {

    private int id;
    private int image;
    private String titre;
    private String description;

    public Blog() {
    }

    public Blog(int id, int image, String titre, String description) {
        this.id = id;
        this.image = image;
        this.titre = titre;
        this.description = description;
    }

    public Blog(int image, String titre, String description) {
        this.image = image;
        this.titre = titre;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}