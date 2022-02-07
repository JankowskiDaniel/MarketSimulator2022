package com.example.projectmarketsimulator2021_22;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;

import java.util.ArrayList;

/**
 * Controller for Panel.fxml which is response to display all possible options in the controll panel
 */
public class Panel extends Controller {
    protected ObservableList<Object> marketsnames = FXCollections.observableArrayList(world.getMarketsStorage().getTypeNames("Stock Market"));

    @FXML
    protected TextField primitiveName;
    @FXML
    protected Label validmsg = new Label();

    @FXML
    protected ComboBox markets = new ComboBox(marketsnames);
    @FXML
    protected Label marketCreationMsg;

    @FXML private Slider speed;
    @FXML private Label warningspeed;


    /**
     * Set combobox with markets. This method is also inherited from other controllers
     * @param arr
     */
    public void setComboBox(ArrayList arr){
        markets.getItems().addAll(arr);
    }

    /**
     * Method which is used by other scenes to validate name field ( e.g. market name, asset name etc..)
     * @param field
     * @return
     */
    public boolean validateName(TextField field){
        String name = field.getText();
        if (name == null || name.trim().isEmpty()){
            this.validmsg.setText("Error name");
            return false;
        } else {
            this.validmsg.setText(null);
            return true;
        }
    }

    public void setSpeed(){
        int value = (int)speed.getValue();
        world.setSpeed(value);
        this.warningspeed.setVisible(true);
    }



}
