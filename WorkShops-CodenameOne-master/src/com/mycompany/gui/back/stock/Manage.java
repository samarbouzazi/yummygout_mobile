package com.yummygout.gui.back.stock;


import com.codename1.ui.*;
import com.codename1.ui.layouts.BoxLayout;
import com.yummygout.entities.Fournisseur;
import com.yummygout.entities.Stock;
import com.yummygout.services.FournisseurService;
import com.yummygout.services.StockService;
import com.yummygout.utils.AlertUtils;

import java.util.ArrayList;

public class Manage extends Form {


    Stock currentStock;

    Label nomLabel, prixLabel, quantiteLabel;
    TextField nomTF, prixTF, quantiteTF;
    PickerComponent dateAjoutTF;
    PickerComponent dateFinTF;

    ArrayList<Fournisseur> listFournisseurs;
    PickerComponent fournisseurPC;
    Fournisseur selectedFournisseur = null;


    Button manageButton;

    Form previous;

    public Manage(Form previous) {
        super(ShowAll.currentStock == null ? "Ajouter" : "Modifier", new BoxLayout(BoxLayout.Y_AXIS));
        this.previous = previous;

        currentStock = ShowAll.currentStock;

        addGUIs();
        addActions();

        getToolbar().addMaterialCommandToLeftBar("  ", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
    }

    private void addGUIs() {

        String[] fournisseurStrings;
        int fournisseurIndex;
        fournisseurPC = PickerComponent.createStrings("").label("Fournisseur");
        listFournisseurs = FournisseurService.getInstance().getAll();
        fournisseurStrings = new String[listFournisseurs.size()];
        fournisseurIndex = 0;
        for (Fournisseur fournisseur : listFournisseurs) {
            fournisseurStrings[fournisseurIndex] = fournisseur.getNom();
            fournisseurIndex++;
        }
        fournisseurPC.getPicker().setStrings(fournisseurStrings);
        fournisseurPC.getPicker().addActionListener(l -> selectedFournisseur = listFournisseurs.get(fournisseurPC.getPicker().getSelectedStringIndex()));


        nomLabel = new Label("Nom : ");
        nomLabel.setUIID("labelDefault");
        nomTF = new TextField();
        nomTF.setHint("Tapez le nom");


        dateAjoutTF = PickerComponent.createDate(null).label("DateAjout");


        dateFinTF = PickerComponent.createDate(null).label("DateFin");


        prixLabel = new Label("Prix : ");
        prixLabel.setUIID("labelDefault");
        prixTF = new TextField();
        prixTF.setHint("Tapez le prix");


        quantiteLabel = new Label("Quantite : ");
        quantiteLabel.setUIID("labelDefault");
        quantiteTF = new TextField();
        quantiteTF.setHint("Tapez le quantite");


        if (currentStock == null) {


            manageButton = new Button("Ajouter");
        } else {

            nomTF.setText(currentStock.getNom());
            dateAjoutTF.getPicker().setDate(currentStock.getDateAjout());
            dateFinTF.getPicker().setDate(currentStock.getDateFin());
            prixTF.setText(String.valueOf(currentStock.getPrix()));
            quantiteTF.setText(String.valueOf(currentStock.getQuantite()));

            fournisseurPC.getPicker().setSelectedString(currentStock.getFournisseur().getNom());
            selectedFournisseur = currentStock.getFournisseur();


            manageButton = new Button("Modifier");
        }
        manageButton.setUIID("buttonWhiteCenter");

        Container container = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        container.setUIID("containerRounded");

        container.addAll(
                nomLabel, nomTF,
                dateAjoutTF,
                dateFinTF,
                prixLabel, prixTF,
                quantiteLabel, quantiteTF,
                fournisseurPC,
                manageButton
        );

        this.addAll(container);
    }

    private void addActions() {

        if (currentStock == null) {
            manageButton.addActionListener(action -> {
                if (controleDeSaisie()) {
                    int responseCode = StockService.getInstance().add(
                            new Stock(
                                    selectedFournisseur,
                                    nomTF.getText(),
                                    dateAjoutTF.getPicker().getDate(),
                                    dateFinTF.getPicker().getDate(),
                                    Float.parseFloat(prixTF.getText()),
                                    (int) Float.parseFloat(quantiteTF.getText())
                            )
                    );
                    if (responseCode == 200) {
                        AlertUtils.makeNotification("Stock ajouté avec succes");
                        showBackAndRefresh();
                    } else {
                        Dialog.show("Erreur", "Erreur d'ajout de stock. Code d'erreur : " + responseCode, new Command("Ok"));
                    }
                }
            });
        } else {
            manageButton.addActionListener(action -> {
                if (controleDeSaisie()) {
                    int responseCode = StockService.getInstance().edit(
                            new Stock(
                                    currentStock.getId(),
                                    selectedFournisseur,
                                    nomTF.getText(),
                                    dateAjoutTF.getPicker().getDate(),
                                    dateFinTF.getPicker().getDate(),
                                    Float.parseFloat(prixTF.getText()),
                                    (int) Float.parseFloat(quantiteTF.getText())

                            )
                    );
                    if (responseCode == 200) {
                        AlertUtils.makeNotification("Stock modifié avec succes");
                        showBackAndRefresh();
                    } else {
                        Dialog.show("Erreur", "Erreur de modification de stock. Code d'erreur : " + responseCode, new Command("Ok"));
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


        if (dateAjoutTF.getPicker().getDate() == null) {
            Dialog.show("Avertissement", "Veuillez saisir la dateAjout", new Command("Ok"));
            return false;
        }


        if (dateFinTF.getPicker().getDate() == null) {
            Dialog.show("Avertissement", "Veuillez saisir la dateFin", new Command("Ok"));
            return false;
        }


        if (prixTF.getText().equals("")) {
            Dialog.show("Avertissement", "Prix vide", new Command("Ok"));
            return false;
        }
        try {
            Float.parseFloat(prixTF.getText());
        } catch (NumberFormatException e) {
            Dialog.show("Avertissement", prixTF.getText() + " n'est pas un nombre valide (prix)", new Command("Ok"));
            return false;
        }


        if (quantiteTF.getText().equals("")) {
            Dialog.show("Avertissement", "Quantite vide", new Command("Ok"));
            return false;
        }
        try {
            Float.parseFloat(quantiteTF.getText());
        } catch (NumberFormatException e) {
            Dialog.show("Avertissement", quantiteTF.getText() + " n'est pas un nombre valide (quantite)", new Command("Ok"));
            return false;
        }


        if (selectedFournisseur == null) {
            Dialog.show("Avertissement", "Veuillez choisir un fournisseur", new Command("Ok"));
            return false;
        }


        return true;
    }
}