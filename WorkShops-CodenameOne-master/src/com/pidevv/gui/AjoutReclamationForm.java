/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.gui;

import com.codename1.components.InfiniteProgress;
import com.codename1.components.ScaleImageLabel;
import com.codename1.components.SpanLabel;
import com.codename1.l10n.SimpleDateFormat;
import com.codename1.ui.Button;
import com.codename1.ui.ButtonGroup;
import com.codename1.ui.Component;
import com.codename1.ui.Container;
import com.codename1.ui.Dialog;
import com.codename1.ui.Display;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.Graphics;
import com.codename1.ui.Image;
import com.codename1.ui.Label;
import com.codename1.ui.RadioButton;
import com.codename1.ui.Tabs;
import com.codename1.ui.TextField;
import com.codename1.ui.Toolbar;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.layouts.GridLayout;
import com.codename1.ui.layouts.LayeredLayout;
import com.codename1.ui.plaf.Style;
import com.codename1.ui.util.Resources;
import com.yummygout.entities.Smsmarwa;
import com.yummygout.entities.reclamationliv;
import com.yummygout.services.servicereclamation;
import com.yummygout.gui.back.categorie.Manage;
import static com.yummygout.gui.back.categorie.ShowAll.currentCategorie;

/**
 *
 * @author HP
 */
public class AjoutReclamationForm  extends BaseForm {
     Form current ;
      Button editBtn;
               Container btnsContainer;
    public AjoutReclamationForm(Resources res){
       
    super("Newsfeed",BoxLayout.y());
        Toolbar tb = new Toolbar(true);
        current= this;
        setToolbar(tb);
        getTitleArea().setUIID("container");
        setTitle("Ajout Reclamation");
        getContentPane().setScrollVisible(false);
        tb.addSearchCommand(e->{
        
        });
        Tabs swipe = new Tabs();
        Label s1=  new Label();
         Label s2 =  new Label();
         addTab(swipe ,s1,res.getImage("activation-background.jpg"),"","",res);
         //
         
           swipe.setUIID("Container");
        swipe.getContentPane().setUIID("Container");
        swipe.hideTabs();

        ButtonGroup bg = new ButtonGroup();
        int size = Display.getInstance().convertToPixels(1);
        Image unselectedWalkthru = Image.createImage(size, size, 0);
        Graphics g = unselectedWalkthru.getGraphics();
        g.setColor(0xffffff);
        g.setAlpha(100);
        g.setAntiAliased(true);
        g.fillArc(0, 0, size, size, 0, 360);
        Image selectedWalkthru = Image.createImage(size, size, 0);
        g = selectedWalkthru.getGraphics();
        g.setColor(0xffffff);
        g.setAntiAliased(true);
        g.fillArc(0, 0, size, size, 0, 360);
        RadioButton[] rbs = new RadioButton[swipe.getTabCount()];
        FlowLayout flow = new FlowLayout(CENTER);
        flow.setValign(BOTTOM);
        Container radioContainer = new Container(flow);
        for (int iter = 0; iter < rbs.length; iter++) {
            rbs[iter] = RadioButton.createToggle(unselectedWalkthru, bg);
            rbs[iter].setPressedIcon(selectedWalkthru);
            rbs[iter].setUIID("Label");
            radioContainer.add(rbs[iter]);
        }

        rbs[0].setSelected(true);
        swipe.addSelectionListener((i, ii) -> {
            if (!rbs[ii].isSelected()) {
                rbs[ii].setSelected(true);
            }
        });

        Component.setSameSize(radioContainer, s1, s2);
        add(LayeredLayout.encloseIn(swipe, radioContainer));

        ButtonGroup barGroup = new ButtonGroup();
        RadioButton mesListes = RadioButton.createToggle("Mes Reclamations", barGroup);
        mesListes.setUIID("SelectBar");
        RadioButton liste = RadioButton.createToggle("Autres", barGroup);
        liste.setUIID("SelectBar");
        RadioButton partage = RadioButton.createToggle("Reclamer", barGroup);
        partage.setUIID("SelectBar");
        Label arrow = new Label(res.getImage("news-tab-down-arrow.png"), "Container");


        mesListes.addActionListener((e) -> {
               InfiniteProgress ip = new InfiniteProgress();
        final Dialog ipDlg = ip.showInifiniteBlocking();
        
         ListReclamationFormmarwa a = new  ListReclamationFormmarwa(res);
            a.show();
            refreshTheme();
        });

        partage.addActionListener((e) -> {
               InfiniteProgress ip = new InfiniteProgress();
        final Dialog ipDlg = ip.showInifiniteBlocking();
        
          AjoutReclamationForm a = new AjoutReclamationForm(res);
            a.show();
            refreshTheme();
        });

        add(LayeredLayout.encloseIn(
                GridLayout.encloseIn(3, mesListes, liste, partage),
                FlowLayout.encloseBottom(arrow)
        ));

        partage.setSelected(true);
        arrow.setVisible(false);
        addShowListener(e -> {
            arrow.setVisible(true);
            updateArrowPosition(partage, arrow);
        });
        bindButtonSelection(mesListes, arrow);
        bindButtonSelection(liste, arrow);
        bindButtonSelection(partage, arrow);
        // special case for rotation
        addOrientationListener(e -> {
            updateArrowPosition(barGroup.getRadioButton(barGroup.getSelectedIndex()), arrow);
        });

       
         //
        
        TextField reclamation = new TextField("","entrer reclamation")
        ;
        reclamation.setUIID("textFieldBlack");
        
         addStringValue("reclamation",reclamation);
        TextField sujetrec = new TextField("","entrer sujet")
        ;
        reclamation.setUIID("textFieldBlack");
        
         addStringValue("sujetrec",sujetrec);
         
         Button  btnajouter = new Button("Ajouter");
     addStringValue("",btnajouter);
     
     btnajouter.addActionListener((e) -> {
    
      try {
                if (reclamation.getText()==" "||sujetrec.getText()==" ") {
                    Dialog.show("Veuillez vérifier les données", "", "Annuler", "OK");

                } 
                else{
                     InfiniteProgress ip = new InfiniteProgress(); //loading start
                    final Dialog iDialog = ip.showInfiniteBlocking();
               reclamationliv r = new reclamationliv(
                            String.valueOf(reclamation.getText()),
                            String.valueOf(sujetrec.getText()));
                System.out.println(r);
                servicereclamation.getInstance().ajouterrrRec(r);
               ///// Smsmarwa.send1("your complaint has has been submitted \n"+String.valueOf(reclamation.getText()));
                iDialog.dispose(); //end loading
                new ListReclamationFormmarwa(res).show();
                    refreshTheme();
                }
     }catch (Exception ex) {
                ex.printStackTrace();
            }
        });
      
 
 
     
    }
    private void addStringValue(String s, Component v) {
        add(BorderLayout.west(new Label(s, "PadderLabel"))
                .add(BorderLayout.CENTER, v));
        add(createLineSeparator(0xeeeeee));
    }

    private void addTab(Tabs swipe, Label spacer, Image image, String string, String text, Resources res) {
int size = Math.min(Display.getInstance().getDisplayWidth(), Display.getInstance().getDisplayHeight());

       ScaleImageLabel imageScale = new ScaleImageLabel(image);
       imageScale.setUIID("container");
       imageScale.setBackgroundType(Style.BACKGROUND_IMAGE_SCALED_FILL);
       Label overLay = new Label("","ImageOverlay");
        Container page1 = LayeredLayout.encloseIn(imageScale, 
        overLay,
        BorderLayout.south(BoxLayout.encloseX(
        new SpanLabel(text, "largeWhiteText"),
                spacer
                
        )
        )
                
        );
              swipe.addTab("", res.getImage("activation-background.jpg"), page1);
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
        l.getParent().repaint();    }
    
    
}
