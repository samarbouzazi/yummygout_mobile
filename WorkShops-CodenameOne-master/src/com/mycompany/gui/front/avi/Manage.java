package com.yummygout.gui.front.avi;


import com.codename1.ui.*;
import com.codename1.ui.layouts.BoxLayout;
import com.yummygout.entities.Avi;
import com.yummygout.entities.Blog;
import com.yummygout.entities.Clientinfo;
import com.yummygout.gui.front.blog.ShowAll;
import com.yummygout.services.AviService;
import com.yummygout.services.ClientinfoService;

import java.util.ArrayList;

public class Manage extends Form {

    Avi currentAvi;

    Label descriptionLabel;
    TextField descriptionTF;
    CheckBox likeCB;

    public static Blog selectedBlog = null;
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


        likeCB = new CheckBox("Like : ");

        descriptionLabel = new Label("Description : ");
        descriptionLabel.setUIID("labelDefault");
        descriptionTF = new TextField();
        descriptionTF.setHint("Tapez le description");


        if (currentAvi == null) {
            manageButton = new Button("Ajouter");
        } else {
            likeCB.setSelected(currentAvi.getLikee() == 1);
            descriptionTF.setText(currentAvi.getDescription());
            selectedBlog = currentAvi.getBlog();
            clientinfoPC.getPicker().setSelectedString(currentAvi.getClientinfo().getNom());
            selectedClientinfo = currentAvi.getClientinfo();


            manageButton = new Button("Modifier");
        }
        manageButton.setUIID("buttonWhiteCenter");

        Container container = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        container.setUIID("containerRounded");

        container.addAll(
                likeCB,
                descriptionLabel, descriptionTF
                , clientinfoPC,
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
                                    null,
                                    likeCB.isSelected() ? 1 : 0,
                                    likeCB.isSelected() ? 0 : 1,
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
                                    null,
                                    likeCB.isSelected() ? 1 : 0,
                                    likeCB.isSelected() ? 0 : 1,
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
        ((ShowAll) ShowAll.blogForm).refresh();
        previous.showBack();
    }

    private boolean controleDeSaisie() {

        if (descriptionTF.getText().equals("")) {
            Dialog.show("Avertissement", "Description vide", new Command("Ok"));
            return false;
        }

        if (selectedClientinfo == null) {
            Dialog.show("Avertissement", "Veuillez choisir un client", new Command("Ok"));
            return false;
        }

        return true;
    }
}