package com.yummygout.gui.front.plat;

import com.codename1.components.ImageViewer;
import com.codename1.components.ShareButton;
import com.codename1.io.FileSystemStorage;
import com.codename1.io.Log;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.ImageIO;
import com.codename1.ui.util.Resources;
import com.yummygout.entities.Plat;
import com.yummygout.gui.front.AccueilFront;
import com.yummygout.services.PlatService;
import com.yummygout.utils.Statics;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;

public class ShowAll extends Form {

    public static Plat currentPlat = null;
    Form previous;
    Resources theme = UIManager.initFirstTheme("/theme");
    PickerComponent sortPicker;
    ArrayList<Component> componentModels;
    Label imageLabel, categorieLabel, nomLabel, descriptionLabel, prixLabel, quantiteLabel, stockLabel;
    ImageViewer imageIV;
    Container btnsContainer;

    public ShowAll() {
        super("Plats", new BoxLayout(BoxLayout.Y_AXIS));

        addGUIs();
        addActions();
    }

    public void refresh() {
        this.removeAll();
        addGUIs();
        addActions();
        this.refreshTheme();
    }

    private void addGUIs() {


        ArrayList<Plat> listPlats = PlatService.getInstance().getAll();

        componentModels = new ArrayList<>();

        sortPicker = PickerComponent.createStrings("Image", "Categorie", "Nom", "Description", "Prix", "Quantite", "Stock").label("Trier par");
        sortPicker.getPicker().setSelectedString("");
        sortPicker.getPicker().addActionListener((l) -> {
            if (componentModels.size() > 0) {
                for (Component componentModel : componentModels) {
                    this.removeComponent(componentModel);
                }
            }
            componentModels = new ArrayList<>();
            Statics.compareVar = sortPicker.getPicker().getSelectedString();
            Collections.sort(listPlats);
            for (Plat plat : listPlats) {
                Component model = makePlatModel(plat);
                this.add(model);
                componentModels.add(model);
            }
            this.revalidate();
        });
        this.add(sortPicker);

        if (listPlats.size() > 0) {
            for (Plat plat : listPlats) {
                Component model = makePlatModel(plat);
                this.add(model);
                componentModels.add(model);
            }
        } else {
            this.add(new Label("Aucune donnée"));
        }
    }

    private void addActions() {

    }

    private Container makeModelWithoutButtons(Plat plat) {
        Container platModel = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        platModel.setUIID("containerRounded");


        imageLabel = new Label("Image : " + plat.getImage());
        imageLabel.setUIID("labelDefault");

        categorieLabel = new Label("Categorie : " + plat.getCategorie());
        categorieLabel.setUIID("labelDefault");

        nomLabel = new Label("Nom : " + plat.getNom());
        nomLabel.setUIID("labelDefault");

        descriptionLabel = new Label("Description : " + plat.getDescription());
        descriptionLabel.setUIID("labelDefault");

        prixLabel = new Label("Prix : " + plat.getPrix());
        prixLabel.setUIID("labelDefault");

        quantiteLabel = new Label("Quantite : " + plat.getQuantite());
        quantiteLabel.setUIID("labelDefault");

        stockLabel = new Label("Stock : " + (plat.getStock() == 1 ? "True" : "False"));
        stockLabel.setUIID("labelDefault");

        categorieLabel = new Label("Categorie : " + plat.getCategorie().getNom());
        categorieLabel.setUIID("labelDefault");

        if (plat.getImage() != null) {
            String url = Statics.PLAT_IMAGE_URL + plat.getImage();
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

        platModel.addAll(
                imageIV,
                categorieLabel, nomLabel, descriptionLabel, prixLabel, quantiteLabel, stockLabel
        );

        return platModel;
    }

    private Component makePlatModel(Plat plat) {

        Container platModel = makeModelWithoutButtons(plat);

        btnsContainer = new Container(new BorderLayout());
        btnsContainer.setUIID("containerButtons");


        Button btnAfficherScreenshot = new Button("Partager");
        btnAfficherScreenshot.addActionListener(listener -> share(plat));

        btnsContainer.add(BorderLayout.CENTER, btnAfficherScreenshot);

        platModel.add(btnsContainer);

        return platModel;
    }

    private void share(Plat plat) {
        Form form = new Form(new BoxLayout(BoxLayout.Y_AXIS));
        form.add(makeModelWithoutButtons(plat));
        String imageFile = FileSystemStorage.getInstance().getAppHomePath() + "screenshot.png";
        Image screenshot = Image.createImage(
                com.codename1.ui.Display.getInstance().getDisplayWidth(),
                com.codename1.ui.Display.getInstance().getDisplayHeight()
        );
        form.revalidate();
        form.setVisible(true);
        form.paintComponent(screenshot.getGraphics(), true);
        form.removeAll();
        try (OutputStream os = FileSystemStorage.getInstance().openOutputStream(imageFile)) {
            ImageIO.getImageIO().save(screenshot, os, ImageIO.FORMAT_PNG, 1);
        } catch (IOException err) {
            Log.e(err);
        }
        Form screenShotForm = new Form("Partager plat", new BoxLayout(BoxLayout.Y_AXIS));
        ImageViewer screenshotViewer = new ImageViewer(screenshot.fill(1000, 2000));
        screenshotViewer.setFocusable(false);
        screenshotViewer.setUIID("screenshot");
        ShareButton btnPartager = new ShareButton();
        btnPartager.setText("Partager ");
        btnPartager.setTextPosition(LEFT);
        btnPartager.setImageToShare(imageFile, "image/png");
        btnPartager.setTextToShare(plat.toString());
        screenShotForm.addAll(screenshotViewer, btnPartager);
        screenShotForm.getToolbar().addMaterialCommandToLeftBar("  ", FontImage.MATERIAL_ARROW_BACK, e -> AccueilFront.accueilFront.showBack());
        screenShotForm.show();
    }

}