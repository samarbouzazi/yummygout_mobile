package com.yummygout.services;

import com.codename1.components.InfiniteProgress;
import com.codename1.io.*;
import com.codename1.ui.events.ActionListener;
import com.yummygout.entities.Categorie;
import com.yummygout.utils.Statics;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CategorieService {

    public static CategorieService instance = null;
    public int resultCode;
    private ConnectionRequest cr;
    private ArrayList<Categorie> listCategories;


    private CategorieService() {
        cr = new ConnectionRequest();
    }

    public static CategorieService getInstance() {
        if (instance == null) {
            instance = new CategorieService();
        }
        return instance;
    }

    public ArrayList<Categorie> getAll() {
        listCategories = new ArrayList<>();

        cr = new ConnectionRequest();
        cr.setUrl(Statics.BASE_URL + "/categorie");
        cr.setHttpMethod("GET");

        cr.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {

                if (cr.getResponseCode() == 200) {
                    listCategories = getList();
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

        return listCategories;
    }

    private ArrayList<Categorie> getList() {
        try {
            Map<String, Object> parsedJson = new JSONParser().parseJSON(new CharArrayReader(
                    new String(cr.getResponseData()).toCharArray()
            ));
            List<Map<String, Object>> list = (List<Map<String, Object>>) parsedJson.get("root");

            for (Map<String, Object> obj : list) {
                Categorie categorie = new Categorie(
                        (int) Float.parseFloat(obj.get("id").toString()),

                        (String) obj.get("image"),
                        (String) obj.get("nom")

                );

                listCategories.add(categorie);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listCategories;
    }

    public int add(Categorie categorie) {
        return manage(categorie, false, true);
    }

    public int edit(Categorie categorie, boolean imageEdited) {
        return manage(categorie, true, imageEdited);
    }

    public int manage(Categorie categorie, boolean isEdit, boolean imageEdited) {

        MultipartRequest cr = new MultipartRequest();
        cr.setFilename("file", "Categorie.jpg");


        cr.setHttpMethod("POST");
        if (isEdit) {
            cr.setUrl(Statics.BASE_URL + "/categorie/edit");
            cr.addArgumentNoEncoding("id", String.valueOf(categorie.getId()));
        } else {
            cr.setUrl(Statics.BASE_URL + "/categorie/add");
        }

        if (imageEdited) {
            try {
                cr.addData("file", categorie.getImage(), "image/jpeg");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            cr.addArgumentNoEncoding("image", categorie.getImage());
        }

        cr.addArgumentNoEncoding("image", categorie.getImage());
        cr.addArgumentNoEncoding("nom", categorie.getNom());


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

    public int delete(int categorieId) {
        cr = new ConnectionRequest();
        cr.setUrl(Statics.BASE_URL + "/categorie/delete");
        cr.setHttpMethod("POST");
        cr.addArgument("id", String.valueOf(categorieId));

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
