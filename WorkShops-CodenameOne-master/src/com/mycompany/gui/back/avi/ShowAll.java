package com.yummygout.gui.back.avi;

import com.codename1.components.InteractionDialog;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.yummygout.entities.Avi;
import com.yummygout.services.AviService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ShowAll extends Form {

    public static Avi currentAvi = null;
    Form previous;
    Button addBtn;
    Label blogLabel, clientinfoLabel, dateavisLabel, likeeLabel, deslikeLabel, descriptionLabel;
    Button editBtn, deleteBtn;
    Container btnsContainer;

    public ShowAll(Form previous) {
        super("Avis", new BoxLayout(BoxLayout.Y_AXIS));
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


        ArrayList<Avi> listAvis = AviService.getInstance().getAll();


        if (listAvis.size() > 0) {
            for (Avi avi : listAvis) {
                Component model = makeAviModel(avi);
                this.add(model);
            }
        } else {
            this.add(new Label("Aucune donnée"));
        }
    }

    private void addActions() {
        addBtn.addActionListener(action -> {
            currentAvi = null;
            new Manage(this).show();
        });

    }

    private Container makeModelWithoutButtons(Avi avi) {
        Container aviModel = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        aviModel.setUIID("containerRounded");


        blogLabel = new Label("Blog : " + avi.getBlog());
        blogLabel.setUIID("labelDefault");

        clientinfoLabel = new Label("Clientinfo : " + avi.getClientinfo());
        clientinfoLabel.setUIID("labelDefault");

        dateavisLabel = new Label("Dateavis : " + new SimpleDateFormat("dd-MM-yyyy").format(avi.getDateavis()));
        dateavisLabel.setUIID("labelDefault");

        likeeLabel = new Label("Likee : " + avi.getLikee());
        likeeLabel.setUIID("labelDefault");

        deslikeLabel = new Label("Deslike : " + avi.getDeslike());
        deslikeLabel.setUIID("labelDefault");

        descriptionLabel = new Label("Description : " + avi.getDescription());
        descriptionLabel.setUIID("labelDefault");

        blogLabel = new Label("Blog : " + avi.getBlog().getTitre());
        blogLabel.setUIID("labelDefault");

        clientinfoLabel = new Label("Clientinfo : " + avi.getClientinfo().getNom());
        clientinfoLabel.setUIID("labelDefault");


        aviModel.addAll(

                blogLabel, clientinfoLabel, dateavisLabel, likeeLabel, deslikeLabel, descriptionLabel
        );

        return aviModel;
    }

    private Component makeAviModel(Avi avi) {

        Container aviModel = makeModelWithoutButtons(avi);

        btnsContainer = new Container(new BorderLayout());
        btnsContainer.setUIID("containerButtons");

        editBtn = new Button("Modifier");
        editBtn.setUIID("buttonWhiteCenter");
        editBtn.addActionListener(action -> {
            currentAvi = avi;
            new Manage(this).show();
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


        aviModel.add(btnsContainer);

        return aviModel;
    }

}