package com.example.projectmarketsimulator2021_22;

import com.example.projectmarketsimulator2021_22.classes.Fund;
import com.example.projectmarketsimulator2021_22.classes.PrivateInvestor;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for InvestorsScene.fxml response for creating investors
 */
public class InvestorsScene extends Panel implements Initializable {
    @FXML
    private ComboBox investorTypeCombo = new ComboBox();
    @FXML
    private Label fundinf = new Label();
    @FXML
    private TextField fundname = new TextField();
    @FXML
    private TextField lastname = new TextField();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        investorTypeCombo.getItems().addAll(world.getInvestorTypes());
    }

    /**
     * Validate fields and create investor
     */
    public void createInvestor(){
        if( validateName(this.primitiveName) && validateName(this.lastname)){
            String type = (String) this.investorTypeCombo.getValue();
            String name = this.primitiveName.getText();
            String lastname = this.lastname.getText();
            if( type.equals("Private Investor")){
              PrivateInvestor inv = new PrivateInvestor(name, lastname);
//                (new Thread(inv)).start();
              world.getInvestorsLocker().addInvestor(inv, inv.toString());

            } else if (type.equals("Fund")){
                if(validateName(this.fundname)){
                    String fundname = this.fundname.getText();
                    Fund fund = new Fund(name, lastname, fundname);
//                    (new Thread(fund)).start();
                    world.getInvestorsLocker().addInvestor(fund, fund.toString());
                }


            }
        }
    }

    /**
     * Load additional field, when user want to create a fund
     */
    public void loadFund(){
        this.validmsg.setText(null);
        String type = (String) this.investorTypeCombo.getValue();
        if (type.equals("Fund")){
            this.fundinf.setVisible(true);
            this.fundname.setVisible(true);

        } else if (type.equals("Private Investor")){
            this.fundinf.setVisible(false);
            this.fundname.setVisible(false);
        }
    }
}
