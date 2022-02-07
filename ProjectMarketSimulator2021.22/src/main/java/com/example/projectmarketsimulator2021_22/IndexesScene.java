package com.example.projectmarketsimulator2021_22;

import com.example.projectmarketsimulator2021_22.classes.StockMarket;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.util.ArrayList;

/**
 * Controller for IndexesScene.fxml response for creating indexes
 */
public class IndexesScene extends Panel {

    @FXML
    private Label indexCreateMsg;


    /**
     * Validate fields and create index on selected stock market
     */
    public void createIndex() {
        ArrayList names = world.getMarketsStorage().getTypeNames("Stock Market");

        boolean isEmpty = this.markets.getSelectionModel().isEmpty();
        if (isEmpty || names.size() == 0) {
            indexCreateMsg.setText("Market not selected or there is no available stock market to select!");
        } else {
            String indexname = primitiveName.getText();
            String selectedmarket = (String) this.markets.getValue();
            if (indexname == null || indexname.trim().isEmpty()) {
                this.marketCreationMsg.setText("Market name not entered!");
            } else {
                // TODO check if that index have not already exist in that market
                String marketid = "Stock Market@" + selectedmarket;
                ((StockMarket) world.getMarketsStorage().getMarkets().get(marketid)).addIndex(indexname);
            }
        }
    }
}
