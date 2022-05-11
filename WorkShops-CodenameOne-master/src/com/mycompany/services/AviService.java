package com.yummygout.services;

import com.codename1.components.InfiniteProgress;
import com.codename1.io.*;
import com.codename1.ui.events.ActionListener;
import com.yummygout.entities.Avi;
import com.yummygout.entities.Blog;
import com.yummygout.entities.Clientinfo;
import com.yummygout.utils.Statics;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AviService {

    public static AviService instance = null;
    public int resultCode;
    private ConnectionRequest cr;
    private ArrayList<Avi> listAvis;


    private AviService() {
        cr = new ConnectionRequest();
    }

    public static AviService getInstance() {
        if (instance == null) {
            instance = new AviService();
        }
        return instance;
    }

    public ArrayList<Avi> getAll() {
        listAvis = new ArrayList<>();

        cr = new ConnectionRequest();
        cr.setUrl(Statics.BASE_URL + "/avi");
        cr.setHttpMethod("GET");

        cr.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {

                if (cr.getResponseCode() == 200) {
                    listAvis = getList();
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

        return listAvis;
    }

    private ArrayList<Avi> getList() {
        try {
            Map<String, Object> parsedJson = new JSONParser().parseJSON(new CharArrayReader(
                    new String(cr.getResponseData()).toCharArray()
            ));
            List<Map<String, Object>> list = (List<Map<String, Object>>) parsedJson.get("root");

            for (Map<String, Object> obj : list) {
                Avi avi = new Avi(
                        (int) Float.parseFloat(obj.get("id").toString()),

                        makeBlog((Map<String, Object>) obj.get("blog")),
                        makeClientinfo((Map<String, Object>) obj.get("clientinfo")),
                        new SimpleDateFormat("dd-MM-yyyy").parse((String) obj.get("dateavis")),
                        (int) Float.parseFloat(obj.get("likee").toString()),
                        (int) Float.parseFloat(obj.get("deslike").toString()),
                        (String) obj.get("description")

                );

                listAvis.add(avi);
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return listAvis;
    }

    public Blog makeBlog(Map<String, Object> obj) {
        if (obj == null) {
            return null;
        }
        Blog blog = new Blog();
        blog.setId((int) Float.parseFloat(obj.get("id").toString()));
        blog.setTitre((String) obj.get("titre"));
        return blog;
    }

    public Clientinfo makeClientinfo(Map<String, Object> obj) {
        if (obj == null) {
            return null;
        }
        Clientinfo clientinfo = new Clientinfo();
        clientinfo.setId((int) Float.parseFloat(obj.get("id").toString()));
        clientinfo.setNom((String) obj.get("nom"));
        return clientinfo;
    }

    public int add(Avi avi) {
        return manage(avi, false);
    }

    public int edit(Avi avi) {
        return manage(avi, true);
    }

    public int manage(Avi avi, boolean isEdit) {

        cr = new ConnectionRequest();


        cr.setHttpMethod("POST");
        if (isEdit) {
            cr.setUrl(Statics.BASE_URL + "/avi/edit");
            cr.addArgument("id", String.valueOf(avi.getId()));
        } else {
            cr.setUrl(Statics.BASE_URL + "/avi/add");
        }

        cr.addArgument("blog", String.valueOf(avi.getBlog().getId()));
        cr.addArgument("clientinfo", String.valueOf(avi.getClientinfo().getId()));
        cr.addArgument("likee", String.valueOf(avi.getLikee()));
        cr.addArgument("deslike", String.valueOf(avi.getDeslike()));
        cr.addArgument("description", avi.getDescription());


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

    public int delete(int aviId) {
        cr = new ConnectionRequest();
        cr.setUrl(Statics.BASE_URL + "/avi/delete");
        cr.setHttpMethod("POST");
        cr.addArgument("id", String.valueOf(aviId));

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
