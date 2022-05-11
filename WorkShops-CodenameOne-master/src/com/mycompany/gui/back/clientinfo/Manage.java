package com.yummygout.gui.back.clientinfo;


import com.codename1.ui.*;
import com.codename1.ui.layouts.BoxLayout;
import com.yummygout.entities.Clientinfo;
import com.yummygout.services.ClientinfoService;

public class Manage extends Form {


    Clientinfo currentClientinfo;

    Label nomLabel;
    TextField nomTF;


    Button manageButton;

    Form previous;

    public Manage(Form previous) {
        super(ShowAll.currentClientinfo == null ? "Ajouter" : "Modifier", new BoxLayout(BoxLayout.Y_AXIS));
        this.previous = previous;

        currentClientinfo = ShowAll.currentClientinfo;

        addGUIs();
        addActions();

        getToolbar().addMaterialCommandToLeftBar("  ", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
    }

    private void addGUIs() {


        nomLabel = new Label("Nom : ");
        nomLabel.setUIID("labelDefault");
        nomTF = new TextField();
        nomTF.setHint("Tapez le nom");


        if (currentClientinfo == null) {


            manageButton = new Button("Ajouter");
        } else {
            nomTF.setText(currentClientinfo.getNom());


            manageButton = new Button("Modifier");
        }
        manageButton.setUIID("buttonWhiteCenter");

        Container container = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        container.setUIID("containerRounded");

        container.addAll(

                nomLabel, nomTF,

                manageButton
        );

        this.addAll(container);
    }

    private void addActions() {

        if (currentClientinfo == null) {
            manageButton.addActionListener(action -> {
                if (controleDeSaisie()) {
                    int responseCode = ClientinfoService.getInstance().add(
                            new Clientinfo(


                                    nomTF.getText()
                            )
                    );
                    if (responseCode == 200) {
                        Dialog.show("Succés", "Clientinfo ajouté avec succes", new Command("Ok"));
                        showBackAndRefresh();
                    } else {
                        Dialog.show("Erreur", "Erreur d'ajout de clientinfo. Code d'erreur : " + responseCode, new Command("Ok"));
                    }
                }
            });
        } else {
            manageButton.addActionListener(action -> {
                if (controleDeSaisie()) {
                    int responseCode = ClientinfoService.getInstance().edit(
                            new Clientinfo(
                                    currentClientinfo.getId(),


                                    nomTF.getText()

                            )
                    );
                    if (responseCode == 200) {
                        Dialog.show("Succés", "Clientinfo modifié avec succes", new Command("Ok"));
                        showBackAndRefresh();
                    } else {
                        Dialog.show("Erreur", "Erreur de modification de clientinfo. Code d'erreur : " + responseCode, new Command("Ok"));
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


        if (nomTF.getText().equals("")) {
            Dialog.show("Avertissement", "Nom vide", new Command("Ok"));
            return false;
        }


        return true;
    }
}