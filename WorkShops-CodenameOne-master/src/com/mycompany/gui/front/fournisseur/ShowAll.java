package com.yummygout.gui.front.fournisseur;

import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.yummygout.entities.Fournisseur;
import com.yummygout.services.FournisseurService;

import java.util.ArrayList;

public class ShowAll extends Form {

    public static Fournisseur currentFournisseur = null;
    Form previous;
    Label nomLabel, prenomLabel, categorieLabel, telLabel, addLabel;
    Container btnsContainer;

    public ShowAll(Form previous) {
        super("Fournisseurs", new BoxLayout(BoxLayout.Y_AXIS));
        this.previous = previous;

        addGUIs();
        addActions();

        super.getToolbar().addMaterialCommandToLeftBar("  ", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
    }

    public void refresh() {
        this.removeAll();
        addGUIs();
        addActions();
        this.refreshTheme();
    }

    private void addGUIs() {


        ArrayList<Fournisseur> listFournisseurs = FournisseurService.getInstance().getAll();


        if (listFournisseurs.size() > 0) {
            for (Fournisseur fournisseur : listFournisseurs) {
                Component model = makeFournisseurModel(fournisseur);
                this.add(model);
            }
        } else {
            this.add(new Label("Aucune donnée"));
        }
    }

    private void addActions() {

    }

    private Container makeModelWithoutButtons(Fournisseur fournisseur) {
        Container fournisseurModel = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        fournisseurModel.setUIID("containerRounded");


        nomLabel = new Label("Nom : " + fournisseur.getNom());
        nomLabel.setUIID("labelDefault");

        prenomLabel = new Label("Prenom : " + fournisseur.getPrenom());
        prenomLabel.setUIID("labelDefault");

        categorieLabel = new Label("Categorie : " + fournisseur.getCategorie());
        categorieLabel.setUIID("labelDefault");

        telLabel = new Label("Tel : " + fournisseur.getTel());
        telLabel.setUIID("labelDefault");

        addLabel = new Label("Add : " + fournisseur.getAdd());
        addLabel.setUIID("labelDefault");


        fournisseurModel.addAll(

                nomLabel, prenomLabel, categorieLabel, telLabel, addLabel
        );

        return fournisseurModel;
    }

    private Component makeFournisseurModel(Fournisseur fournisseur) {

        Container fournisseurModel = makeModelWithoutButtons(fournisseur);

        btnsContainer = new Container(new BorderLayout());
        btnsContainer.setUIID("containerButtons");


        fournisseurModel.add(btnsContainer);

        return fournisseurModel;
    }

}