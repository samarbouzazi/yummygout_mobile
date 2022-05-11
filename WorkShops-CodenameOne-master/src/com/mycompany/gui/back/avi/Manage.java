package com.yummygout.gui.back.avi;


import com.codename1.ui.*;
import com.codename1.ui.layouts.BoxLayout;
import com.yummygout.entities.Avi;
import com.yummygout.entities.Blog;
import com.yummygout.entities.Clientinfo;
import com.yummygout.services.AviService;
import com.yummygout.services.BlogService;
import com.yummygout.services.ClientinfoService;

import java.util.ArrayList;

public class Manage extends Form {


    Avi currentAvi;

    Label likeeLabel, deslikeLabel, descriptionLabel;
    TextField likeeTF, deslikeTF, descriptionTF;
    PickerComponent dateavisTF;

    ArrayList<Blog> listBlogs;
    PickerComponent blogPC;
    Blog selectedBlog = null;
    ArrayList<Clientinfo> listClientinfos;
    PickerComponent clientinfoPC;
    Clientinfo selectedClientinfo = null;


    Button manageButton;

    Form previous;

    public Manage(Form previous) {
        super(ShowAll.currentAvi == null ? "Ajouter" : "Modifier", new BoxLayout(BoxLayout.Y_AXIS));
        this.previous = previous;

        currentAvi = ShowAll.currentAvi;

        addGUIs();
        addActions();

        getToolbar().addMaterialCommandToLeftBar("  ", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
    }

    private void addGUIs() {

        String[] blogStrings;
        int blogIndex;
        blogPC = PickerComponent.createStrings("").label("Blog");
        listBlogs = BlogService.getInstance().getAll();
        blogStrings = new String[listBlogs.size()];
        blogIndex = 0;
        for (Blog blog : listBlogs) {
            blogStrings[blogIndex] = blog.getTitre();
            blogIndex++;
        }
        blogPC.getPicker().setStrings(blogStrings);
        blogPC.getPicker().addActionListener(l -> selectedBlog = listBlogs.get(blogPC.getPicker().getSelectedStringIndex()));

        String[] clientinfoStrings;
        int clientinfoIndex;
        clientinfoPC = PickerComponent.createStrings("").label("Clientinfo");
        listClientinfos = ClientinfoService.getInstance().getAll();
        clientinfoStrings = new String[listClientinfos.size()];
        clientinfoIndex = 0;
        for (Clientinfo clientinfo : listClientinfos) {
            clientinfoStrings[clientinfoIndex] = clientinfo.getNom();
            clientinfoIndex++;
        }
        clientinfoPC.getPicker().setStrings(clientinfoStrings);
        clientinfoPC.getPicker().addActionListener(l -> selectedClientinfo = listClientinfos.get(clientinfoPC.getPicker().getSelectedStringIndex()));


        dateavisTF = PickerComponent.createDate(null).label("Dateavis");


        likeeLabel = new Label("Likee : ");
        likeeLabel.setUIID("labelDefault");
        likeeTF = new TextField();
        likeeTF.setHint("Tapez le likee");


        deslikeLabel = new Label("Deslike : ");
        deslikeLabel.setUIID("labelDefault");
        deslikeTF = new TextField();
        deslikeTF.setHint("Tapez le deslike");


        descriptionLabel = new Label("Description : ");
        descriptionLabel.setUIID("labelDefault");
        descriptionTF = new TextField();
        descriptionTF.setHint("Tapez le description");


        if (currentAvi == null) {


            manageButton = new Button("Ajouter");
        } else {


            dateavisTF.getPicker().setDate(currentAvi.getDateavis());
            likeeTF.setText(String.valueOf(currentAvi.getLikee()));
            deslikeTF.setText(String.valueOf(currentAvi.getDeslike()));
            descriptionTF.setText(currentAvi.getDescription());

            blogPC.getPicker().setSelectedString(currentAvi.getBlog().getTitre());
            selectedBlog = currentAvi.getBlog();
            clientinfoPC.getPicker().setSelectedString(currentAvi.getClientinfo().getNom());
            selectedClientinfo = currentAvi.getClientinfo();


            manageButton = new Button("Modifier");
        }
        manageButton.setUIID("buttonWhiteCenter");

        Container container = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        container.setUIID("containerRounded");

        container.addAll(


                dateavisTF,
                likeeLabel, likeeTF,
                deslikeLabel, deslikeTF,
                descriptionLabel, descriptionTF,
                blogPC, clientinfoPC,
                manageButton
        );

        this.addAll(container);
    }

    private void addActions() {

        if (currentAvi == null) {
            manageButton.addActionListener(action -> {
                if (controleDeSaisie()) {
                    int responseCode = AviService.getInstance().add(
                            new Avi(


                                    selectedBlog,
                                    selectedClientinfo,
                                    dateavisTF.getPicker().getDate(),
                                    (int) Float.parseFloat(likeeTF.getText()),
                                    (int) Float.parseFloat(deslikeTF.getText()),
                                    descriptionTF.getText()
                            )
                    );
                    if (responseCode == 200) {
                        Dialog.show("Succés", "Avi ajouté avec succes", new Command("Ok"));
                        showBackAndRefresh();
                    } else {
                        Dialog.show("Erreur", "Erreur d'ajout de avi. Code d'erreur : " + responseCode, new Command("Ok"));
                    }
                }
            });
        } else {
            manageButton.addActionListener(action -> {
                if (controleDeSaisie()) {
                    int responseCode = AviService.getInstance().edit(
                            new Avi(
                                    currentAvi.getId(),


                                    selectedBlog,
                                    selectedClientinfo,
                                    dateavisTF.getPicker().getDate(),
                                    (int) Float.parseFloat(likeeTF.getText()),
                                    (int) Float.parseFloat(deslikeTF.getText()),
                                    descriptionTF.getText()

                            )
                    );
                    if (responseCode == 200) {
                        Dialog.show("Succés", "Avi modifié avec succes", new Command("Ok"));
                        showBackAndRefresh();
                    } else {
                        Dialog.show("Erreur", "Erreur de modification de avi. Code d'erreur : " + responseCode, new Command("Ok"));
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


        if (dateavisTF.getPicker().getDate() == null) {
            Dialog.show("Avertissement", "Veuillez saisir la dateavis", new Command("Ok"));
            return false;
        }


        if (likeeTF.getText().equals("")) {
            Dialog.show("Avertissement", "Likee vide", new Command("Ok"));
            return false;
        }
        try {
            Float.parseFloat(likeeTF.getText());
        } catch (NumberFormatException e) {
            Dialog.show("Avertissement", likeeTF.getText() + " n'est pas un nombre valide (likee)", new Command("Ok"));
            return false;
        }


        if (deslikeTF.getText().equals("")) {
            Dialog.show("Avertissement", "Deslike vide", new Command("Ok"));
            return false;
        }
        try {
            Float.parseFloat(deslikeTF.getText());
        } catch (NumberFormatException e) {
            Dialog.show("Avertissement", deslikeTF.getText() + " n'est pas un nombre valide (deslike)", new Command("Ok"));
            return false;
        }


        if (descriptionTF.getText().equals("")) {
            Dialog.show("Avertissement", "Description vide", new Command("Ok"));
            return false;
        }


        if (selectedBlog == null) {
            Dialog.show("Avertissement", "Veuillez choisir un blog", new Command("Ok"));
            return false;
        }

        if (selectedClientinfo == null) {
            Dialog.show("Avertissement", "Veuillez choisir un clientinfo", new Command("Ok"));
            return false;
        }


        return true;
    }
}