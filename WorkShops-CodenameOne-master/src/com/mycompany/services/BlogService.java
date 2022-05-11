package com.yummygout.services;

import com.codename1.components.InfiniteProgress;
import com.codename1.io.*;
import com.codename1.ui.events.ActionListener;
import com.yummygout.entities.Blog;
import com.yummygout.utils.Statics;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BlogService {

    public static BlogService instance = null;
    public int resultCode;
    private ConnectionRequest cr;
    private ArrayList<Blog> listBlogs;


    private BlogService() {
        cr = new ConnectionRequest();
    }

    public static BlogService getInstance() {
        if (instance == null) {
            instance = new BlogService();
        }
        return instance;
    }

    public ArrayList<Blog> getAll() {
        listBlogs = new ArrayList<>();

        cr = new ConnectionRequest();
        cr.setUrl(Statics.BASE_URL + "/blog");
        cr.setHttpMethod("GET");

        cr.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {

                if (cr.getResponseCode() == 200) {
                    listBlogs = getList();
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

        return listBlogs;
    }

    private ArrayList<Blog> getList() {
        try {
            Map<String, Object> parsedJson = new JSONParser().parseJSON(new CharArrayReader(
                    new String(cr.getResponseData()).toCharArray()
            ));
            List<Map<String, Object>> list = (List<Map<String, Object>>) parsedJson.get("root");

            for (Map<String, Object> obj : list) {
                Blog blog = new Blog(
                        (int) Float.parseFloat(obj.get("id").toString()),

                        (int) Float.parseFloat(obj.get("image").toString()),
                        (String) obj.get("titre"),
                        (String) obj.get("description")

                );

                listBlogs.add(blog);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listBlogs;
    }

    public int add(Blog blog) {
        return manage(blog, false);
    }

    public int edit(Blog blog) {
        return manage(blog, true);
    }

    public int manage(Blog blog, boolean isEdit) {

        cr = new ConnectionRequest();


        cr.setHttpMethod("POST");
        if (isEdit) {
            cr.setUrl(Statics.BASE_URL + "/blog/edit");
            cr.addArgument("id", String.valueOf(blog.getId()));
        } else {
            cr.setUrl(Statics.BASE_URL + "/blog/add");
        }

        cr.addArgument("image", String.valueOf(blog.getImage()));
        cr.addArgument("titre", blog.getTitre());
        cr.addArgument("description", blog.getDescription());


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

    public int delete(int blogId) {
        cr = new ConnectionRequest();
        cr.setUrl(Statics.BASE_URL + "/blog/delete");
        cr.setHttpMethod("POST");
        cr.addArgument("id", String.valueOf(blogId));

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
