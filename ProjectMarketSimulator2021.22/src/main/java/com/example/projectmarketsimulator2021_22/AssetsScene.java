package com.example.projectmarketsimulator2021_22;

import com.example.projectmarketsimulator2021_22.classes.Commodity;
import com.example.projectmarketsimulator2021_22.classes.CommodityMarket;
import com.example.projectmarketsimulator2021_22.classes.Currency;
import com.example.projectmarketsimulator2021_22.classes.CurrencyMarket;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Controller for AssetsScene.fxml response for validate and create assets on given markets.
 */
public class AssetsScene extends Panel implements Initializable {
    private final ObservableList<String> assetstypes = FXCollections.observableArrayList("Currency", "Commodity");

    @FXML
    protected ComboBox assets = new ComboBox(assetstypes);

    @FXML
    private TextField shortname = new TextField();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        assets.setItems(assetstypes);
    }

    /**
     * Method which validates form and creates asset on choosen market.
     */
    public void createAsset() {
        //TODO validation of textfield and choosen market
        if( validateName(this.primitiveName) && validateName(this.shortname)){
            String assetname = this.primitiveName.getText();
            String shortname = this.shortname.getText();
            String selectedmarket = (String) this.markets.getValue();
            String asset = (String) this.assets.getValue();
            if (asset.equals("Currency")) {
                String marketid = "Currency Market@" + selectedmarket;
                Currency currency = new Currency(assetname, shortname, selectedmarket);
                if(((CurrencyMarket) world.getMarketsStorage().getMarkets().get(marketid)).getCurrencies().containsKey(currency.getShortname())){
                    System.out.println("this market has this currency");
                } else {
                    ((CurrencyMarket) world.getMarketsStorage().getMarkets().get(marketid)).addCurrency(currency);
                    world.getAssetsLocker().addAsset(currency, currency.toString());
                }
            } else if (asset.equals("Commodity")) {
                String marketid = "Commodity Market@" + selectedmarket;
                String currency = ((CommodityMarket) world.getMarketsStorage().getMarkets().get(marketid)).getCurrency();
                Commodity commodity = new Commodity(assetname, shortname, selectedmarket, currency);
                if (((CommodityMarket) world.getMarketsStorage().getMarkets().get(marketid)).getCommodities().containsKey(commodity.getName())){
                    System.out.println("this market has this commodity");
                } else {
                    ((CommodityMarket) world.getMarketsStorage().getMarkets().get(marketid)).addCommodity(commodity);
                    world.getAssetsLocker().addAsset(commodity, commodity.toString());
                }

            }
        }
    }

    /**
     * Method which loads markets for selected type of an asset. If user select commodity, then only commodities markets
     * are loaded, etc..
     */
    public void loadMarketsAssets() {
        String asset = (String) this.assets.getValue();
        marketsnames.removeAll(marketsnames);
        if (asset.equals("Currency")) {
            ArrayList<String> names = world.getMarketsStorage().getTypeNames("Currency Market");
            marketsnames.addAll(names);
            markets.setItems(marketsnames);
        } else if (asset.equals("Commodity")) {
            ArrayList<String> names = world.getMarketsStorage().getTypeNames("Commodity Market");
            marketsnames.addAll(names);
            markets.setItems(marketsnames);
        }
    }
}
