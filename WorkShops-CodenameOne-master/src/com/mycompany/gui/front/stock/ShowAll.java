package com.yummygout.gui.front.stock;

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
    TextField searchTF;
    ArrayList<Component> componentModels;
    Label fournisseurLabel, nomLabel, dateAjoutLabel, dateFinLabel, prixLabel, quantiteLabel;
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


        stockModel.add(btnsContainer);

        return stockModel;
    }

}