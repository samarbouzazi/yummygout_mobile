package com.yummygout.gui.front.blog;

import com.codename1.components.InteractionDialog;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.yummygout.entities.Avi;
import com.yummygout.entities.Blog;
import com.yummygout.gui.front.AccueilFront;
import com.yummygout.services.AviService;
import com.yummygout.services.BlogService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ShowAll extends Form {

    public static Blog currentBlog = null;
    public static Avi currentAvi = null;
    public static Form blogForm = null;
    Button addBtn;
    Label  titreLabel, descriptionLabel;
    Button editBtn, deleteBtn;
    Container btnsContainer;
    Form previous;
    Label blogLabel, clientinfoLabel, dateavisLabel, likeeLabel, deslikeLabel;

    public ShowAll() {
        super("Blogs", new BoxLayout(BoxLayout.Y_AXIS));
        blogForm = this;

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
            new Manage(AccueilFront.accueilFront).show();
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
            new Manage(AccueilFront.accueilFront).show();
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

        Button commentsButton = new Button("Avis");
        commentsButton.addActionListener(l -> {
            ArrayList<Avi> listAvis = AviService.getInstance().getAll();

            Container commentsContainer = new Container(new BoxLayout(BoxLayout.Y_AXIS));
            commentsContainer.setUIID("containerRounded");

            int likeCount = 0;
            if (listAvis.size() > 0) {
                for (Avi avi : listAvis) {
                    if (avi.getBlog().getId() == blog.getId()) {
                        likeCount = avi.getLikee() == 1 ? likeCount + 1 : likeCount;
                        Component model = makeAviModel(avi);
                        commentsContainer.add(model);
                    }
                }
            } else {
                commentsContainer.add(new Label("Aucun commentaire"));
            }

            blogModel.add(new Label("Likes : " + likeCount));
            blogModel.add(commentsContainer);


            addBtn = new Button("Ajouter un avi");
            addBtn.setUIID("buttonWhiteCenter");
            addBtn.addActionListener((la) -> {
                currentAvi = null;
                com.yummygout.gui.front.avi.Manage.selectedBlog = blog;
                new com.yummygout.gui.front.avi.Manage(AccueilFront.accueilFront).show();
            });
            blogModel.add(addBtn);


            this.revalidate();
            this.refreshTheme();
        });

        blogModel.add(commentsButton);

        return blogModel;
    }

    private Component makeAviModel(Avi avi) {

        Container aviModel = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        aviModel.setUIID("containerRounded");

        dateavisLabel = new Label("Dateavis : " + new SimpleDateFormat("dd-MM-yyyy").format(avi.getDateavis()));
        dateavisLabel.setUIID("labelDefault");

        clientinfoLabel = new Label("Client : " + avi.getClientinfo());
        clientinfoLabel.setUIID("labelDefault");

        descriptionLabel = new Label("Description : " + avi.getDescription());
        descriptionLabel.setUIID("labelDefault");

        btnsContainer = new Container(new BorderLayout());
        btnsContainer.setUIID("containerButtons");

        editBtn = new Button("Modifier");
        editBtn.setUIID("buttonWhiteCenter");
        editBtn.addActionListener(action -> {
            currentAvi = avi;
            new com.yummygout.gui.front.avi.Manage(AccueilFront.accueilFront).show();
        });

        deleteBtn = new Button("Supprimer");
        deleteBtn.setUIID("buttonWhiteCenter");
        deleteBtn.addActionListener(action -> {
            InteractionDialog dlg = new InteractionDialog("Confirmer la suppression");
            dlg.setLayout(new BorderLayout());
            dlg.add(BorderLayout.CENTER, new Label("Voulez vous vraiment supprimer ce avi ?"));
            Button btnClose = new Button("Annuler");
            btnClose.addActionListener((ee) -> dlg.dispose());
            Button btnConfirm = new Button("Confirmer");
            btnConfirm.addActionListener(actionConf -> {
                int responseCode = AviService.getInstance().delete(avi.getId());

                if (responseCode == 200) {
                    currentAvi = null;
                    dlg.dispose();
                    aviModel.remove();
                    this.refreshTheme();
                } else {
                    Dialog.show("Erreur", "Erreur de suppression du avi. Code d'erreur : " + responseCode, new Command("Ok"));
                }
            });
            Container btnContainer = new Container(new BoxLayout(BoxLayout.X_AXIS));
            btnContainer.addAll(btnClose, btnConfirm);
            dlg.addComponent(BorderLayout.SOUTH, btnContainer);
            dlg.show(800, 800, 0, 0);
        });

        btnsContainer.add(BorderLayout.WEST, editBtn);
        btnsContainer.add(BorderLayout.EAST, deleteBtn);

        aviModel.addAll(
                dateavisLabel, clientinfoLabel, descriptionLabel,
                btnsContainer
        );

        return aviModel;
    }

}