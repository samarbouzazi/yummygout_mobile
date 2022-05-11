package com.yummygout.gui.back.stock;

import com.codename1.components.InteractionDialog;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.yummygout.entities.Stock;
import com.yummygout.services.StockService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ShowAll extends Form {

    public static Stock currentStock = null;
    Form previous;
    Button addBtn;

    TextField searchTF;
    ArrayList<Component> componentModels;
    Label fournisseurLabel, nomLabel, dateAjoutLabel, dateFinLabel, prixLabel, quantiteLabel;
    Button editBtn, deleteBtn;
    Container btnsContainer;

    public ShowAll(Form previous) {
        super("Stocks", new BoxLayout(BoxLayout.Y_AXIS));
        this.previous = previous;

        addGUIs();
        addActions();

        super.getToolbar().addMaterialCommandToLeftBar("  ", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
    }

    public void refresh() {
        this.removeAll();
        addGUIs();
        addActions();
        this.refreshTheme();
    }

    private void addGUIs() {
        addBtn = new Button("Ajouter");
        addBtn.setUIID("buttonWhiteCenter");
        this.add(addBtn);


        ArrayList<Stock> listStocks = StockService.getInstance().getAll();
        componentModels = new ArrayList<>();

        searchTF = new TextField("", "Chercher stock par Nom");
        searchTF.addDataChangedListener((d, t) -> {
            if (componentModels.size() > 0) {
                for (Component componentModel : componentModels) {
                    this.removeComponent(componentModel);
                }
            }
            componentModels = new ArrayList<>();
            for (Stock stock : listStocks) {
                if (stock.getNom().toLowerCase().startsWith(searchTF.getText().toLowerCase())) {
                    Component model = makeStockModel(stock);
                    this.add(model);
                    componentModels.add(model);
                }
            }
            this.revalidate();
        });
        this.add(searchTF);


        if (listStocks.size() > 0) {
            for (Stock stock : listStocks) {
                Component model = makeStockModel(stock);
                this.add(model);
                componentModels.add(model);
            }
        } else {
            this.add(new Label("Aucune donnée"));
        }
    }

    private void addActions() {
        addBtn.addActionListener(action -> {
            currentStock = null;
            new Manage(this).show();
        });

    }

    private Container makeModelWithoutButtons(Stock stock) {
        Container stockModel = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        stockModel.setUIID("containerRounded");


        fournisseurLabel = new Label("Fournisseur : " + stock.getFournisseur());
        fournisseurLabel.setUIID("labelDefault");

        nomLabel = new Label("Nom : " + stock.getNom());
        nomLabel.setUIID("labelDefault");

        dateAjoutLabel = new Label("DateAjout : " + new SimpleDateFormat("dd-MM-yyyy").format(stock.getDateAjout()));
        dateAjoutLabel.setUIID("labelDefault");

        dateFinLabel = new Label("DateFin : " + new SimpleDateFormat("dd-MM-yyyy").format(stock.getDateFin()));
        dateFinLabel.setUIID("labelDefault");

        prixLabel = new Label("Prix : " + stock.getPrix());
        prixLabel.setUIID("labelDefault");

        quantiteLabel = new Label("Quantite : " + stock.getQuantite());
        quantiteLabel.setUIID("labelDefault");

        fournisseurLabel = new Label("Fournisseur : " + stock.getFournisseur().getNom());
        fournisseurLabel.setUIID("labelDefault");


        stockModel.addAll(

                fournisseurLabel, nomLabel, dateAjoutLabel, dateFinLabel, prixLabel, quantiteLabel
        );

        return stockModel;
    }

    private Component makeStockModel(Stock stock) {

        Container stockModel = makeModelWithoutButtons(stock);

        btnsContainer = new Container(new BorderLayout());
        btnsContainer.setUIID("containerButtons");

        editBtn = new Button("Modifier");
        editBtn.setUIID("buttonWhiteCenter");
        editBtn.addActionListener(action -> {
            currentStock = stock;
            new Manage(this).show();
        });

        deleteBtn = new Button("Supprimer");
        deleteBtn.setUIID("buttonWhiteCenter");
        deleteBtn.addActionListener(action -> {
            InteractionDialog dlg = new InteractionDialog("Confirmer la suppression");
            dlg.setLayout(new BorderLayout());
            dlg.add(BorderLayout.CENTER, new Label("Voulez vous vraiment supprimer ce stock ?"));
            Button btnClose = new Button("Annuler");
            btnClose.addActionListener((ee) -> dlg.dispose());
            Button btnConfirm = new Button("Confirmer");
            btnConfirm.addActionListener(actionConf -> {
                int responseCode = StockService.getInstance().delete(stock.getId());

                if (responseCode == 200) {
                    currentStock = null;
                    dlg.dispose();
                    stockModel.remove();
                    this.refreshTheme();
                } else {
                    Dialog.show("Erreur", "Erreur de suppression du stock. Code d'erreur : " + responseCode, new Command("Ok"));
                }
            });
            Container btnContainer = new Container(new BoxLayout(BoxLayout.X_AXIS));
            btnContainer.addAll(btnClose, btnConfirm);
            dlg.addComponent(BorderLayout.SOUTH, btnContainer);
            dlg.show(800, 800, 0, 0);
        });

        btnsContainer.add(BorderLayout.WEST, editBtn);
        btnsContainer.add(BorderLayout.EAST, deleteBtn);


        stockModel.add(btnsContainer);

        return stockModel;
    }

}