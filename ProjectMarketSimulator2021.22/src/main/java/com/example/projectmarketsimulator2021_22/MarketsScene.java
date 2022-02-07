package com.example.projectmarketsimulator2021_22;

import com.example.projectmarketsimulator2021_22.classes.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for MarketsScene.fxml response for creating markets
 */
public class MarketsScene extends Panel implements Initializable {
    @FXML
    protected ComboBox<String> marketType = new ComboBox<>();
    @FXML TextField country;
    @FXML TextField address;
    @FXML TextField city;
    @FXML TextField currency;
    @FXML Slider margin;
    @FXML Label marketsucc;
    @FXML Label marketerr;
    @FXML Label marketexist;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        marketType.getItems().addAll(world.getMarketTypes());
    }

    /**
     * Validate all fields related to the market creation
     */
    public void validateMarket() {
        this.marketexist.setVisible(false);
        this.marketerr.setVisible(false);
        this.marketsucc.setVisible(false);
        String marketName = primitiveName.getText().trim();
        String marketType = this.marketType.getValue();
        String country = this.country.getText().trim();
        String city = this.city.getText().trim();
        String address = this.address.getText().trim();
        String currency = this.currency.getText().trim();
        BigDecimal fee = BigDecimal.valueOf(this.margin.getValue()).setScale(2, RoundingMode.HALF_DOWN);
        if (marketName.isEmpty() || country.isEmpty() || city.isEmpty() || address.isEmpty()) {
            this.marketerr.setVisible(true);
        } else {
                String checker = marketType + "@" + marketName;
                if (world.getMarketsStorage().getMarkets().containsKey(checker)) {
                    this.marketexist.setVisible(true);
                } else {
                    this.marketsucc.setVisible(true);
                    createMarket(marketName, marketType, country, city, address, currency, fee);
                }
            }
    }

    /**
     * Load additional fields, whether we want to create a market with specified currency (i.e. stock and commodity market)
     */
    public void loadCurrency(){
        String marketType = this.marketType.getValue();
        if(!marketType.equals("Currency Market")){
            this.currency.setVisible(true);
        } else {
            this.currency.setVisible(false);
        }
    }

    /**
     * Load slider which will declare a market fee
     */
    public void loadSlider(){
        this.margin.setMajorTickUnit(0.1);
        this.margin.setMinorTickCount(1);
        this.margin.setShowTickMarks(true);
        this.margin.setShowTickLabels(true);
    }

    /**
     * Create market based on the data filled by user in the form
     * @param name
     * @param type
     * @param country
     * @param city
     * @param address
     * @param currency
     * @param fee
     */
    public void createMarket(String name, String type, String country, String city, String address, String currency, BigDecimal fee){
        if (type.equals("Currency Market")){
            CurrencyMarket NewMarket = new CurrencyMarket(name, country, city, address, fee);
            world.getMarketsStorage().addMarket(NewMarket, NewMarket.toString());
        } else if (type.equals("Commodity Market")){
            CommodityMarket NewMarket = new CommodityMarket(name, country, city, address, currency, fee);
            world.getMarketsStorage().addMarket(NewMarket, NewMarket.toString());
        } else if (type.equals("Stock Market")){
            StockMarket NewMarket = new StockMarket(name, country, city, address, currency, fee);
            world.getMarketsStorage().addMarket(NewMarket, NewMarket.toString());
        } else {
            System.out.println("Type market error");
        }
    }
}
