package com.example.projectmarketsimulator2021_22;

import com.example.projectmarketsimulator2021_22.classes.Asset;
import com.example.projectmarketsimulator2021_22.classes.Investor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.ResourceBundle;

import static javafx.scene.control.SelectionMode.MULTIPLE;

/**
 * Controller for InvestorInfo.fxml response for displaying information about investor
 */
public class InvestorInfo extends Controller implements Initializable {
    private Investor choosen;


    @FXML
    private Label type;
    @FXML
    private Label name;
    @FXML
    private Label name2;
    @FXML
    private Label id;
    @FXML
    private Label budget;
    @FXML
    private Label noinvestormsg;
    @FXML
    private TableView investorassets;
    @FXML
    private TableColumn<Asset, String> nameas = new TableColumn<Asset, String>();
    @FXML
    private TableColumn<Asset, BigDecimal> amountas = new TableColumn<Asset, BigDecimal>();


    private ObservableList<Asset> assets = FXCollections.observableArrayList();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nameas.setCellValueFactory(new PropertyValueFactory<Asset, String>("name"));
        amountas.setCellValueFactory(new PropertyValueFactory<Asset, BigDecimal>("amount"));
        investorassets.setItems(assets);
    }


    /**
     * Set selected investor as an investor, for which information will be displayed.
     * @param selected
     */
    public void setInvestor(ObservableList<Investor> selected){
        if(selected.size() == 0){
            noinvestormsg.setVisible(true);
        } else {
            this.choosen = selected.get(0);
            investorassets.setVisible(true);

            if (choosen.getType().equals("Fund")){
                labelFund();
            } else{
                labelPrivate();
            }
        }
    }

    /**
     * Load assets from selected trader account
     */
    public void loadAssets(){
        Hashtable<String, Asset> storage = choosen.getAccount().getAssets();
        Enumeration<String> s = storage.keys();
        while(s.hasMoreElements()){
            String key = s.nextElement();
            this.assets.add(storage.get(key));
        }
    }

    /**
     * Load labels for private investor
     */
    public void labelPrivate(){
        type.setText("Type: "+choosen.getType());
        name.setText("Name: "+choosen.getName()+" "+choosen.getLastname());
        id.setText("Account ID: "+choosen.getInvestorID());
        budget.setText("Budget: "+choosen.getAccount().GetBudget().toString());
    }

    /**
     * Load labels for fund
     */
    public void labelFund(){
        type.setText("Type: "+choosen.getType());
        name.setText("Manager: "+choosen.getName()+" "+choosen.getLastname());
        name2.setText("Fund name: "+choosen.getFundname());
        id.setText("Account ID: "+choosen.getInvestorID());
        budget.setText("Budget: "+choosen.getAccount().GetBudget().toString());
    }
}
