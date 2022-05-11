package com.yummygout.services;

import com.codename1.components.InfiniteProgress;
import com.codename1.io.*;
import com.codename1.ui.events.ActionListener;
import com.yummygout.entities.Categorie;
import com.yummygout.entities.Plat;
import com.yummygout.utils.Statics;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PlatService {

    public static PlatService instance = null;
    public int resultCode;
    private ConnectionRequest cr;
    private ArrayList<Plat> listPlats;


    private PlatService() {
        cr = new ConnectionRequest();
    }

    public static PlatService getInstance() {
        if (instance == null) {
            instance = new PlatService();
        }
        return instance;
    }

    public ArrayList<Plat> getAll() {
        listPlats = new ArrayList<>();

        cr = new ConnectionRequest();
        cr.setUrl(Statics.BASE_URL + "/plat");
        cr.setHttpMethod("GET");

        cr.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {

                if (cr.getResponseCode() == 200) {
                    listPlats = getList();
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

        return listPlats;
    }

    private ArrayList<Plat> getList() {
        try {
            Map<String, Object> parsedJson = new JSONParser().parseJSON(new CharArrayReader(
                    new String(cr.getResponseData()).toCharArray()
            ));
            List<Map<String, Object>> list = (List<Map<String, Object>>) parsedJson.get("root");

            for (Map<String, Object> obj : list) {
                Plat plat = new Plat(
                        (int) Float.parseFloat(obj.get("id").toString()),

                        (String) obj.get("image"),
                        makeCategorie((Map<String, Object>) obj.get("categorie")),
                        (String) obj.get("nom"),
                        (String) obj.get("description"),
                        (int) Float.parseFloat(obj.get("prix").toString()),
                        (int) Float.parseFloat(obj.get("quantite").toString()),
                        (int) Float.parseFloat(obj.get("stock").toString())

                );

                listPlats.add(plat);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listPlats;
    }

    public Categorie makeCategorie(Map<String, Object> obj) {
        if (obj == null) {
            return null;
        }
        Categorie categorie = new Categorie();
        categorie.setId((int) Float.parseFloat(obj.get("id").toString()));
        categorie.setNom((String) obj.get("nom"));
        return categorie;
    }

    public int add(Plat plat) {
        return manage(plat, false, true);
    }

    public int edit(Plat plat, boolean imageEdited) {
        return manage(plat, true, imageEdited);
    }

    public int manage(Plat plat, boolean isEdit, boolean imageEdited) {

        MultipartRequest cr = new MultipartRequest();
        cr.setFilename("file", "Plat.jpg");


        cr.setHttpMethod("POST");
        if (isEdit) {
            cr.setUrl(Statics.BASE_URL + "/plat/edit");
            cr.addArgumentNoEncoding("id", String.valueOf(plat.getId()));
        } else {
            cr.setUrl(Statics.BASE_URL + "/plat/add");
        }

        if (imageEdited) {
            try {
                cr.addData("file", plat.getImage(), "image/jpeg");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            cr.addArgumentNoEncoding("image", plat.getImage());
        }

        cr.addArgumentNoEncoding("image", plat.getImage());
        cr.addArgumentNoEncoding("categorie", String.valueOf(plat.getCategorie().getId()));
        cr.addArgumentNoEncoding("nom", plat.getNom());
        cr.addArgumentNoEncoding("description", plat.getDescription());
        cr.addArgumentNoEncoding("prix", String.valueOf(plat.getPrix()));
        cr.addArgumentNoEncoding("quantite", String.valueOf(plat.getQuantite()));
        cr.addArgumentNoEncoding("stock", String.valueOf(plat.getStock()));


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

    public int delete(int platId) {
        cr = new ConnectionRequest();
        cr.setUrl(Statics.BASE_URL + "/plat/delete");
        cr.setHttpMethod("POST");
        cr.addArgument("id", String.valueOf(platId));

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
