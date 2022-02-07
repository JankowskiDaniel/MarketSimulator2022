package com.example.projectmarketsimulator2021_22;

import com.example.projectmarketsimulator2021_22.classes.Asset;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 *  Controller for AssetsGraph.fxml response for proper displaying graph of the asset's prices.
 */
public class AssetsGraph extends Controller{
    private ObservableList<Asset> selected = FXCollections.observableArrayList();
    @FXML
    private LineChart<String, BigDecimal> chart;
    @FXML
    private Label noassetmsg;


    /**
     * Set a graph for selected assets
     * @param selectedItems set of selected assets from AssetsLists scene.
     */
    public void setGraph(ObservableList<Asset> selectedItems){
        this.selected = selectedItems;
        if(selected.size() == 1){
            Asset a = selected.get(0);
            chart.setVisible(true);
            chart.getData().clear();
            chart.setTitle(a.getName());
            XYChart.Series<String, BigDecimal> series = loadAxis(a.getPrices());
            series.setName("Prices of "+a.getName()+" over time");
            chart.getData().add(series);
        } else if (selected.size() > 1) {
            chart.setVisible(true);
            chart.setTitle("Multiple assets chart");
            int no_assets = selected.size();
//            List<XYChart.Series> series = new ArrayList<>();
            for(int i=0; i<no_assets; i++){
                XYChart.Series<String, BigDecimal> s = loadAxis(selected.get(i).getPercentchanges());
                s.setName(selected.get(i).getName());
//                series.add(loadAxis(selected.get(i).getPrices()));
                chart.getData().add(s);
            }

        } else if (selected.size() == 0){
            noassetmsg.setVisible(true);
        }

    }

    /**
     * Load axis with given asset's prices
     * @param prices array with prices of an asset
     * @return series with data for a graph
     */
    public XYChart.Series loadAxis(ArrayList<BigDecimal> prices){
        XYChart.Series<String, BigDecimal> series = new XYChart.Series<String, BigDecimal>();
        Integer x = 1;
        for(Integer i=0; i<prices.size(); i++){
            series.getData().add(new XYChart.Data<String, BigDecimal>(String.valueOf(x), prices.get(i)));
            x+=1;
        }
        return series;
    }
}
