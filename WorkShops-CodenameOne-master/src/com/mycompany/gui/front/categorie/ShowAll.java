package com.yummygout.gui.front.categorie;

import com.codename1.components.ImageViewer;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import com.yummygout.entities.Categorie;
import com.yummygout.services.CategorieService;
import com.yummygout.utils.Statics;

import java.util.ArrayList;

public class ShowAll extends Form {

    public static Categorie currentCategorie = null;
    Form previous;
    Resources theme = UIManager.initFirstTheme("/theme");
    TextField searchTF;
    ArrayList<Component> componentModels;
    Label imageLabel, nomLabel;
    ImageViewer imageIV;
    Container btnsContainer;

    public ShowAll(Form previous) {
        super("Categories", new BoxLayout(BoxLayout.Y_AXIS));
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


        ArrayList<Categorie> listCategories = CategorieService.getInstance().getAll();
        componentModels = new ArrayList<>();

        searchTF = new TextField("", "Chercher categorie par Nom");
        searchTF.addDataChangedListener((d, t) -> {
            if (componentModels.size() > 0) {
                for (Component componentModel : componentModels) {
                    this.removeComponent(componentModel);
                }
            }
            componentModels = new ArrayList<>();
            for (Categorie categorie : listCategories) {
                if (categorie.getNom().toLowerCase().startsWith(searchTF.getText().toLowerCase())) {
                    Component model = makeCategorieModel(categorie);
                    this.add(model);
                    componentModels.add(model);
                }
            }
            this.revalidate();
        });
        this.add(searchTF);


        if (listCategories.size() > 0) {
            for (Categorie categorie : listCategories) {
                Component model = makeCategorieModel(categorie);
                this.add(model);
                componentModels.add(model);
            }
        } else {
            this.add(new Label("Aucune donnée"));
        }
    }

    private void addActions() {

    }

    private Container makeModelWithoutButtons(Categorie categorie) {
        Container categorieModel = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        categorieModel.setUIID("containerRounded");


        imageLabel = new Label("Image : " + categorie.getImage());
        imageLabel.setUIID("labelDefault");

        nomLabel = new Label("Nom : " + categorie.getNom());
        nomLabel.setUIID("labelDefault");

        if (categorie.getImage() != null) {
            String url = Statics.CATEGORIE_IMAGE_URL + categorie.getImage();
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

        categorieModel.addAll(
                imageIV,
                nomLabel
        );

        return categorieModel;
    }

    private Component makeCategorieModel(Categorie categorie) {

        Container categorieModel = makeModelWithoutButtons(categorie);

        btnsContainer = new Container(new BorderLayout());
        btnsContainer.setUIID("containerButtons");


        categorieModel.add(btnsContainer);

        return categorieModel;
    }

}