package com.yummygout.gui.back.fournisseur;

import com.codename1.components.InteractionDialog;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.yummygout.entities.Fournisseur;
import com.yummygout.services.FournisseurService;

import java.util.ArrayList;

public class ShowAll extends Form {

    public static Fournisseur currentFournisseur = null;
    Form previous;
    Button addBtn;
    Label nomLabel, prenomLabel, categorieLabel, telLabel, addLabel;
    Button editBtn, deleteBtn;
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
        addBtn = new Button("Ajouter");
        addBtn.setUIID("buttonWhiteCenter");
        this.add(addBtn);


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
        addBtn.addActionListener(action -> {
            currentFournisseur = null;
            new Manage(this).show();
        });

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

        editBtn = new Button("Modifier");
        editBtn.setUIID("buttonWhiteCenter");
        editBtn.addActionListener(action -> {
            currentFournisseur = fournisseur;
            new Manage(this).show();
        });

        deleteBtn = new Button("Supprimer");
        deleteBtn.setUIID("buttonWhiteCenter");
        deleteBtn.addActionListener(action -> {
            InteractionDialog dlg = new InteractionDialog("Confirmer la suppression");
            dlg.setLayout(new BorderLayout());
            dlg.add(BorderLayout.CENTER, new Label("Voulez vous vraiment supprimer ce fournisseur ?"));
            Button btnClose = new Button("Annuler");
            btnClose.addActionListener((ee) -> dlg.dispose());
            Button btnConfirm = new Button("Confirmer");
            btnConfirm.addActionListener(actionConf -> {
                int responseCode = FournisseurService.getInstance().delete(fournisseur.getId());

                if (responseCode == 200) {
                    currentFournisseur = null;
                    dlg.dispose();
                    fournisseurModel.remove();
                    this.refreshTheme();
                } else {
                    Dialog.show("Erreur", "Erreur de suppression du fournisseur. Code d'erreur : " + responseCode, new Command("Ok"));
                }
            });
            Container btnContainer = new Container(new BoxLayout(BoxLayout.X_AXIS));
            btnContainer.addAll(btnClose, btnConfirm);
            dlg.addComponent(BorderLayout.SOUTH, btnContainer);
            dlg.show(800, 800, 0, 0);
        });

        btnsContainer.add(BorderLayout.WEST, editBtn);
        btnsContainer.add(BorderLayout.EAST, deleteBtn);


        fournisseurModel.add(btnsContainer);

        return fournisseurModel;
    }

}