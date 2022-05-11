package com.yummygout.gui.front.clientinfo;

import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.yummygout.entities.Clientinfo;
import com.yummygout.services.ClientinfoService;

import java.util.ArrayList;

public class ShowAll extends Form {

    public static Clientinfo currentClientinfo = null;
    Form previous;
    Label nomLabel;
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


        clientinfoModel.add(btnsContainer);

        return clientinfoModel;
    }

}