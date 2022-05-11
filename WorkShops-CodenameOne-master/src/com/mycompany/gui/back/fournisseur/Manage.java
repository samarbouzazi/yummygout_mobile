package com.yummygout.gui.back.fournisseur;


import com.codename1.ui.*;
import com.codename1.ui.layouts.BoxLayout;
import com.yummygout.entities.Fournisseur;
import com.yummygout.services.FournisseurService;
import com.yummygout.utils.AlertUtils;

public class Manage extends Form {


    Fournisseur currentFournisseur;

    Label nomLabel, prenomLabel, categorieLabel, telLabel, addLabel;
    TextField nomTF, prenomTF, categorieTF, telTF, addTF;


    Button manageButton;

    Form previous;

    public Manage(Form previous) {
        super(ShowAll.currentFournisseur == null ? "Ajouter" : "Modifier", new BoxLayout(BoxLayout.Y_AXIS));
        this.previous = previous;

        currentFournisseur = ShowAll.currentFournisseur;

        addGUIs();
        addActions();

        getToolbar().addMaterialCommandToLeftBar("  ", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
    }

    private void addGUIs() {


        nomLabel = new Label("Nom : ");
        nomLabel.setUIID("labelDefault");
        nomTF = new TextField();
        nomTF.setHint("Tapez le nom");


        prenomLabel = new Label("Prenom : ");
        prenomLabel.setUIID("labelDefault");
        prenomTF = new TextField();
        prenomTF.setHint("Tapez le prenom");


        categorieLabel = new Label("Categorie : ");
        categorieLabel.setUIID("labelDefault");
        categorieTF = new TextField();
        categorieTF.setHint("Tapez le categorie");


        telLabel = new Label("Tel : ");
        telLabel.setUIID("labelDefault");
        telTF = new TextField();
        telTF.setHint("Tapez le tel");


        addLabel = new Label("Add : ");
        addLabel.setUIID("labelDefault");
        addTF = new TextField();
        addTF.setHint("Tapez le add");


        if (currentFournisseur == null) {


            manageButton = new Button("Ajouter");
        } else {
            nomTF.setText(currentFournisseur.getNom());
            prenomTF.setText(currentFournisseur.getPrenom());
            categorieTF.setText(currentFournisseur.getCategorie());
            telTF.setText(String.valueOf(currentFournisseur.getTel()));
            addTF.setText(currentFournisseur.getAdd());


            manageButton = new Button("Modifier");
        }
        manageButton.setUIID("buttonWhiteCenter");

        Container container = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        container.setUIID("containerRounded");

        container.addAll(

                nomLabel, nomTF,
                prenomLabel, prenomTF,
                categorieLabel, categorieTF,
                telLabel, telTF,
                addLabel, addTF,

                manageButton
        );

        this.addAll(container);
    }

    private void addActions() {

        if (currentFournisseur == null) {
            manageButton.addActionListener(action -> {
                if (controleDeSaisie()) {
                    int responseCode = FournisseurService.getInstance().add(
                            new Fournisseur(
                                    nomTF.getText(),
                                    prenomTF.getText(),
                                    categorieTF.getText(),
                                    (int) Float.parseFloat(telTF.getText()),
                                    addTF.getText()
                            )
                    );
                    if (responseCode == 200) {
                        AlertUtils.makeNotification("Fournisseur ajouté avec succes");
                        showBackAndRefresh();
                    } else {
                        Dialog.show("Erreur", "Erreur d'ajout de fournisseur. Code d'erreur : " + responseCode, new Command("Ok"));
                    }
                }
            });
        } else {
            manageButton.addActionListener(action -> {
                if (controleDeSaisie()) {
                    int responseCode = FournisseurService.getInstance().edit(
                            new Fournisseur(
                                    currentFournisseur.getId(),


                                    nomTF.getText(),
                                    prenomTF.getText(),
                                    categorieTF.getText(),
                                    (int) Float.parseFloat(telTF.getText()),
                                    addTF.getText()

                            )
                    );
                    if (responseCode == 200) {
                        AlertUtils.makeNotification("Fournisseur modifié avec succes");
                        showBackAndRefresh();
                    } else {
                        Dialog.show("Erreur", "Erreur de modification de fournisseur. Code d'erreur : " + responseCode, new Command("Ok"));
                    }
                }
            });
        }
    }

    private void showBackAndRefresh() {
        ((ShowAll) previous).refresh();
        previous.showBack();
    }

    private boolean controleDeSaisie() {


        if (nomTF.getText().equals("")) {
            Dialog.show("Avertissement", "Nom vide", new Command("Ok"));
            return false;
        }


        if (prenomTF.getText().equals("")) {
            Dialog.show("Avertissement", "Prenom vide", new Command("Ok"));
            return false;
        }


        if (categorieTF.getText().equals("")) {
            Dialog.show("Avertissement", "Categorie vide", new Command("Ok"));
            return false;
        }


        if (telTF.getText().equals("")) {
            Dialog.show("Avertissement", "Tel vide", new Command("Ok"));
            return false;
        }
        try {
            Float.parseFloat(telTF.getText());
        } catch (NumberFormatException e) {
            Dialog.show("Avertissement", telTF.getText() + " n'est pas un nombre valide (tel)", new Command("Ok"));
            return false;
        }


        if (addTF.getText().equals("")) {
            Dialog.show("Avertissement", "Add vide", new Command("Ok"));
            return false;
        }


        return true;
    }
}