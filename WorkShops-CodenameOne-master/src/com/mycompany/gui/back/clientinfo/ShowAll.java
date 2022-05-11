package com.yummygout.gui.back.clientinfo;

import com.codename1.components.InteractionDialog;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.yummygout.entities.Clientinfo;
import com.yummygout.services.ClientinfoService;

import java.util.ArrayList;

public class ShowAll extends Form {

    public static Clientinfo currentClientinfo = null;
    Form previous;
    Button addBtn;
    Label nomLabel;
    Button editBtn, deleteBtn;
    Container btnsContainer;

    public ShowAll(Form previous) {
        super("Clientinfos", new BoxLayout(BoxLayout.Y_AXIS));
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


        ArrayList<Clientinfo> listClientinfos = ClientinfoService.getInstance().getAll();


        if (listClientinfos.size() > 0) {
            for (Clientinfo clientinfo : listClientinfos) {
                Component model = makeClientinfoModel(clientinfo);
                this.add(model);
            }
        } else {
            this.add(new Label("Aucune donnée"));
        }
    }

    private void addActions() {
        addBtn.addActionListener(action -> {
            currentClientinfo = null;
            new Manage(this).show();
        });

    }

    private Container makeModelWithoutButtons(Clientinfo clientinfo) {
        Container clientinfoModel = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        clientinfoModel.setUIID("containerRounded");


        nomLabel = new Label("Nom : " + clientinfo.getNom());
        nomLabel.setUIID("labelDefault");


        clientinfoModel.addAll(

                nomLabel
        );

        return clientinfoModel;
    }

    private Component makeClientinfoModel(Clientinfo clientinfo) {

        Container clientinfoModel = makeModelWithoutButtons(clientinfo);

        btnsContainer = new Container(new BorderLayout());
        btnsContainer.setUIID("containerButtons");

        editBtn = new Button("Modifier");
        editBtn.setUIID("buttonWhiteCenter");
        editBtn.addActionListener(action -> {
            currentClientinfo = clientinfo;
            new Manage(this).show();
        });

        deleteBtn = new Button("Supprimer");
        deleteBtn.setUIID("buttonWhiteCenter");
        deleteBtn.addActionListener(action -> {
            InteractionDialog dlg = new InteractionDialog("Confirmer la suppression");
            dlg.setLayout(new BorderLayout());
            dlg.add(BorderLayout.CENTER, new Label("Voulez vous vraiment supprimer ce clientinfo ?"));
            Button btnClose = new Button("Annuler");
            btnClose.addActionListener((ee) -> dlg.dispose());
            Button btnConfirm = new Button("Confirmer");
            btnConfirm.addActionListener(actionConf -> {
                int responseCode = ClientinfoService.getInstance().delete(clientinfo.getId());

                if (responseCode == 200) {
                    currentClientinfo = null;
                    dlg.dispose();
                    clientinfoModel.remove();
                    this.refreshTheme();
                } else {
                    Dialog.show("Erreur", "Erreur de suppression du clientinfo. Code d'erreur : " + responseCode, new Command("Ok"));
                }
            });
            Container btnContainer = new Container(new BoxLayout(BoxLayout.X_AXIS));
            btnContainer.addAll(btnClose, btnConfirm);
            dlg.addComponent(BorderLayout.SOUTH, btnContainer);
            dlg.show(800, 800, 0, 0);
        });

        btnsContainer.add(BorderLayout.WEST, editBtn);
        btnsContainer.add(BorderLayout.EAST, deleteBtn);


        clientinfoModel.add(btnsContainer);

        return clientinfoModel;
    }

}