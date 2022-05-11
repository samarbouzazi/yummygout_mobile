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
import static com.fasterxml.jackson.databind.type.LogicalType.DateTime;
import com.yummygout.entities.reclamationliv;
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
public class servicereclamation {
    public static servicereclamation instance= null ; 
    
    //intialisation connexion
    private ConnectionRequest req ; 
     public static Boolean resultOk = true;
    public static servicereclamation getInstance(){
    if(instance == null )
        instance=new servicereclamation();
     return instance;
    }
    
    public servicereclamation(){
    req= new ConnectionRequest();
    }
    
      
  public void ajouterrrRec(reclamationliv Reclamation) {
        String datemodif="2022-05-09T00:00:00+02:00";
        String clt="amani";
   String url = Statics.BASE_URL +"/me?reclamation="+Reclamation.getReclamation()+"&sujetrec="+Reclamation.getSujetrec();       
        req.setUrl(url);
        req.addResponseListener((e) -> {

            String str = new String(req.getResponseData());

        });
        NetworkManager.getInstance().addToQueueAndWait(req);
    }
    
    
     public ArrayList<reclamationliv> AffichageReclamation() {
         ArrayList<reclamationliv> result = new ArrayList<>();
         String url = Statics.BASE_URL + "/marwa";
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

                        reclamationliv r = new reclamationliv();
                       
                       float id = Float.parseFloat(obj.get("idreclivv").toString());
                       
                        String reclamation = obj.get("reclamation").toString();
                        String sujetrec = obj.get("sujetrec").toString();
                       // String clientname = obj.get("reclamation").toString();
                       // String idLivraison = obj.get("reclamation").toString();
                        r.setIdreclivv((int)id);
                        r.setReclamation(reclamation);
                        r.setSujetrec(sujetrec);
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        Date create_at = format.parse(obj.get("createdat").toString().substring(0, 10));                      
                         r.setCreatedat(create_at);
                        result.add(r);
                    }
                } catch (Exception ex) {
                    System.out.println(ex);
                }

            }

        });
        NetworkManager.getInstance().addToQueueAndWait(req);
        return result;

                }
     

    public reclamationliv getOneReCc(String json) throws ParseException {
        reclamationliv r = new reclamationliv();

        try {

            JSONParser j = new JSONParser();

            Map<String, Object> obj = j.parseJSON(new CharArrayReader(json.toCharArray()));

              float id = Float.parseFloat(obj.get("id").toString());
          

          r.setIdreclivv((int)id);
            r.setReclamation(obj.get("reclamation").toString());
            r.setSujetrec(obj.get("sujetrec").toString());

                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
  Date create_at = format.parse(obj.get("createdat").toString().substring(0, 10));

                         r.setCreatedat(create_at);

        } catch (IOException ex) {
        }
        return r;

    }

    public reclamationliv afficheDetail(int id) {
        reclamationliv r = new reclamationliv();
        String url = Statics.BASE_URL + "/detail/" + id;
        req.setUrl(url);
        req.addResponseListener((NetworkEvent evt) -> {
            servicereclamation ser = new servicereclamation();
            try {
                r.setIdreclivv(ser.getOneReCc(new String(req.getResponseData())).getIdreclivv());
           
            r.setReclamation(ser.getOneReCc(new String(req.getResponseData())).getReclamation());
            r.setSujetrec(ser.getOneReCc(new String(req.getResponseData())).getSujetrec());
            r.setCreatedat(ser.getOneReCc(new String(req.getResponseData())).getCreatedat());
             } catch (ParseException ex) {
                System.out.println(ex+"codenameone LOL");
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(req);

        return r;

        //Show Reclamation :
    }
      //Delete Reclamation
    public boolean deleteReclamation(int id) {
        String url = Statics.BASE_URL + "/m/" +id;

        req.setUrl(url);
        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                req.removeResponseCodeListener(this);
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(req);
        return resultOk;
    }
               
     }
                

