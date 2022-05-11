package com.yummygout.gui.front;

import com.codename1.components.ImageViewer;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import com.yummygout.gui.Login;

public class AccueilFront extends Form {

    public static Form accueilFront;
    Resources theme = UIManager.initFirstTheme("/theme");
    Label label;

    public AccueilFront() {
        super(new BorderLayout());
        accueilFront = this;
        addGUIs();
    }

    private void addGUIs() {
        Tabs tabs = new Tabs();

        tabs.addTab("Blogs", FontImage.MATERIAL_MESSAGE, 5,
                new com.yummygout.gui.front.blog.ShowAll()
        );
        tabs.addTab("Plats", FontImage.MATERIAL_FASTFOOD, 5,
                new com.yummygout.gui.front.plat.ShowAll()
        );
        tabs.addTab("Plus", FontImage.MATERIAL_MENU, 5, moreGUI());

        this.add(BorderLayout.CENTER, tabs);
    }

    private Container moreGUI() {

        ImageViewer userImage = new ImageViewer(theme.getImage("default.jpg").fill(200, 200));
        userImage.setUIID("candidatImage");
        label = new Label("Client");
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
                makeStocksButton(),
                makeFournisseursButton(),
                makeCategoriesButton(),
                makereclamationButton()
        );

        return menuContainer;
    }

    private Button makeStocksButton() {
        Button button = new Button("Stocks");
        button.setUIID("buttonMenu");
        button.setMaterialIcon(FontImage.MATERIAL_HOUSE);
        button.addActionListener(action -> new com.yummygout.gui.front.stock.ShowAll(this).show());
        return button;
    }

    private Button makeFournisseursButton() {
        Button button = new Button("Fournisseurs");
        button.setUIID("buttonMenu");
        button.setMaterialIcon(FontImage.MATERIAL_CAR_RENTAL);
        button.addActionListener(action -> new com.yummygout.gui.front.fournisseur.ShowAll(this).show());
        return button;
    }

    private Button makeCategoriesButton() {
        Button button = new Button("Categories");
        button.setUIID("buttonMenu");
        button.setMaterialIcon(FontImage.MATERIAL_CATEGORY);
        button.addActionListener(action -> new com.yummygout.gui.front.categorie.ShowAll(this).show());
        return button;
    }
    
     private Button makereclamationButton() {
        Button button = new Button("reclamation");
        button.setUIID("buttonMenu");
        button.setMaterialIcon(FontImage.MATERIAL_CATEGORY);
        button.addActionListener(action -> new com.mycompany.gui.AjoutReclamationForm(theme).show());
        return button;
    }

}