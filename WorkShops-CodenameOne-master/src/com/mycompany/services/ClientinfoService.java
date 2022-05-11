package com.yummygout.services;

import com.codename1.components.InfiniteProgress;
import com.codename1.io.*;
import com.codename1.ui.events.ActionListener;
import com.yummygout.entities.Clientinfo;
import com.yummygout.utils.Statics;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ClientinfoService {

    public static ClientinfoService instance = null;
    public int resultCode;
    private ConnectionRequest cr;
    private ArrayList<Clientinfo> listClientinfos;


    private ClientinfoService() {
        cr = new ConnectionRequest();
    }

    public static ClientinfoService getInstance() {
        if (instance == null) {
            instance = new ClientinfoService();
        }
        return instance;
    }

    public ArrayList<Clientinfo> getAll() {
        listClientinfos = new ArrayList<>();

        cr = new ConnectionRequest();
        cr.setUrl(Statics.BASE_URL + "/clientinfo");
        cr.setHttpMethod("GET");

        cr.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {

                if (cr.getResponseCode() == 200) {
                    listClientinfos = getList();
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

        return listClientinfos;
    }

    private ArrayList<Clientinfo> getList() {
        try {
            Map<String, Object> parsedJson = new JSONParser().parseJSON(new CharArrayReader(
                    new String(cr.getResponseData()).toCharArray()
            ));
            List<Map<String, Object>> list = (List<Map<String, Object>>) parsedJson.get("root");

            for (Map<String, Object> obj : list) {
                Clientinfo clientinfo = new Clientinfo(
                        (int) Float.parseFloat(obj.get("id").toString()),

                        (String) obj.get("nom")

                );

                listClientinfos.add(clientinfo);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listClientinfos;
    }

    public int add(Clientinfo clientinfo) {
        return manage(clientinfo, false);
    }

    public int edit(Clientinfo clientinfo) {
        return manage(clientinfo, true);
    }

    public int manage(Clientinfo clientinfo, boolean isEdit) {

        cr = new ConnectionRequest();


        cr.setHttpMethod("POST");
        if (isEdit) {
            cr.setUrl(Statics.BASE_URL + "/clientinfo/edit");
            cr.addArgument("id", String.valueOf(clientinfo.getId()));
        } else {
            cr.setUrl(Statics.BASE_URL + "/clientinfo/add");
        }

        cr.addArgument("nom", clientinfo.getNom());


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

    public int delete(int clientinfoId) {
        cr = new ConnectionRequest();
        cr.setUrl(Statics.BASE_URL + "/clientinfo/delete");
        cr.setHttpMethod("POST");
        cr.addArgument("id", String.valueOf(clientinfoId));

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
