package com.yummygout.gui.back.plat;

import com.codename1.components.ImageViewer;
import com.codename1.components.InteractionDialog;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import com.yummygout.entities.Plat;
import com.yummygout.services.PlatService;
import com.yummygout.utils.Statics;

import java.util.ArrayList;
import java.util.Collections;

public class ShowAll extends Form {

    public static Plat currentPlat = null;
    Form previous;
    Resources theme = UIManager.initFirstTheme("/theme");
    Button addBtn;


    PickerComponent sortPicker;
    ArrayList<Component> componentModels;
    Label imageLabel, categorieLabel, nomLabel, descriptionLabel, prixLabel, quantiteLabel, stockLabel;
    ImageViewer imageIV;
    Button editBtn, deleteBtn;
    Container btnsContainer;

    public ShowAll(Form previous) {
        super("Plats", new BoxLayout(BoxLayout.Y_AXIS));
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
        addBtn.addActionListener(action -> {
            currentPlat = null;
            new Manage(this).show();
        });

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

        editBtn = new Button("Modifier");
        editBtn.setUIID("buttonWhiteCenter");
        editBtn.addActionListener(action -> {
            currentPlat = plat;
            new Manage(this).show();
        });

        deleteBtn = new Button("Supprimer");
        deleteBtn.setUIID("buttonWhiteCenter");
        deleteBtn.addActionListener(action -> {
            InteractionDialog dlg = new InteractionDialog("Confirmer la suppression");
            dlg.setLayout(new BorderLayout());
            dlg.add(BorderLayout.CENTER, new Label("Voulez vous vraiment supprimer ce plat ?"));
            Button btnClose = new Button("Annuler");
            btnClose.addActionListener((ee) -> dlg.dispose());
            Button btnConfirm = new Button("Confirmer");
            btnConfirm.addActionListener(actionConf -> {
                int responseCode = PlatService.getInstance().delete(plat.getId());

                if (responseCode == 200) {
                    currentPlat = null;
                    dlg.dispose();
                    platModel.remove();
                    this.refreshTheme();
                } else {
                    Dialog.show("Erreur", "Erreur de suppression du plat. Code d'erreur : " + responseCode, new Command("Ok"));
                }
            });
            Container btnContainer = new Container(new BoxLayout(BoxLayout.X_AXIS));
            btnContainer.addAll(btnClose, btnConfirm);
            dlg.addComponent(BorderLayout.SOUTH, btnContainer);
            dlg.show(800, 800, 0, 0);
        });

        btnsContainer.add(BorderLayout.WEST, editBtn);
        btnsContainer.add(BorderLayout.EAST, deleteBtn);


        platModel.add(btnsContainer);

        return platModel;
    }

}