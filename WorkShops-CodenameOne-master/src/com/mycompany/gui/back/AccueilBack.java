package com.yummygout.gui.back;

import com.codename1.components.ImageViewer;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import com.yummygout.gui.Login;

public class AccueilBack extends Form {

    Resources theme = UIManager.initFirstTheme("/theme");
    Label label;

    public AccueilBack() {
        super("Menu", new BoxLayout(BoxLayout.Y_AXIS));
        addGUIs();
    }

    private void addGUIs() {
        ImageViewer userImage = new ImageViewer(theme.getImage("default.jpg").fill(200, 200));
        userImage.setUIID("candidatImage");
        label = new Label("Admin");
        label.setUIID("links");
        Button btnDeconnexion = new Button();
        btnDeconnexion.setUIID("buttonLogout");
        btnDeconnexion.setMaterialIcon(FontImage.MATERIAL_ARROW_FORWARD);
        btnDeconnexion.addActionListener(action -> {
            Login.loginForm.showBack();
        });

        Container userContainer = new Container(new BorderLayout());
        userContainer.setUIID("userContainer");
        userContainer.add(BorderLayout.WEST, userImage);
        userContainer.add(BorderLayout.CENTER, label);
        userContainer.add(BorderLayout.EAST, btnDeconnexion);

        Container menuContainer = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        menuContainer.addAll(
                userContainer,
                makenosuserButton(),
                makeStocksButton(),
                makeFournisseursButton(),
                makeBlogsButton(),
                makeAvisButton(),
                makePlatsButton(),
                makeCategoriesButton(),
                makelivraisonButton(),
                 makenoslivraisonButton() 
        );

        this.add(menuContainer);
    }

    private Button makeStocksButton() {
        Button button = new Button("Stocks");
        button.setUIID("buttonMenu");
        button.setMaterialIcon(FontImage.MATERIAL_HOUSE);
        button.addActionListener(action -> new com.yummygout.gui.back.stock.ShowAll(this).show());
        return button;
    }

    private Button makeFournisseursButton() {
        Button button = new Button("Fournisseurs");
        button.setUIID("buttonMenu");
        button.setMaterialIcon(FontImage.MATERIAL_CAR_RENTAL);
        button.addActionListener(action -> new com.yummygout.gui.back.fournisseur.ShowAll(this).show());
        return button;
    }

    private Button makeBlogsButton() {
        Button button = new Button("Blogs");
        button.setUIID("buttonMenu");
        button.setMaterialIcon(FontImage.MATERIAL_BOOK);
        button.addActionListener(action -> new com.yummygout.gui.back.blog.ShowAll(this).show());
        return button;
    }

    private Button makeAvisButton() {
        Button button = new Button("Avis");
        button.setUIID("buttonMenu");
        button.setMaterialIcon(FontImage.MATERIAL_MESSAGE);
        button.addActionListener(action -> new com.yummygout.gui.back.avi.ShowAll(this).show());
        return button;
    }

    private Button makePlatsButton() {
        Button button = new Button("Plats");
        button.setUIID("buttonMenu");
        button.setMaterialIcon(FontImage.MATERIAL_FOOD_BANK);
        button.addActionListener(action -> new com.yummygout.gui.back.plat.ShowAll(this).show());
        return button;
    }

    private Button makeCategoriesButton() {
        Button button = new Button("Categories");
        button.setUIID("buttonMenu");
        button.setMaterialIcon(FontImage.MATERIAL_CATEGORY);
        button.addActionListener(action -> new com.yummygout.gui.back.categorie.ShowAll(this).show());
        return button;
    }
    
    private Button makelivraisonButton() {
        Button button = new Button("livraison");
        button.setUIID("buttonMenu");
        button.setMaterialIcon(FontImage.MATERIAL_CATEGORY);
        button.addActionListener(action -> new com.mycompany.gui.AjoutLivraisonForm(theme).show());
        return button;
    }
     private Button makenoslivraisonButton() {
        Button button = new Button("nos livraisons");
        button.setUIID("buttonMenu");
        button.setMaterialIcon(FontImage.MATERIAL_CATEGORY);
        button.addActionListener(action -> new com.mycompany.gui.ListLivraisonForm(theme).show());
        return button;
    }
     
      private Button makenosuserButton() {
        Button button = new Button("User");
        button.setUIID("buttonMenu");
        button.setMaterialIcon(FontImage.MATERIAL_CATEGORY);
        button.addActionListener(action -> new com.mycompany.gui.SignUpFormbachir(theme).show());
        return button;
    }
}
