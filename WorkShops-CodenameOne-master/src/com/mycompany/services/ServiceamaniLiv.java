/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yummygout.services;

import com.codename1.io.CharArrayReader;
import com.codename1.io.ConnectionRequest;
import com.codename1.io.JSONParser;
import com.codename1.io.NetworkEvent;
import com.codename1.io.NetworkManager;
import com.codename1.l10n.ParseException;
import com.codename1.l10n.SimpleDateFormat;
import com.codename1.ui.events.ActionListener;
import com.yummygout.entities.Livraison;
import com.yummygout.utils.Statics;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * @author HP
 */
public class ServiceamaniLiv {
    public static Boolean resultOk = true;
    //singleton
    public static ServiceamaniLiv instance= null;
    private ConnectionRequest req;
    public static ServiceamaniLiv getInstance(){
        if(instance == null)
            instance=new ServiceamaniLiv();
        return instance;
    }
    
    public ServiceamaniLiv() {
        req= new ConnectionRequest();
    }
    public void Ajouterlivraison(Livraison livraison){      
        String url = Statics.BASE_URL + "/manena?reflivraison="+livraison.getReflivraison()+"&region="+livraison.getRegion()+"&rueliv="+livraison.getRueliv()+"&client=amani";
        req.setUrl(url);
        req.addResponseListener((e) -> {
            String str = new String(req.getResponseData());
        });
        NetworkManager.getInstance().addToQueueAndWait(req);}
    
    
    
    
   public ArrayList<Livraison> Affichageamani(){
         ArrayList<Livraison> result = new ArrayList<>();
         //System.out.println(result.size());
         String url = Statics.BASE_URL + "/amani";
        req.setUrl(url);
         req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                JSONParser jsonp;     
                jsonp = new JSONParser();
                try {
                    System.out.println(jsonp.parseJSON(new CharArrayReader(new String(req.getResponseData()).toCharArray())));
                     Map<String, Object> mapReclamation = jsonp.parseJSON(new CharArrayReader(new String(req.getResponseData()).toCharArray()));
                    List<Map<String, Object>> listOfMaps = (List<Map<String, Object>>) mapReclamation.get("root");
                    for (Map<String, Object> obj : listOfMaps) {
                        Livraison r = new Livraison();                      
                       float id = Float.parseFloat(obj.get("idLivraison").toString());  
                       float ref = Float.parseFloat(obj.get("reflivraison").toString());
                       SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        Date create_at = format.parse(obj.get("date").toString().substring(0, 10));
                        String etat = obj.get("etat").toString();
                        String reclamation = obj.get("region").toString();
                        String sujetrec = obj.get("rueliv").toString(); 
                        String client = obj.get("client").toString();  
                        r.setIdLivraison((int)id);
                        r.setReflivraison((int)ref);
                        r.setRegion(reclamation);
                        r.setRueliv(sujetrec);
                        r.setEtat(etat);
                        r.setDate(create_at);
                        r.setClient(client);
                        result.add(r);
                    }
                } catch (Exception ex) {
                    System.out.println(ex);
                }}});
        NetworkManager.getInstance().addToQueueAndWait(req);
        return result;}

   public Livraison getOneReCc(String json) throws ParseException {
        Livraison r = new Livraison();
        try {
            JSONParser j = new JSONParser();
            Map<String, Object> obj = j.parseJSON(new CharArrayReader(json.toCharArray()));
            float id = Float.parseFloat(obj.get("id").toString());          
            r.setIdLivraison((int)id);
            r.setRegion(obj.get("region").toString());
            r.setRueliv(obj.get("rueliv").toString());
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date create_at = format.parse(obj.get("date").toString().substring(0, 10));
            r.setDate(create_at);} catch (IOException ex) {
        }NetworkManager.getInstance().addToQueueAndWait(req);return r;}
   
    public Livraison afficheDetail(int id) {
        Livraison r = new Livraison();
        String url = Statics.BASE_URL + "/detailamani/" + id;
        req.setUrl(url);
        req.addResponseListener((NetworkEvent evt) -> {
            ServiceamaniLiv ser = new ServiceamaniLiv();
            try {
                r.setIdLivraison(ser.getOneReCc(new String(req.getResponseData())).getIdLivraison());
           
            r.setRegion(ser.getOneReCc(new String(req.getResponseData())).getRegion());
            r.setRueliv(ser.getOneReCc(new String(req.getResponseData())).getRueliv());
            r.setEtat(ser.getOneReCc(new String(req.getResponseData())).getEtat());
             } catch (ParseException ex) {
                System.out.println(ex+"codenameone LOL");
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(req);

        return r;

        //Show Reclamation :
    }
      //Delete Reclamation
    public boolean deleteLivraison(int id) {
        String url = Statics.BASE_URL + "/suppjson/" +id;

        req.setUrl(url);
        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                req.removeResponseCodeListener(this);
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(req);
        return resultOk;}
 public ArrayList<Livraison> encours(){
         ArrayList<Livraison> result = new ArrayList<>();
         //System.out.println(result.size());
         String url = Statics.BASE_URL + "/a";
        req.setUrl(url);
         req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                JSONParser jsonp;     
                jsonp = new JSONParser();
                try {
                    System.out.println(jsonp.parseJSON(new CharArrayReader(new String(req.getResponseData()).toCharArray())));
                     Map<String, Object> mapReclamation = jsonp.parseJSON(new CharArrayReader(new String(req.getResponseData()).toCharArray()));
                    List<Map<String, Object>> listOfMaps = (List<Map<String, Object>>) mapReclamation.get("root");
                    for (Map<String, Object> obj : listOfMaps) {
                        Livraison r = new Livraison();                      
                       float id = Float.parseFloat(obj.get("idLivraison").toString());  
                       float ref = Float.parseFloat(obj.get("reflivraison").toString());
                       SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        Date create_at = format.parse(obj.get("date").toString().substring(0, 10));
                        String etat = obj.get("etat").toString();
                        String reclamation = obj.get("region").toString();
                        String sujetrec = obj.get("rueliv").toString(); 
                        String client = obj.get("client").toString();  
                        r.setIdLivraison((int)id);
                        r.setReflivraison((int)ref);
                        r.setRegion(reclamation);
                        r.setRueliv(sujetrec);
                        r.setEtat(etat);
                        r.setDate(create_at);
                        r.setClient(client);
                        result.add(r);
                    }
                } catch (Exception ex) {
                    System.out.println(ex);
                }}});
        NetworkManager.getInstance().addToQueueAndWait(req);
        return result;}
}
