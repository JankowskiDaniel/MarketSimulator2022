package com.example.projectmarketsimulator2021_22;

import com.example.projectmarketsimulator2021_22.classes.Company;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controller of Buyout.fxml which is response for performing buy out operation of given company
 */
public class Buyout extends Controller{
    private Company choosen;

    @FXML
    private Label title;
    @FXML Label av_shares;
    @FXML private TextField valueshares;
    @FXML private Button submit;
    @FXML private Label msg;


    /**
     * Set choosen company
     * @param choosen
     */
    public void setCompany(Company choosen){
        this.choosen = choosen;
    }

    /**
     * Load labels with proper text
     */
    public void loadLabels(){
        this.title.setText("Force a buy out for "+choosen.getName());
        this.av_shares.setText("(Available shares: "+choosen.getCompanyshares().getVolume()+")");
    }

    /**
     * Switch back into CompanyInfo scene.
     * @param event
     * @throws IOException
     */
    public void switchCompanyInfo(ActionEvent event) throws IOException {
        ObservableList<Company> selected = FXCollections.observableArrayList();
        selected.add(this.choosen);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("CompanyInfo.fxml"));
        root = loader.load();
        CompanyInfo controller = loader.getController();
        controller.setWorld(this.world);
        controller.setCompany(selected);
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Validate field with value of shares, which user wants to buy out for given company.
     */
    public void validateField(){
        synchronized (choosen){
            numericOnly(this.valueshares);
            if(!this.valueshares.getText().isEmpty() && this.valueshares.getText().length() < 10){
                Integer value = Integer.valueOf(this.valueshares.getText());
                if(value<=choosen.getCompanyshares().getVolume()){
                    choosen.buyout(value);
                    msg.setText("Operation succeed!");
                } else {
                    msg.setText("Improper value!");
                }
            }

        }

    }

    /**
     * Allows to fill the TextField only with a numeric values.
     * @param field TextField
     */
    public static void numericOnly(final TextField field) {
        field.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(
                    ObservableValue<? extends String> observable,
                    String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    field.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
    }


}
