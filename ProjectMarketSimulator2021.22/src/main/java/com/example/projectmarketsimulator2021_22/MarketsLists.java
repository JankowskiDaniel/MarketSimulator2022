package com.example.projectmarketsimulator2021_22;

import com.example.projectmarketsimulator2021_22.classes.Market;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.ResourceBundle;

import static javafx.scene.control.SelectionMode.SINGLE;

/**
 * Controller for MarketsLists.fxml response for displaying table with market in the world
 */
public class MarketsLists extends Controller implements Initializable {
    TableView.TableViewSelectionModel<Market> selectionModel;
    @FXML
    private TableView<Market> table = new TableView<>();
    @FXML
    private TableColumn<Market, String> name = new TableColumn();
    @FXML private Label type;
    @FXML private Label country;
    @FXML private Label city;
    @FXML private Label margin;
    @FXML private Label currency;

    private ObservableList<Market> markets = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        name.setCellValueFactory(new PropertyValueFactory<Market, String>("name"));
        table.setItems(markets);
        selectionModel = table.getSelectionModel();
        selectionModel.setSelectionMode(SINGLE);
    }

    /**
     * Load markets from the world.
     */
    public void loadMarkets(){
        Hashtable<String, Market> storage = world.getMarketsStorage().getMarkets();
        Enumeration<String> s = storage.keys();
        while(s.hasMoreElements()){
            String key = s.nextElement();
            this.markets.add(storage.get(key));
        }
    }

    /**
     * Show information about selected market
     */
    public void showinformation(){
        ObservableList<Market> selected = selectionModel.getSelectedItems();
        currency.setVisible(false);
        if(selected.size() == 1){
            Market choosen = selected.get(0);
            type.setText("Type: "+choosen.getType());
            country.setText("Country: "+choosen.getCountry());
            city.setText("Address: "+choosen.getCity()+" ,"+choosen.getAddress());
            margin.setText("Operation fee: "+choosen.getOperation_fee().toString()+"%");
            if (!choosen.getType().equals("Currency Market")){
                currency.setText("Trading currency: "+choosen.getCurrency());
                currency.setVisible(true);

            }
        }

    }

}
