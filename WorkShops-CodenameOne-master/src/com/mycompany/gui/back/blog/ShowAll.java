package com.yummygout.gui.back.blog;

import com.codename1.components.InteractionDialog;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.yummygout.entities.Blog;
import com.yummygout.services.BlogService;

import java.util.ArrayList;

public class ShowAll extends Form {

    public static Blog currentBlog = null;
    Form previous;
    Button addBtn;
    Label  titreLabel, descriptionLabel;
    Button editBtn, deleteBtn;
    Container btnsContainer;

    public ShowAll(Form previous) {
        super("Blogs", new BoxLayout(BoxLayout.Y_AXIS));
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


        ArrayList<Blog> listBlogs = BlogService.getInstance().getAll();


        if (listBlogs.size() > 0) {
            for (Blog blog : listBlogs) {
                Component model = makeBlogModel(blog);
                this.add(model);
            }
        } else {
            this.add(new Label("Aucune donnée"));
        }
    }

    private void addActions() {
        addBtn.addActionListener(action -> {
            currentBlog = null;
            new Manage(this).show();
        });

    }

    private Container makeModelWithoutButtons(Blog blog) {
        Container blogModel = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        blogModel.setUIID("containerRounded");



        titreLabel = new Label("Titre : " + blog.getTitre());
        titreLabel.setUIID("labelDefault");

        descriptionLabel = new Label("Description : " + blog.getDescription());
        descriptionLabel.setUIID("labelDefault");


        blogModel.addAll(
 titreLabel, descriptionLabel
        );

        return blogModel;
    }

    private Component makeBlogModel(Blog blog) {

        Container blogModel = makeModelWithoutButtons(blog);

        btnsContainer = new Container(new BorderLayout());
        btnsContainer.setUIID("containerButtons");

        editBtn = new Button("Modifier");
        editBtn.setUIID("buttonWhiteCenter");
        editBtn.addActionListener(action -> {
            currentBlog = blog;
            new Manage(this).show();
        });

        deleteBtn = new Button("Supprimer");
        deleteBtn.setUIID("buttonWhiteCenter");
        deleteBtn.addActionListener(action -> {
            InteractionDialog dlg = new InteractionDialog("Confirmer la suppression");
            dlg.setLayout(new BorderLayout());
            dlg.add(BorderLayout.CENTER, new Label("Voulez vous vraiment supprimer ce blog ?"));
            Button btnClose = new Button("Annuler");
            btnClose.addActionListener((ee) -> dlg.dispose());
            Button btnConfirm = new Button("Confirmer");
            btnConfirm.addActionListener(actionConf -> {
                int responseCode = BlogService.getInstance().delete(blog.getId());

                if (responseCode == 200) {
                    currentBlog = null;
                    dlg.dispose();
                    blogModel.remove();
                    this.refreshTheme();
                } else {
                    Dialog.show("Erreur", "Erreur de suppression du blog. Code d'erreur : " + responseCode, new Command("Ok"));
                }
            });
            Container btnContainer = new Container(new BoxLayout(BoxLayout.X_AXIS));
            btnContainer.addAll(btnClose, btnConfirm);
            dlg.addComponent(BorderLayout.SOUTH, btnContainer);
            dlg.show(800, 800, 0, 0);
        });

        btnsContainer.add(BorderLayout.WEST, editBtn);
        btnsContainer.add(BorderLayout.EAST, deleteBtn);


        blogModel.add(btnsContainer);

        return blogModel;
    }

}