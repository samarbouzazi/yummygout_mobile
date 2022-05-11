/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.gui;

import com.codename1.components.SpanLabel;

import com.codename1.ui.Button;
import com.codename1.ui.ButtonGroup;
import com.codename1.ui.CN;
import com.codename1.ui.Container;
import com.codename1.ui.Dialog;
import com.codename1.ui.EncodedImage;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.Image;
import com.codename1.ui.Label;
import com.codename1.ui.RadioButton;

import com.codename1.ui.Toolbar;
import com.codename1.ui.URLImage;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.layouts.GridLayout;
import com.codename1.ui.layouts.LayeredLayout;
import com.codename1.ui.plaf.Style;
import com.codename1.ui.util.Resources;
import com.yummygout.entities.reclamationliv;
import com.yummygout.services.servicereclamation;
import java.util.ArrayList;

public class ListReclamationFormmarwa extends BaseForm {

    public static String stringuewi;
    public static Integer id_reclamation;
    public static int counter_m;
    public static int counter_om;
    public static int marr = 10;
    public static int oss;
    Form current;
    final Button show = new Button("Show Dialog");
    final Button showPopup = new Button("Show Popup");
//Form verticalConatainer = new Form("", new BoxLayout(BoxLayout.Y_AXIS));

    public ListReclamationFormmarwa(Resources res) {

        super("Newsfeed", BoxLayout.y());
        Toolbar tb = new Toolbar(true);
        current = this;
        setToolbar(tb);
        getTitleArea().setUIID("Container");
        setTitle("Ajouter Réclamation");
        getContentPane().setScrollVisible(true);

        super.addSideMenu(res);
        ButtonGroup barGroup = new ButtonGroup();
        RadioButton listeReclamations = RadioButton.createToggle("mes reclamations", barGroup);
        listeReclamations.setUIID("SelectBar");

        RadioButton ajoutReclamation = RadioButton.createToggle("Ajouter Reclamation", barGroup);
        ajoutReclamation.setUIID("SelectBar");
        Label arrow = new Label(res.getImage("news-tab-down-arrow.png"), "Container");

        listeReclamations.addActionListener((e) -> {

            ListReclamationFormmarwa a = new ListReclamationFormmarwa(res);
            a.show();
            refreshTheme();
        });
        ajoutReclamation.addActionListener((e) -> {

            new AjoutReclamationForm(res).show();
            refreshTheme();
        });

        add(LayeredLayout.encloseIn(
                GridLayout.encloseIn(2, listeReclamations, ajoutReclamation),
                FlowLayout.encloseBottom(arrow)
        ));

        listeReclamations.setSelected(true);
        arrow.setVisible(false);
        addShowListener(e -> {
            arrow.setVisible(true);
            updateArrowPosition(listeReclamations, arrow);
        });
        bindButtonSelection(listeReclamations, arrow);

        bindButtonSelection(ajoutReclamation, arrow);
        // special case for rotation
        addOrientationListener(e -> {
            updateArrowPosition(barGroup.getRadioButton(barGroup.getSelectedIndex()), arrow);
        });

        ArrayList<reclamationliv> arR = servicereclamation.getInstance().AffichageReclamation();
         System.out.println(arR);
        
        for (int counter = 0; counter < arR.size(); counter++) {
    //supprimer button
        Label lSupprimer = new Label(" ");
        lSupprimer.setUIID("NewsTopLine");
        Style supprmierStyle = new Style(lSupprimer.getUnselectedStyle());
        supprmierStyle.setFgColor(0xf21f1f);
        
        FontImage suprrimerImage = FontImage.createMaterial(FontImage.MATERIAL_DELETE, supprmierStyle);
        lSupprimer.setIcon(suprrimerImage);
        lSupprimer.setTextPosition(RIGHT);
        
        //click delete icon
        lSupprimer.addPointerPressedListener(l -> {
            
            Dialog dig = new Dialog("Suppression");
            
            if(dig.show("Suppression","Vous voulez supprimer ce reclamation ?","Annuler","Oui")) {
                dig.dispose();
            }
            else {
                dig.dispose();
                 }
            reclamationliv rec = new reclamationliv();
                //n3ayto l suuprimer men service Reclamation
                if(servicereclamation.getInstance().deleteReclamation(rec.getIdreclivv())) {
                    new ListReclamationFormmarwa(res).show();
                }
           
        });
            Label showrec = new Label(" ");
    
            Style showStyle = new Style(showrec.getUnselectedStyle());
            showStyle.setFgColor(0xf21f1f);
            FontImage showImage = FontImage.createMaterial(FontImage.MATERIAL_REMOVE_RED_EYE, showStyle);
            showrec.setIcon(showImage);
            showrec.setTextPosition(RIGHT);

            showrec.addPointerPressedListener(bandit -> {

                reclamationliv rrr = new reclamationliv();
                rrr = servicereclamation.getInstance().afficheDetail(Integer.parseInt(showrec.getUIID()));

                ListReclamationFormmarwa.stringuewi = rrr.getIdreclivv()+ "\n" + rrr.getReclamation()+ "\n" + rrr.getSujetrec();
                
              
                Dialog dlgshow = new Dialog("show reclamation", BoxLayout.y());

//                dlgshow.add(new SpanLabel(ListReclamationFormmarwa.stringuewi));
//
//                EncodedImage placeholder = EncodedImage.createFromImage(Image.createImage(CN.convertToPixels(30), CN.convertToPixels(30), 0x00ffffff), true);
//                URLImage meditationIcon = URLImage.createToStorage(placeholder, rrr.getPiece_jointe(),
//                        path, URLImage.RESIZE_SCALE_TO_FILL);
//                meditationIcon.fetch();
//                dlgshow.add(FlowLayout.encloseCenter(new Label(meditationIcon)));
//                dlgshow.setDisposeWhenPointerOutOfBounds(true);
//                dlgshow.show();
//
//                System.out.println(path);

//add(dlgshow);
                //counter_s=0;
                //}
            });

            Label reply = new Label(" ");
            //reply.setUIID(arR.get(counter).getIdreclivv().toString());
            FontImage replyImage = FontImage.createMaterial(FontImage.MATERIAL_SEND, showStyle);
            reply.setIcon(replyImage);
            reply.addPointerPressedListener((ezzz) -> {
                ListReclamationFormmarwa.oss = Integer.parseInt(showrec.getUIID());
                refreshTheme();
            });

            Label whiteLine = new Label();
            whiteLine.setShowEvenIfBlank(true);
            whiteLine.getUnselectedStyle().setBgColor(0xff2c54);
            whiteLine.getUnselectedStyle().setBgTransparency(255);
            whiteLine.getUnselectedStyle().setPaddingUnit(Style.UNIT_TYPE_DIPS);
            whiteLine.getUnselectedStyle().setPadding(1, 0, 200, 1);
            Container cnt = new Container(new BorderLayout());
            cnt.add(BorderLayout.NORTH, new Label(arR.get(counter).getSujetrec(), "TextFieldRed"));
            String stringuewi2 = arR.get(counter).getReclamation()+ "\n \n" +arR.get(counter).getSujetrec()+ "\n \n" +arR.get(counter).getCreatedat().toString();
            cnt.add(BorderLayout.CENTER, BoxLayout.encloseY(BoxLayout.encloseX(new SpanLabel(stringuewi2))));

            cnt.add(BorderLayout.EAST, BoxLayout.encloseY(BoxLayout.encloseX(showrec, lSupprimer, reply)));
            cnt.add(BorderLayout.SOUTH, whiteLine);
            add(cnt);

        }

    }

    public void bindButtonSelection(Button btn, Label l) {

        btn.addActionListener(e -> {
            if (btn.isSelected()) {
                updateArrowPosition(btn, l);
            }
        });
    }

    private void updateArrowPosition(Button btn, Label l) {
        l.getUnselectedStyle().setMargin(LEFT, btn.getX() + btn.getWidth() / 2 - l.getWidth() / 2);
        l.getParent().repaint();
    }

}
