package com.yummygout.services;

import com.codename1.components.InfiniteProgress;
import com.codename1.io.*;
import com.codename1.ui.events.ActionListener;
import com.yummygout.entities.Fournisseur;
import com.yummygout.utils.Statics;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FournisseurService {

    public static FournisseurService instance = null;
    public int resultCode;
    private ConnectionRequest cr;
    private ArrayList<Fournisseur> listFournisseurs;


    private FournisseurService() {
        cr = new ConnectionRequest();
    }

    public static FournisseurService getInstance() {
        if (instance == null) {
            instance = new FournisseurService();
        }
        return instance;
    }

    public ArrayList<Fournisseur> getAll() {
        listFournisseurs = new ArrayList<>();

        cr = new ConnectionRequest();
        cr.setUrl(Statics.BASE_URL + "/fournisseur");
        cr.setHttpMethod("GET");

        cr.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {

                if (cr.getResponseCode() == 200) {
                    listFournisseurs = getList();
                }

                cr.removeResponseListener(this);
            }
        });

        try {
            cr.setDisposeOnCompletion(new InfiniteProgress().showInfiniteBlocking());
            NetworkManager.getInstance().addToQueueAndWait(cr);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return listFournisseurs;
    }

    private ArrayList<Fournisseur> getList() {
        try {
            Map<String, Object> parsedJson = new JSONParser().parseJSON(new CharArrayReader(
                    new String(cr.getResponseData()).toCharArray()
            ));
            List<Map<String, Object>> list = (List<Map<String, Object>>) parsedJson.get("root");

            for (Map<String, Object> obj : list) {
                Fournisseur fournisseur = new Fournisseur(
                        (int) Float.parseFloat(obj.get("id").toString()),

                        (String) obj.get("nom"),
                        (String) obj.get("prenom"),
                        (String) obj.get("categorie"),
                        (int) Float.parseFloat(obj.get("tel").toString()),
                        (String) obj.get("add")

                );

                listFournisseurs.add(fournisseur);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listFournisseurs;
    }

    public int add(Fournisseur fournisseur) {
        return manage(fournisseur, false);
    }

    public int edit(Fournisseur fournisseur) {
        return manage(fournisseur, true);
    }

    public int manage(Fournisseur fournisseur, boolean isEdit) {

        cr = new ConnectionRequest();


        cr.setHttpMethod("POST");
        if (isEdit) {
            cr.setUrl(Statics.BASE_URL + "/fournisseur/edit");
            cr.addArgument("id", String.valueOf(fournisseur.getId()));
        } else {
            cr.setUrl(Statics.BASE_URL + "/fournisseur/add");
        }

        cr.addArgument("nom", fournisseur.getNom());
        cr.addArgument("prenom", fournisseur.getPrenom());
        cr.addArgument("categorie", fournisseur.getCategorie());
        cr.addArgument("tel", String.valueOf(fournisseur.getTel()));
        cr.addArgument("add", fournisseur.getAdd());


        cr.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                resultCode = cr.getResponseCode();
                cr.removeResponseListener(this);
            }
        });
        try {
            cr.setDisposeOnCompletion(new InfiniteProgress().showInfiniteBlocking());
            NetworkManager.getInstance().addToQueueAndWait(cr);
        } catch (Exception ignored) {

        }
        return resultCode;
    }

    public int delete(int fournisseurId) {
        cr = new ConnectionRequest();
        cr.setUrl(Statics.BASE_URL + "/fournisseur/delete");
        cr.setHttpMethod("POST");
        cr.addArgument("id", String.valueOf(fournisseurId));

        cr.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                cr.removeResponseListener(this);
            }
        });

        try {
            cr.setDisposeOnCompletion(new InfiniteProgress().showInfiniteBlocking());
            NetworkManager.getInstance().addToQueueAndWait(cr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cr.getResponseCode();
    }
}
