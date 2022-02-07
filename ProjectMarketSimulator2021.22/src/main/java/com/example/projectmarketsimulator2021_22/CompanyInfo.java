package com.example.projectmarketsimulator2021_22;

import com.example.projectmarketsimulator2021_22.classes.Company;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Controller for CompanyInfo.fxml response for displaying information about a company
 */
public class CompanyInfo extends Controller{
    private Company choosen;

    @FXML
    private Label name;
    @FXML
    private Label nocompanymsg;
    @FXML
    private Label market;
    @FXML private Label idate;
    @FXML private Label ivalue;
    @FXML private Label oprice;
    @FXML private Label acprice;
    @FXML private Label capital;
    @FXML private Label avshares;
    @FXML private Button buyout = new Button();
    @FXML private Button addindx = new Button();
    @FXML
    private LineChart<String, BigDecimal> chart;


    /**
     * Set selected company as a company for which it displays information
     * @param selected
     */
    public void setCompany(ObservableList<Company> selected){
        if(selected.size() == 0){
            nocompanymsg.setVisible(true);
        } else {
            this.choosen = selected.get(0);
            loadLabels();
            loadChart();

        }
    }

    /**
     * Load labels with proper text
     */
    public void loadLabels(){
        name.setText(choosen.getName());
        name.setVisible(true);
        market.setText("Market: "+choosen.getMarket());
        idate.setText("IPO date: "+choosen.getIpo_date());
        oprice.setText("Openning price: "+choosen.getOpen_price().toString()+" "+choosen.getCurrency());
        capital.setText("Capital: "+choosen.getCapital().toString()+" "+choosen.getCurrency());
        avshares.setText("Available shares: "+String.valueOf(choosen.getNo_shares()));
        ivalue.setText("IPO value: "+choosen.getIpoShareValue().toString()+" "+choosen.getCurrency());
        acprice.setText("Actual price: "+choosen.getCompanyshares().getPrice());
        market.setVisible(true);
        idate.setVisible(true);
        ivalue.setVisible(true);
        oprice.setVisible(true);
        capital.setVisible(true);
        avshares.setVisible(true);
        acprice.setVisible(true);
        buyout.setVisible(true);
        addindx.setVisible(true);
    }

    /**
     * Load chart of stock prices of given company
     */
    public void loadChart(){
        chart.setVisible(true);
        chart.getData().clear();
        XYChart.Series<String, BigDecimal> series = loadAxis(choosen.getCompanyshares().getPrices());
        series.setName("Share prices of "+choosen.getName());
        chart.getData().add(series);

    }
    public XYChart.Series loadAxis(ArrayList<BigDecimal> prices){
        XYChart.Series<String, BigDecimal> series = new XYChart.Series<String, BigDecimal>();
        Integer x = 1;
        for(Integer i=0; i<prices.size(); i++){
            series.getData().add(new XYChart.Data<String, BigDecimal>(String.valueOf(x), prices.get(i)));
            x+=1;
        }
        return series;
    }

    /**
     * Switch to the BuyOut.fxml scene
     * @param event
     * @throws IOException
     */
    public void switchBuyout(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Buyout.fxml"));
        root = loader.load();
        Buyout controller = loader.getController();
        controller.setWorld(this.world);
        controller.setCompany(this.choosen);
        controller.loadLabels();
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Switch to CompanyAddIndex scene
     * @param event
     * @throws IOException
     */
    public void switchAddIndex(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("CompanyAddIndex.fxml"));
        root = loader.load();
        CompanyAddIndex controller = loader.getController();
        controller.setWorld(this.world);
        controller.setCompany(this.choosen);
        controller.loadLabels();
        controller.loadIndexes();
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}
