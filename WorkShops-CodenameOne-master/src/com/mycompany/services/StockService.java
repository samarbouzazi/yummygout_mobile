package com.yummygout.services;

import com.codename1.components.InfiniteProgress;
import com.codename1.io.*;
import com.codename1.ui.events.ActionListener;
import com.yummygout.entities.Fournisseur;
import com.yummygout.entities.Stock;
import com.yummygout.utils.Statics;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StockService {

    public static StockService instance = null;
    public int resultCode;
    private ConnectionRequest cr;
    private ArrayList<Stock> listStocks;


    private StockService() {
        cr = new ConnectionRequest();
    }

    public static StockService getInstance() {
        if (instance == null) {
            instance = new StockService();
        }
        return instance;
    }

    public ArrayList<Stock> getAll() {
        listStocks = new ArrayList<>();

        cr = new ConnectionRequest();
        cr.setUrl(Statics.BASE_URL + "/stock");
        cr.setHttpMethod("GET");

        cr.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {

                if (cr.getResponseCode() == 200) {
                    listStocks = getList();
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

        return listStocks;
    }

    private ArrayList<Stock> getList() {
        try {
            Map<String, Object> parsedJson = new JSONParser().parseJSON(new CharArrayReader(
                    new String(cr.getResponseData()).toCharArray()
            ));
            List<Map<String, Object>> list = (List<Map<String, Object>>) parsedJson.get("root");

            for (Map<String, Object> obj : list) {
                Stock stock = new Stock(
                        (int) Float.parseFloat(obj.get("id").toString()),

                        makeFournisseur((Map<String, Object>) obj.get("fournisseur")),
                        (String) obj.get("nom"),
                        new SimpleDateFormat("dd-MM-yyyy").parse((String) obj.get("dateAjout")),
                        new SimpleDateFormat("dd-MM-yyyy").parse((String) obj.get("dateFin")),
                        Float.parseFloat(obj.get("prix").toString()),
                        (int) Float.parseFloat(obj.get("quantite").toString())

                );

                listStocks.add(stock);
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return listStocks;
    }

    public Fournisseur makeFournisseur(Map<String, Object> obj) {
        if (obj == null) {
            return null;
        }
        Fournisseur fournisseur = new Fournisseur();
        fournisseur.setId((int) Float.parseFloat(obj.get("id").toString()));
        fournisseur.setNom((String) obj.get("nom"));
        return fournisseur;
    }

    public int add(Stock stock) {
        return manage(stock, false);
    }

    public int edit(Stock stock) {
        return manage(stock, true);
    }

    public int manage(Stock stock, boolean isEdit) {

        cr = new ConnectionRequest();


        cr.setHttpMethod("POST");
        if (isEdit) {
            cr.setUrl(Statics.BASE_URL + "/stock/edit");
            cr.addArgument("id", String.valueOf(stock.getId()));
        } else {
            cr.setUrl(Statics.BASE_URL + "/stock/add");
        }

        cr.addArgument("fournisseur", String.valueOf(stock.getFournisseur().getId()));
        cr.addArgument("nom", stock.getNom());
        cr.addArgument("dateAjout", new SimpleDateFormat("dd-MM-yyyy").format(stock.getDateAjout()));
        cr.addArgument("dateFin", new SimpleDateFormat("dd-MM-yyyy").format(stock.getDateFin()));
        cr.addArgument("prix", String.valueOf(stock.getPrix()));
        cr.addArgument("quantite", String.valueOf(stock.getQuantite()));


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

    public int delete(int stockId) {
        cr = new ConnectionRequest();
        cr.setUrl(Statics.BASE_URL + "/stock/delete");
        cr.setHttpMethod("POST");
        cr.addArgument("id", String.valueOf(stockId));

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
