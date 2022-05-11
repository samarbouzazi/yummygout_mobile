package com.yummygout.gui.back.blog;


import com.codename1.ui.*;
import com.codename1.ui.layouts.BoxLayout;
import com.yummygout.entities.Blog;
import com.yummygout.services.BlogService;

public class Manage extends Form {


    Blog currentBlog;
    Label  titreLabel, descriptionLabel;
    TextField  titreTF, descriptionTF;

    Button manageButton;

    Form previous;

    public Manage(Form previous) {
        super(ShowAll.currentBlog == null ? "Ajouter" : "Modifier", new BoxLayout(BoxLayout.Y_AXIS));
        this.previous = previous;

        currentBlog = ShowAll.currentBlog;

        addGUIs();
        addActions();

        getToolbar().addMaterialCommandToLeftBar("  ", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
    }

    private void addGUIs() {



        titreLabel = new Label("Titre : ");
        titreLabel.setUIID("labelDefault");
        titreTF = new TextField();
        titreTF.setHint("Tapez le titre");


        descriptionLabel = new Label("Description : ");
        descriptionLabel.setUIID("labelDefault");
        descriptionTF = new TextField();
        descriptionTF.setHint("Tapez le description");


        if (currentBlog == null) {


            manageButton = new Button("Ajouter");
        } else {
            titreTF.setText(currentBlog.getTitre());
            descriptionTF.setText(currentBlog.getDescription());


            manageButton = new Button("Modifier");
        }
        manageButton.setUIID("buttonWhiteCenter");

        Container container = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        container.setUIID("containerRounded");

        container.addAll(

                titreLabel, titreTF,
                descriptionLabel, descriptionTF,

                manageButton
        );

        this.addAll(container);
    }

    private void addActions() {

        if (currentBlog == null) {
            manageButton.addActionListener(action -> {
                if (controleDeSaisie()) {
                    int responseCode = BlogService.getInstance().add(
                            new Blog(


                                    0,
                                    titreTF.getText(),
                                    descriptionTF.getText()
                            )
                    );
                    if (responseCode == 200) {
                        Dialog.show("Succés", "Blog ajouté avec succes", new Command("Ok"));
                        showBackAndRefresh();
                    } else {
                        Dialog.show("Erreur", "Erreur d'ajout de blog. Code d'erreur : " + responseCode, new Command("Ok"));
                    }
                }
            });
        } else {
            manageButton.addActionListener(action -> {
                if (controleDeSaisie()) {
                    int responseCode = BlogService.getInstance().edit(
                            new Blog(
                                    currentBlog.getId(),


                                    0,
                                    titreTF.getText(),
                                    descriptionTF.getText()

                            )
                    );
                    if (responseCode == 200) {
                        Dialog.show("Succés", "Blog modifié avec succes", new Command("Ok"));
                        showBackAndRefresh();
                    } else {
                        Dialog.show("Erreur", "Erreur de modification de blog. Code d'erreur : " + responseCode, new Command("Ok"));
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



        if (titreTF.getText().equals("")) {
            Dialog.show("Avertissement", "Titre vide", new Command("Ok"));
            return false;
        }


        if (descriptionTF.getText().equals("")) {
            Dialog.show("Avertissement", "Description vide", new Command("Ok"));
            return false;
        }


        return true;
    }
}