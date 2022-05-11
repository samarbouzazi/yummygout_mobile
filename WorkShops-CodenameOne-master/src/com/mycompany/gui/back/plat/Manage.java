package com.yummygout.gui.back.plat;


import com.codename1.capture.Capture;
import com.codename1.components.ImageViewer;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import com.yummygout.entities.Categorie;
import com.yummygout.entities.Plat;
import com.yummygout.services.CategorieService;
import com.yummygout.services.PlatService;
import com.yummygout.utils.Statics;

import java.io.IOException;
import java.util.ArrayList;

public class Manage extends Form {


    Resources theme = UIManager.initFirstTheme("/theme");
    String selectedImage;
    boolean imageEdited = false;


    Plat currentPlat;

    Label imageLabel, nomLabel, descriptionLabel, prixLabel, quantiteLabel, stockLabel;
    TextField imageTF, nomTF, descriptionTF, prixTF, quantiteTF;

    CheckBox stockCB;
    ArrayList<Categorie> listCategories;
    PickerComponent categoriePC;
    Categorie selectedCategorie = null;


    ImageViewer imageIV;
    Button selectImageButton;

    Button manageButton;

    Form previous;

    public Manage(Form previous) {
        super(ShowAll.currentPlat == null ? "Ajouter" : "Modifier", new BoxLayout(BoxLayout.Y_AXIS));
        this.previous = previous;

        currentPlat = ShowAll.currentPlat;

        addGUIs();
        addActions();

        getToolbar().addMaterialCommandToLeftBar("  ", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
    }

    private void addGUIs() {

        String[] categorieStrings;
        int categorieIndex;
        categoriePC = PickerComponent.createStrings("").label("Categorie");
        listCategories = CategorieService.getInstance().getAll();
        categorieStrings = new String[listCategories.size()];
        categorieIndex = 0;
        for (Categorie categorie : listCategories) {
            categorieStrings[categorieIndex] = categorie.getNom();
            categorieIndex++;
        }
        categoriePC.getPicker().setStrings(categorieStrings);
        categoriePC.getPicker().addActionListener(l -> selectedCategorie = listCategories.get(categoriePC.getPicker().getSelectedStringIndex()));


        nomLabel = new Label("Nom : ");
        nomLabel.setUIID("labelDefault");
        nomTF = new TextField();
        nomTF.setHint("Tapez le nom");


        descriptionLabel = new Label("Description : ");
        descriptionLabel.setUIID("labelDefault");
        descriptionTF = new TextField();
        descriptionTF.setHint("Tapez le description");


        prixLabel = new Label("Prix : ");
        prixLabel.setUIID("labelDefault");
        prixTF = new TextField();
        prixTF.setHint("Tapez le prix");


        quantiteLabel = new Label("Quantite : ");
        quantiteLabel.setUIID("labelDefault");
        quantiteTF = new TextField();
        quantiteTF.setHint("Tapez le quantite");


        stockCB = new CheckBox("Stock : ");


        imageLabel = new Label("Image : ");
        imageLabel.setUIID("labelDefault");
        selectImageButton = new Button("Ajouter une image");

        if (currentPlat == null) {

            imageIV = new ImageViewer(theme.getImage("default.jpg").fill(1100, 500));


            manageButton = new Button("Ajouter");
        } else {


            nomTF.setText(currentPlat.getNom());
            descriptionTF.setText(currentPlat.getDescription());
            prixTF.setText(String.valueOf(currentPlat.getPrix()));
            quantiteTF.setText(String.valueOf(currentPlat.getQuantite()));
            if (currentPlat.getStock() == 1) {
                stockCB.setSelected(true);
            }

            categoriePC.getPicker().setSelectedString(currentPlat.getCategorie().getNom());
            selectedCategorie = currentPlat.getCategorie();


            if (currentPlat.getImage() != null) {
                selectedImage = currentPlat.getImage();
                String url = Statics.PLAT_IMAGE_URL + currentPlat.getImage();
                Image image = URLImage.createToStorage(
                        EncodedImage.createFromImage(theme.getImage("default.jpg").fill(1100, 500), false),
                        url,
                        url,
                        URLImage.RESIZE_SCALE
                );
                imageIV = new ImageViewer(image);
            } else {
                imageIV = new ImageViewer(theme.getImage("default.jpg").fill(1100, 500));
            }
            imageIV.setFocusable(false);


            manageButton = new Button("Modifier");
        }
        manageButton.setUIID("buttonWhiteCenter");

        Container container = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        container.setUIID("containerRounded");

        container.addAll(
                imageLabel, imageIV,
                selectImageButton,


                nomLabel, nomTF,
                descriptionLabel, descriptionTF,
                prixLabel, prixTF,
                quantiteLabel, quantiteTF,
                stockCB,
                categoriePC,
                manageButton
        );

        this.addAll(container);
    }

    private void addActions() {

        selectImageButton.addActionListener(a -> {
            selectedImage = Capture.capturePhoto(900, -1);
            try {
                imageEdited = true;
                imageIV.setImage(Image.createImage(selectedImage));
            } catch (IOException e) {
                e.printStackTrace();
            }
            selectImageButton.setText("Modifier l'image");
        });

        if (currentPlat == null) {
            manageButton.addActionListener(action -> {
                if (controleDeSaisie()) {
                    int responseCode = PlatService.getInstance().add(
                            new Plat(


                                    selectedImage,
                                    selectedCategorie,
                                    nomTF.getText(),
                                    descriptionTF.getText(),
                                    (int) Float.parseFloat(prixTF.getText()),
                                    (int) Float.parseFloat(quantiteTF.getText()),
                                    stockCB.isSelected() ? 1 : 0
                            )
                    );
                    if (responseCode == 200) {
                        Dialog.show("Succés", "Plat ajouté avec succes", new Command("Ok"));
                        showBackAndRefresh();
                    } else {
                        Dialog.show("Erreur", "Erreur d'ajout de plat. Code d'erreur : " + responseCode, new Command("Ok"));
                    }
                }
            });
        } else {
            manageButton.addActionListener(action -> {
                if (controleDeSaisie()) {
                    int responseCode = PlatService.getInstance().edit(
                            new Plat(
                                    currentPlat.getId(),


                                    selectedImage,
                                    selectedCategorie,
                                    nomTF.getText(),
                                    descriptionTF.getText(),
                                    (int) Float.parseFloat(prixTF.getText()),
                                    (int) Float.parseFloat(quantiteTF.getText()),
                                    stockCB.isSelected() ? 1 : 0

                            ), imageEdited
                    );
                    if (responseCode == 200) {
                        Dialog.show("Succés", "Plat modifié avec succes", new Command("Ok"));
                        showBackAndRefresh();
                    } else {
                        Dialog.show("Erreur", "Erreur de modification de plat. Code d'erreur : " + responseCode, new Command("Ok"));
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


        if (descriptionTF.getText().equals("")) {
            Dialog.show("Avertissement", "Description vide", new Command("Ok"));
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


        if (selectedCategorie == null) {
            Dialog.show("Avertissement", "Veuillez choisir un categorie", new Command("Ok"));
            return false;
        }


        if (selectedImage == null) {
            Dialog.show("Avertissement", "Veuillez choisir une image", new Command("Ok"));
            return false;
        }


        return true;
    }
}